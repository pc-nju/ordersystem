package com.imooc.service.impl;

import com.imooc.bean.Business;
import com.imooc.bean.Comment;
import com.imooc.bean.Order;
import com.imooc.bean.User;
import com.imooc.constant.ApiCodeEnum;
import com.imooc.constant.CommentStateConstant;
import com.imooc.constant.FinalName;
import com.imooc.constant.PageCodeEnum;
import com.imooc.dao.BusinessDao;
import com.imooc.dao.CommentDao;
import com.imooc.dao.OrderDao;
import com.imooc.dao.UserDao;
import com.imooc.dto.ApiCodeDto;
import com.imooc.dto.CommentDto;
import com.imooc.dto.CommentListDto;
import com.imooc.dto.CommentOrderDto;
import com.imooc.entity.Page;
import com.imooc.service.CommentService;
import com.imooc.util.VerifyUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/6/12 7:47
 */
@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private BusinessDao businessDao;
    @Value("${comment.pageSize}")
    private Integer commentPageSize;
    //“@Transactional”：一旦执行中发现任何异常，整个方法都需要回滚

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ApiCodeDto submitComment(CommentOrderDto commentOrderDto) {
        if (StringUtils.isBlank(commentOrderDto.getUsername()) || commentOrderDto.getId() == null){
            LOGGER.warn("有人在攻击！！");
            return new ApiCodeDto(ApiCodeEnum.PARAM_NULL);
        }
        if (StringUtils.isBlank(commentOrderDto.getToken())){
            return new ApiCodeDto(ApiCodeEnum.NOT_LOGIN);
        }
        if (!VerifyUtil.verifyPhoneNumber(commentOrderDto.getUsername())){
            LOGGER.warn("有人在攻击！！");
            return new ApiCodeDto(ApiCodeEnum.PHONE_ERROR);
        }
        User user = userDao.selectUserByPhone(Long.parseLong(commentOrderDto.getUsername()));
        if (user == null){
            LOGGER.warn("有人在攻击！！");
            return new ApiCodeDto(ApiCodeEnum.USER_NOT_EXISTS);
        }
        Order order = orderDao.selectOrderById(commentOrderDto.getId());
        if (order == null){
            LOGGER.warn("有人在攻击！！");
            return new ApiCodeDto(ApiCodeEnum.ORDER_NOT_EXISTS);
        }
        if (!user.getId().equals(order.getUserId())){
            LOGGER.warn("有人在攻击！！");
            return new ApiCodeDto(ApiCodeEnum.ORDER_NOT_YOURS);
        }
        Comment comment = new Comment();
        comment.setOrderId(order.getId());
        comment.setContent(commentOrderDto.getComment());
        comment.setStar(commentOrderDto.getStar());
        int affectedRow = commentDao.insertComment(comment);
        if (affectedRow != 1){
            return new ApiCodeDto(ApiCodeEnum.COMMENT_ERROR);
        }
        //更新订单的评论状态
        Order newOrder = new Order();
        newOrder.setId(order.getId());
        newOrder.setCommentState(CommentStateConstant.HAS_COMMENT);
        if (orderDao.updateOrder(newOrder) != 1){
            return new ApiCodeDto(ApiCodeEnum.UPDATE_ORDER_ERROR);
        }
        return new ApiCodeDto(ApiCodeEnum.SUCCESS);
    }

    @Override
    public CommentListDto getCommentListForApi(Integer currentPage, Integer businessId) {
        CommentListDto commentListDto = new CommentListDto();
        Business business = businessDao.selectBusinessesById(businessId);
        if (business != null){
            Comment comment = new Comment();
            Page page = new Page();
            /*
             *     前端是从0开始，第0页，第1页，若“currentPage + 1”不这样做，那么第0页和第1页的数据是一样的，
             * 因为Page会把0处理为1
             */
            page.setCurrentPage(currentPage + 1);
            page.setPageSize(commentPageSize);
            comment.setPage(page);
            //构建查询条件
            comment.setBusiness(business);
            List<CommentDto> commentDtoList = getCommentListByPage(comment, true);
            if (CollectionUtils.isNotEmpty(commentDtoList)){
                commentListDto.setData(commentDtoList);
                /*
                 * 注意：这里不能用currentPage,得用page.getCurrentPage()
                 * 原因：因为前端的页码是从0开始的，得进入我们的Page对象处理之后，才能从1开始
                 */
                if (page.getCurrentPage() < page.getTotalPage()){
                    commentListDto.setHasMore(true);
                }
            } else {
                LOGGER.info("businessId= " + businessId + " 的商户暂未有消费者评论！" );
            }
        } else {
            LOGGER.error("无businessId= " + businessId + " 的商户！" );
        }
        return commentListDto;
    }

    @Override
    public void search(String searchKey, Integer currentPage, Model model) {
        //TODO 校验searchKey(符合一定规则，不能太长)
        Comment comment = new Comment();
        comment.setSearchKey(searchKey);
        Page page = new Page();
        page.setCurrentPage(currentPage);
        comment.setPage(page);
        model.addAttribute(FinalName.RESUKT_LIST, getCommentListByPage(comment, false));
        model.addAttribute(FinalName.SEARCH_PARAM, comment);
        model.addAttribute(FinalName.SEARCH_KEY, searchKey);
    }

    @Override
    public PageCodeEnum deleteCommentWithId(Integer id) {
        Comment comment = new Comment();
        comment.setId(id);
        return deleteComment(comment);
    }



    /**
     * 获取评论列表（分页）
     * @param comment 待搜索条件
     * @param isForApi true：给API返回的数据；false：给JSP返回的数据
     * @return 评论列表
     */
    private List<CommentDto> getCommentListByPage(Comment comment, boolean isForApi){
        List<Comment> commentList = commentDao.searchByPage(comment);
        if (CollectionUtils.isNotEmpty(commentList)){
            return wrapCommentList(commentList, isForApi);
        }
        return null;
    }

    private List<CommentDto> wrapCommentList(List<Comment> commentList, boolean isForApi) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList){
            commentDtoList.add(wrapComment(comment, isForApi));
        }
        return commentDtoList;
    }

    private CommentDto wrapComment(Comment comment, boolean isForApi) {
        CommentDto commentDto = new CommentDto();
        if (isForApi){
            commentDto.setUsername(String.valueOf(comment.getOrder().getUser().getPhone()));
            //返回给前台的评论内容
            commentDto.setComment(comment.getContent());

        } else {
            //评论id
            commentDto.setId(comment.getId());
            commentDto.setPhone(comment.getOrder().getUser().getPhone());
            commentDto.setTitle(comment.getOrder().getBusiness().getTitle());

            commentDto.setOrderId(comment.getOrderId());
        }
        commentDto.setStar(comment.getStar());
        return commentDto;
    }

    /**
     * 删除评论
     * @param comment 待删除对象
     * @return 删除结果
     */
    private PageCodeEnum deleteComment(Comment comment) {
        int affectedRow = commentDao.deleteComment(comment);
        if (affectedRow == 1){
            LOGGER.info("成功删除id= " + comment.getId() +"的评论！");
            return PageCodeEnum.DELETE_SUCCESS;
        } else {
            LOGGER.error("删除id= " + comment.getId() +"的评论失败！");
            return PageCodeEnum.DELETE_FAILURE;
        }
    }

}
