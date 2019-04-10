package com.lideshuai.service;

import com.lideshuai.entiey.User;
import com.lideshuai.mapper1.UserMapper1;
import com.lideshuai.mapper2.UserMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserMapper1 userMapper1;

    @Autowired
    private UserMapper2 userMapper2;

    @Transactional
    public void addUser(User user)throws Exception{
        userMapper1.addUser(user.getName(),user.getAge());
        userMapper2.addUser(user.getName(),user.getAge());
    }

}
