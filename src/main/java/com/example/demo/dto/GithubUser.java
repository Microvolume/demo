package com.example.demo.dto;

import lombok.Data;
@Data
public class GithubUser {
    private String name;
    private Long id;
    //简介
    private String bio;
    //github用户的头像
    private String avatarUrl;
}
