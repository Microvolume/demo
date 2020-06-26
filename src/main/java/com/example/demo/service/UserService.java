package com.example.demo.service;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by codedrinker on 2019/5/23.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /* 在登陆的时候做一下校验，如果当前github已经登陆过，我们会在数据库中找到它，并把token更新，如果没有登陆过，
    数据库中没有记录，就创建 ,同时做退出登陆的一个功能 */
    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        //视频里原来的是：User dbUser = userMapper.findByAccountId(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            // 数据库中没有记录，就调用【insert方法！】
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //在数据库中找到它，就把token更新，调用【update方法！】
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            //因为头像有可能会变化，所以要改一改！
            updateUser.setAvatarUrl(user.getAvatarUrl());
            //Name也有可能会变化，因此也要改一改！
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            // 原来的是 userMapper.update(dbUser)！
            //这里的updateByExampleSelective，指的是更新局部的SQL语句，当字段不为空的时候，就更新，从userMapper.xml中知道的这一点
            //前面的是更新的内容，后面的是该问题的原内容
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }
}
