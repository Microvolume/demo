package com.example.demo.service;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 当数据库中有该用户时更新用户信息，当数据库中没有该用户时创建新用户。
     * @param user
     */
    public void createOrUpdate(User user) {
        //数据库中存储的旧账户数据
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser==null) {
            // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            //将github返回的最新数据更新到数据库中。
            userMapper.update(dbUser);
        }
    }
}
