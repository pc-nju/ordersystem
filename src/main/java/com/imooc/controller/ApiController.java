package com.imooc.controller;

import com.imooc.dto.*;
import com.imooc.service.*;
import com.imooc.task.BusinessTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/5/25 18:56
 *"@RestController"注解，相当于“@Controller”与“@ResponseBody”注解的结合，这样下面接口就不用带“@ResponseBody”注解了
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommentService commentService;

    /**
     * 首页
     */
    @RequestMapping(value = "/homead", method = RequestMethod.GET)
    public List<AdvertisementDto> homead(@RequestParam(value = "title", required = false) String title,
                                         @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage) throws IOException {
        return advertisementService.searchByPage(title, currentPage, null);
    }

    /**
     * 推荐列表
     */
    @RequestMapping(value = "/homelist/{city}/{page.currentPage}", method = RequestMethod.GET)
    public BusinessListDto homelist(BusinessDto businessDto){
        //按创建时间
        return businessService.searchHomeListForApi(businessDto);
    }
    /**
     * 关键词搜索
     */
    @RequestMapping(value = "/search/{page.currentPage}/{city}/{category}/{searchKey}", method = RequestMethod.GET)
    public BusinessListDto searchByKeyWord(BusinessDto businessDto){
        return businessService.searchBusinessForApi(businessDto);
    }
    /**
     * 不带关键词搜索
     */
    @RequestMapping(value = "/search/{page.currentPage}/{city}/{category}", method = RequestMethod.GET)
    public BusinessListDto search(BusinessDto businessDto){
        return businessService.searchBusinessForApi(businessDto);
    }
    /**
     * 商户信息接口
     */
    @RequestMapping(value = "/detail/info/{id}", method = RequestMethod.GET)
    public BusinessDto getBusinessDetail(BusinessDto businessDto){
        return businessService.searchBusinessDetailForApi(businessDto);
    }

    /**
     * 发送短信验证码
     */
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public ApiCodeDto sms(@RequestParam("username") Long phone){
        //username是和前端妥协的结果，在后端对应的应该是phone
        return userService.sendSmsVerificationCode(phone);
    }
    /**
     * 会员登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiCodeDto login(@RequestParam("username") Long phone, @RequestParam("code") String verificationCode){
        //username是和前端妥协的结果，在后端对应的应该是phone
        return userService.login(phone, verificationCode);
    }

    /**
     * 买单
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ApiCodeDto order(OrderBuyDto orderBuyDto){
        return orderService.order(orderBuyDto);
    }
    /**
     * 订单列表（理想接口）
     */
    @RequestMapping(value = "/test/orderlist/{username}", method = RequestMethod.GET)
    public OrderListDto getOrderListByTest(@PathVariable("username") String phone){
        return orderService.getOrderListByPhoneForApiByTest(phone);
    }

    /**
     * 订单列表（真实接口，前端就这样没办法）
     */
    @RequestMapping(value = "/orderlist/{username}", method = RequestMethod.GET)
    public List<OrderDto> getOrderList(@PathVariable("username") String phone){
        return orderService.getOrderListByPhoneForApi(phone);
    }
    /**
     * 评论
     */
    @RequestMapping(value = "/submitComment", method = RequestMethod.POST)
    public ApiCodeDto submitComment(CommentOrderDto commentOrderDto){
        return commentService.submitComment(commentOrderDto);
    }
    /**
     * 商户的评论列表
     */
    @RequestMapping(value = "/detail/comment/{currentPage}/{businessId}", method = RequestMethod.GET)
    public CommentListDto getCommentList(@PathVariable("currentPage") Integer currentPage,
                                    @PathVariable("businessId") Integer businessId){
        return commentService.getCommentListForApi(currentPage, businessId);
    }
}
