package com.disfile.cn;

import com.disfile.cn.impl.UserServiceImpl;

public class Main {

    /******扫包范围****/
    private static String packageName = "com.disfile.cn";

    public static void main(String[] args) {

        MyClassPathResources  myClassPathResources = new MyClassPathResources(packageName);

        UserServiceImpl userService = (UserServiceImpl)myClassPathResources.getMyBean("userServiceImpl");

        userService.add();

    }


}
