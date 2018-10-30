package com.disfile.cn.impl;

import com.disfile.cn.annotation.MyAutoried;
import com.disfile.cn.annotation.MyService;

/**
 * 测试 自定义注入
 */
@MyService
public class UserServiceImpl {

    @MyAutoried
    private OrderServiceImpl orderServiceImpl;

    public void add(){
        System.out.println("userServiceImpl add user" );
        //如属性orderService 已注入 则不会返回空指针异常
        orderServiceImpl.order();
    }
}
