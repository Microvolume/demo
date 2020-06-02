package com.example.demo.mapper;

import com.example.demo.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (id, title, gmt_create, gmt_modified, creator, comment_count, view_count, like_count, tag, description) values (#{id}, #{title}, #{gmtCreate}, #{gmtModified}, #{creator}, #{commentCount}, #{viewCount}, #{likeCount}, #{tag}, #{description})")
    void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByuserId(@Param(value = "userId")Long userId, @Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    @Select("select count(1) from question where creator=#{userId}")
    Integer countByUserId(@Param(value = "userId") Long userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id") Long id);

    @Update("update question set gmt_modified=#{gmtModified}, title=#{title}, description=#{description},tag=#{tag} where id =#{id}")
    void update(Question question);

    @Update("update question set view_count = view_count + #{viewCount} where id =#{id}")
    void incView(Question question);
}
