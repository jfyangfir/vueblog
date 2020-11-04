package com.yjf.sys.common;

import lombok.Data;

import java.io.Serializable;

@Data//主要作用是提高代码的简洁，使用这个注解可以省去代码中大量的get()、 set()、 toString()等方法
public class Result implements Serializable {

    private int code;//200是正常，非200表示异常
    private String msg;
    private Object data;

    public static Result succ(int code,String msg,Object data){
        Result r=new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(int code,String msg,Object data){
        Result r=new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg,Object data){
        return fail(400,msg,data);
    }

    public static Result fail(String msg){
        return fail(400,msg,null);
    }

    public static Result succ(Object data){
        return succ(200,"操作成功",data);
    }
}
