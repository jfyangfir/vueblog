package com.yjf.sys.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yjf.sys.common.Result;
import com.yjf.sys.common.dto.LoginDto;
import com.yjf.sys.entity.MyUser;
import com.yjf.sys.service.MyUserService;
import com.yjf.util.JwtUtils;
import io.jsonwebtoken.Jwt;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Autowired
    MyUserService myUserService;

    @Autowired
    JwtUtils jwtUtils;

    //@Validated 数据校验
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){

        MyUser myUser=myUserService.getOne(new QueryWrapper<MyUser>().eq("username",loginDto.getUsername()));
        Assert.notNull(myUser,"用户不存在");

        if(myUser.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }
        String jwt= jwtUtils.generateToken(myUser.getId());

        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.succ(MapUtil.builder().put("id",myUser.getId())
                                            .put("username",myUser.getUsername())
                                            .put("avatar",myUser.getAvatar())
                                            .put("email",myUser.getEmail())
                                            .map());
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result loginout(){
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }
}
