package com.imooc.controller;

import com.imooc.constant.PageCodeEnum;
import com.imooc.dto.AdvertisementDto;
import com.imooc.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 潘畅
 * @date 2018/5/19 19:20
 */
@Controller
@RequestMapping(value = "/ad")
public class AdvertisementController {
    @Autowired
    private AdvertisementService advertisementService;
    @RequestMapping
    public String init(Model model){
        advertisementService.searchByPage(null, 1, model);
        return "/content/adList";
    }
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                         Model model){
        advertisementService.searchByPage(title, currentPage, model);
        return "/content/adList";
    }

    @RequestMapping(value = "/addInit", method = RequestMethod.GET)
    public String addInit(){
        return "/content/adAdd";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(AdvertisementDto advertisementDto, Model model){
        if (advertisementService.addAdvertisement(advertisementDto)){
            //调用返回值枚举类
            model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.ADD_SUCCESS);
        } else {
            //调用返回值枚举类
            model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.ADD_FAILURE);
        }
        return "/content/adAdd";
    }
    @RequestMapping(value = "/{id}/deletion", method = RequestMethod.POST)
    public String deleteAdvertisement(@PathVariable("id") Integer id, Model model){
        if (advertisementService.deleteAdvertisement(id)){
            model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.DELETE_SUCCESS);
        } else {
            model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.DELETE_FAILURE);
        }
        return "forward:/ad";
    }
    @RequestMapping(value = "/{id}/modifyInit", method = RequestMethod.POST)
    public String modifyAdvertisementInit(@PathVariable("id") Integer id, Model model){
        AdvertisementDto advertisementDto = advertisementService.modifyInit(id);
        model.addAttribute("modifyObj", advertisementDto);
        return "/content/adModify";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modifyAdvertisement(AdvertisementDto advertisementDto, Model model){
        if (advertisementService.modifyAdvertisement(advertisementDto)){
            model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.UPDATE_SUCCESS);
        } else {
            model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.UPDATE_FAILURE);
        }
        return "forward:/ad";
    }
}
