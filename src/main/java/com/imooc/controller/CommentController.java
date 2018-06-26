package com.imooc.controller;

import com.imooc.constant.PageCodeEnum;
import com.imooc.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 潘畅
 * @date 2018/6/11 11:07
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @RequestMapping
    public String init(Model model){
        commentService.search(null, 1, model);
        return "/content/commentList";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam(value = "searchKey", required = false) String searchKey,
                         @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                         Model model){
        commentService.search(searchKey, currentPage, model);
        return "/content/commentList";
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Integer id, Model model){
        model.addAttribute(PageCodeEnum.KEY, commentService.deleteCommentWithId(id));
        return "redirect:/comment";
    }
}
