package com.example.demo.mapper;
import com.example.demo.model.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}
