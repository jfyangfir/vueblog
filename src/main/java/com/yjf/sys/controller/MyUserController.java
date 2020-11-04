package com.yjf.sys.controller;


import com.yjf.sys.common.Result;
import com.yjf.sys.entity.MyUser;
import com.yjf.sys.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Eric
 * @since 2020-11-04
 */
@RestController
@RequestMapping("/sys/user")
public class MyUserController {

    @Autowired
    MyUserService myUserService;

    @RequestMapping("/index")
    public Object index(){
        MyUser user= myUserService.getById(1L);
        return Result.succ(user);
    }
}
