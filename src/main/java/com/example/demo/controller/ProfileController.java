package com.example.demo.controller;
import com.example.demo.dto.PaginationDTO;
import com.example.demo.model.User;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/profile/{action}")
    //该路径方法首先会被拦截器程序拦截，执行完拦截器中程序之后，再继续向下执行，目的是用拦截器进行统一处理
    public String profile(HttpServletRequest request,
                          //@parthVariable中的参数为name跟value的区别是啥？
                          @PathVariable(name = "action") String action, Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "3") Integer size) {
        //从拦截器类中获取user
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            //访问我的提问页面
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        }else if ("replies".equals(action)) {
            //访问最新回复页面
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
    //根据分页进行查询跳转
    PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination", paginationDTO);
        return "profile";
    }
}
