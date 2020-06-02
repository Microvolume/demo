package com.example.demo.controller;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.exception.CustomizeException;
import com.example.demo.service.QuestionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    //这个地方是自己卡住地方最多的地方，为什么要把id转成Long类型的，就变好了？
    Long questionId = null;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id,
                           @NotNull Model model) {
        try {
            questionId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
        }
        //累加问题阅读数
        questionService.incView(questionId);
        //拿到问题和创建者的组合信息对象
        QuestionDTO questionDTO = questionService.getById(questionId);
        model.addAttribute("question", questionDTO);
        return "question";
    }
}
