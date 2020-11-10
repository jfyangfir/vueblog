package com.yjf.sys.controller;


import com.yjf.sys.common.Result;
import com.yjf.sys.entity.MyUser;
import com.yjf.sys.service.MyUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequiresAuthentication //需要登录认证才能访问
    @RequestMapping("/index")
    public Object index(){
        MyUser user= myUserService.getById(1L);
        return Result.succ(user);
    }

    @PostMapping("/save")
    public Result save(@PathVariable @RequestBody MyUser myUser){
        MyUser user= myUserService.getById(1L);
        return Result.succ(user);
    }
}
