package com.example.demo.controller;

import com.example.demo.dto.CommentCreateDTO;
import com.example.demo.dto.ResultDTO;
import com.example.demo.enums.CommentTypeEnum;
import com.example.demo.model.Comment;
import com.example.demo.dto.CommentDTO;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.model.User;
import org.apache.commons.lang3.StringUtils;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
    /**
     * Created by codedrinker on 2019/5/30.
     */
    @Controller
    public class CommentController {

        @Autowired
        private CommentService commentService;

        //我们需要将服务器端的Java对象的时候转成前端的json，我们就需要@ResponseBody注解
        @ResponseBody
        @RequestMapping(value = "/comment", method = RequestMethod.POST)
        //当我们需要将前端的json格式数据转成服务器端的Java对象的时候，我们就需要@RequestBody注解
        public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                           HttpServletRequest request) {
            // 从session中获取user
            User user = (User) request.getSession().getAttribute("user");
            //user可能不存在，要做异常处理！
            if (user == null) {
                return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
            }
            //评论的Content可能不存在，要做异常处理！这两行用到了Apache Commons Lang的工具包，到Maven仓库中下
            if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
                return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
            }
            Comment comment = new Comment();
            comment.setParentId(commentCreateDTO.getParentId());
            comment.setContent(commentCreateDTO.getContent());
            comment.setType(commentCreateDTO.getType());
            comment.setGmtModified(System.currentTimeMillis());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setCommentator(user.getId());
            comment.setLikeCount(0L);
            // 这里用Service的原因，是因为我们除了需要commentMapper，还需要questionMapper
            commentService.insert(comment, user);
            return ResultDTO.okOf();
        }

        @ResponseBody
        @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
        public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
            List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
            return ResultDTO.okOf(commentDTOS);
        }
    }
