package com.imooc.service.impl;

import com.imooc.bean.Order;
import com.imooc.bean.SalesStatistics;
import com.imooc.bean.User;
import com.imooc.constant.ApiCodeEnum;
import com.imooc.constant.CommentStateConstant;
import com.imooc.constant.FinalName;
import com.imooc.dao.OrderDao;
import com.imooc.dao.UserDao;
import com.imooc.dto.ApiCodeDto;
import com.imooc.dto.OrderBuyDto;
import com.imooc.dto.OrderDto;
import com.imooc.dto.OrderListDto;
import com.imooc.entity.Page;
import com.imooc.service.OrderService;
import com.imooc.util.VerifyUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/10 14:56
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Value("${business.url}")
    private String imgUrl;
    @Value("${order.pageSize}")
    private Integer orderPageSize;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserDao userDao;
    @Override
    public ApiCodeDto order(OrderBuyDto orderBuyDto) {
        //判断参数是否有问题
        if (orderBuyDto == null ||StringUtils.isBlank(orderBuyDto.getUsername())){
            return new ApiCodeDto(ApiCodeEnum.PARAM_NULL);
        }
        //判断是否有token，有，则继续验证；无，则未登录
        if (StringUtils.isBlank(orderBuyDto.getToken())){
            return new ApiCodeDto(ApiCodeEnum.NOT_LOGIN);
        }
        //判断用户是否存在
        Long phone = Long.valueOf(orderBuyDto.getUsername());
        User user = userDao.selectUserByPhone(phone);
        if (user == null){
            return new ApiCodeDto(ApiCodeEnum.USER_NOT_EXISTS);
        }
        //验证token是否正确
        switch (VerifyUtil.verifyToken(orderBuyDto.getUsername(), orderBuyDto.getToken())){
            case 0:
                //继续执行程序
                break;
            case 1:
                //登录凭证不匹配
                return new ApiCodeDto(ApiCodeEnum.TOKEN_ERROR);
            case 2:
                //登录失效或未登录
                return new ApiCodeDto(ApiCodeEnum.TOKEN_EXPIRE);
            default:
        }
        //生成订单
        if (!saveOrder(orderBuyDto, user.getId())){
            return new ApiCodeDto(ApiCodeEnum.BUY_FAILURE);
        }
        return new ApiCodeDto(ApiCodeEnum.SUCCESS);
    }

    @Override
    public List<OrderDto> getOrderListByPhoneForApi(String phone) {
        Page page = new Page();
        page.setPageSize(orderPageSize);
        return getOrderListByPhone(phone, page, true);
    }

    @Override
    public OrderListDto getOrderListByPhoneForApiByTest(String phone) {
        Page page = new Page();
        page.setPageSize(orderPageSize);
        List<OrderDto> orderDtoList = getOrderListByPhone(phone, page, true);
        return wrapOrderListDto(orderDtoList, page);
    }

    @Override
    public void getOrderList(Model model) {
        Order order = new Order();
        Page page = new Page();
        order.setPage(page);
        model.addAttribute(FinalName.RESUKT_LIST, getOrderListByPage(order, false));
        model.addAttribute(FinalName.SEARCH_PARAM, order);
    }

    @Override
    public void search(String searchKey, Integer currentPage, Model model) {
        Order order = new Order();
        order.setSearchKey(searchKey);
        Page page = new Page();
        page.setCurrentPage(currentPage);
        //总是忘记！！！！
        order.setPage(page);
        model.addAttribute(FinalName.RESUKT_LIST, getOrderListByPage(order, false));
        model.addAttribute(FinalName.SEARCH_PARAM, order);
        model.addAttribute(FinalName.SEARCH_KEY, searchKey);
    }

    /**
     * 生成订单
     * @param orderBuyDto 前端传过来的对象
     * @param userId 用户id
     * @return 订单是否生成成功
     */
    private boolean saveOrder(OrderBuyDto orderBuyDto, Integer userId) {
        Order order = new Order();
        order.setBusinessId(orderBuyDto.getId());
        order.setUserId(userId);
        order.setNum(orderBuyDto.getNum());
        order.setPrice(orderBuyDto.getPrice());
        //生成订单的时候，默认未评论
        order.setCommentState(CommentStateConstant.NOT_COMMENT);
        int affectedRow = orderDao.insertOrder(order);
        if (affectedRow != 1){
            return false;
        }
        /*
         * 定时任务：
         *     理论上生成订单之后，应该增加商户表中的已售数量，但是对于已售数量而言，并不需要实时数据，每隔一段时间统计
         * 一次即可。（若实时统计，明显增大了数据库的压力，并没有显著的用户体验感提升）
         */
        return true;
    }

    /**
     * 根据手机号，分页对象查询订单列表
     * @param phone 手机号
     * @param page 分页对象
     * @return 订单列表
     * 备注：单独把这个抽象出来，就是方便后台管理系统和前台API一起用
     */
    private List<OrderDto> getOrderListByPhone(String phone, Page page, boolean isForApi) {
        //校验手机号是否符合规范
        if (VerifyUtil.verifyPhoneNumber(phone)){
            User user = userDao.selectUserByPhone(Long.parseLong(phone));
            if (user != null){
                Order order = new Order();
                order.setUserId(user.getId());
                order.setPage(page);
                return getOrderListByPage(order, isForApi);
            } else {
                LOGGER.error("该用户暂未注册：" + phone);
            }
        } else {
            LOGGER.error("手机号码错误，不符合手机号码规范！");
        }
        return null;
    }

    /**
     * 获取订单列表
     * @param order 待查询对象
     * @return 订单列表
     */
    private List<OrderDto> getOrderListByPage(Order order, boolean isForApi) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<Order> orderList = orderDao.searchOrderListByPage(order);
        if (CollectionUtils.isNotEmpty(orderList)){
            orderDtoList = wrapOrderList(orderList, isForApi);
        } else {
            LOGGER.info("该用户暂无订单！");
        }
        return orderDtoList;
    }

    private List<OrderDto> wrapOrderList(List<Order> orderList, boolean isForApi) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order : orderList){
            orderDtoList.add(wrapOrder(order, isForApi));
        }
        return orderDtoList;
    }

    /**
     * 包装返回给前台的数据
     * @param order 待包装订单对象
     * @param isForApi true：返回给API  false：返回给JSP
     * @return 包装后的订单对象
     */
    private OrderDto wrapOrder(Order order, boolean isForApi) {
        OrderDto orderDto = new OrderDto();
        orderDto.setTitle(order.getBusiness().getTitle());
        orderDto.setCount(order.getNum());
        orderDto.setPrice(order.getPrice());
        orderDto.setId(Long.valueOf(order.getId()));
        if (isForApi){
            //为API准备的返回数据
            orderDto.setCommentState(order.getCommentState());
            orderDto.setImg(imgUrl + order.getBusiness().getImgFileName());
        } else {
            //为JSP准备的返回数据
            orderDto.setPhone(order.getUser().getPhone());
        }

        return orderDto;
    }

    /**
     * 包装订单列表
     */
    private OrderListDto wrapOrderListDto(List<OrderDto> orderDtoList, Page page) {
        if (CollectionUtils.isNotEmpty(orderDtoList)){
            OrderListDto orderListDto = new OrderListDto();
            orderListDto.setOrderDtoList(orderDtoList);
            if (page.getCurrentPage() < page.getTotalPage()){
                orderListDto.setHasMore(true);
            } else {
                orderListDto.setHasMore(false);
            }
            return orderListDto;
        } else {
            LOGGER.info("该用户暂无订单,无法包装！");
        }
        return null;
    }
}
