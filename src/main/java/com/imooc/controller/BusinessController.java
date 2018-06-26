package com.imooc.controller;

import com.imooc.bean.Dictionary;
import com.imooc.constant.DictionaryConstant;
import com.imooc.constant.FinalName;
import com.imooc.constant.PageCodeEnum;
import com.imooc.dto.BusinessDto;
import com.imooc.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 潘畅
 * @date 2018/5/31 20:51
 */
@Controller
@RequestMapping("/businesses")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    /**
     * 商户列表页面初始化
     */
    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model){
        model.addAttribute(FinalName.RESUKT_LIST, businessService.searchBusinessByPage(model));
        return "/content/businessList";
    }

    /**
     * 商户列表页面搜索
     */
    @RequestMapping(value = "/search")
    public String searchBusiness(@RequestParam(value = "searchKey", required = false) String searchKey,
                                 @RequestParam(value = "city", required = false) String city,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                 Model model){
        businessService.searchBusinessByPage(searchKey, city, category, currentPage, model);
        return "/content/businessList";
    }

    /**
     * 商户增加页面初始化
     */
    @RequestMapping("/addInit")
    public String initAddPage(Model model){
        List<Dictionary> cityList = businessService.getDictionariesByType(DictionaryConstant.TYPE_DICTIONARY_CITY);
        List<Dictionary> categoryList = businessService.getDictionariesByType(DictionaryConstant.TYPE_DICTIONARY_CATEGORY);
        model.addAttribute("cityList", cityList);
        model.addAttribute("categoryList", categoryList);
        return "/content/businessAdd";
    }

    /**
     * 商户增加
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.POST)
    public String addPage(BusinessDto businessDto, Model model){
        if (businessService.addBusiness(businessDto)){
            model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.ADD_SUCCESS);
        } else {
            model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.ADD_FAILURE);
        }
        return "forward:/businesses/addInit";
    }

    /**
     * 商户信息修改页面初始化
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String modifyBusiness(@PathVariable("id") Integer id, Model model){
        model.addAttribute(FinalName.MODIFY_OBJECT, businessService.searchBusinessById(id));
        model.addAttribute("cityList", businessService.getDictionariesByType(DictionaryConstant.TYPE_DICTIONARY_CITY));
        model.addAttribute("categoryList", businessService.getDictionariesByType(DictionaryConstant.TYPE_DICTIONARY_CATEGORY));
        return "/content/businessModify";
    }

    /**
     * 商户信息修改
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String modifyBusiness(@PathVariable("id") Integer id, BusinessDto businessDto){
        businessDto.setId(id);
        businessService.modifyBusiness(businessDto);
//        return "redirect:/businesses/id";
        return "redirect:/businesses";
    }

    /**
     * 商户删除
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String removeBusiness(@PathVariable("id") Integer id){
        businessService.deleteBusiness(id);
        //redirect不带参数
        return "redirect:/businesses";
    }
}
