package com.imooc.controller;

import com.imooc.bean.SalesStatistics;
import com.imooc.constant.DictionaryConstant;
import com.imooc.constant.FinalName;
import com.imooc.dto.OrderDto;
import com.imooc.service.OrderService;
import com.imooc.service.SalesStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @author 潘畅
 * @date 2018/6/11 11:07
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping
    public String init(Model model){
        orderService.getOrderList(model);
        return "/content/orderList";
    }
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam(value = "searchKey", required = false) String searchKey,
                         @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                         Model model){
        orderService.search(searchKey, currentPage, model);
        return "/content/orderList";
    }
}
