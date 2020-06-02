package com.example.demo.model;

import lombok.Data;
@Data
public class Question {
    //问题编号
    private Long id;
    //问题标题
    private String title;
    //创建时间
    private Long gmtCreate;
    //修改时间
    private Long gmtModified;
    /*发布人（User）的id，此处需要通过question表的creator属性关联user表的ID属性，然后再通过user表的ID难道user的头像
    所以说要再建一个对象（QuestionDTO），在里面加上User对象*/
    private Long creator;
    //评论数量，初始化为0
    private Integer commentCount;
    //浏览数，初始化为0
    private Integer viewCount;
    //点赞数，初始化为0
    private Integer likeCount;
    //标签，只能选择，并且多个标签用“，”相隔存入存入数据库中。当从数据库中取出时需要用“，”切分取出各个标签，展示到页面
    private String tag;
    //问题描述（内容）
    private String description;
}
