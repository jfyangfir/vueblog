package com.yjf.sys.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjf.sys.common.Result;
import com.yjf.sys.entity.MyBlog;
import com.yjf.sys.service.MyBlogService;
import com.yjf.util.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class BlogController {

    @Autowired
    MyBlogService myBlogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1")Integer currentPage){

        Page page=new Page(currentPage,5);
        IPage pageData=myBlogService.page(page,new QueryWrapper<MyBlog>().orderByDesc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id")Long id){

        MyBlog myBlog=myBlogService.getById(id);
        Assert.notNull(myBlog,"该博客已经被删除");
        return Result.succ(null);
    }

    @RequiresAuthentication
    @GetMapping("/blog/edit")
    public Result edit(@Validated @RequestBody MyBlog blog){

        MyBlog temp=null;
        if(blog.getId()!=null){
            temp=myBlogService.getById(blog.getId());
            //只能编辑自己的文章
            Assert.isTrue(temp.getUserId().longValue()== ShiroUtil.getProfile().getId().longValue(),"没有权限编辑");
        }else {

            temp=new MyBlog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }

        BeanUtil.copyProperties(blog,temp,"id","userId","created","status");
        myBlogService.saveOrUpdate(temp);
        return Result.succ(null);
    }
}
