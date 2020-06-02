package com.example.demo.controller;
import com.example.demo.dto.PaginationDTO;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    //首先，当在地址里输入localhost：8080时，会被"/"拦截，接下来做的是如下的：检验登陆状态，最后跳转到index页面
    //该路径方法首先会被拦截器程序拦截，执行完拦截器中程序之后，再继续向下执行，目的是用拦截器进行统一处理
    public String index( Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {
        /*list()是返回的是Question对象，不依赖user表的，所以如果这样做：List<QuestionDTO> questionList= questionMapper.list();
        的话就是错的，于是我们新建了一个QuestionService层，使用QuestionService的目的是在里面不但可以使用QuestionMapper，还可
        UserMapper，起到组装的作用*/
        PaginationDTO pagination = questionService.list(page,size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
