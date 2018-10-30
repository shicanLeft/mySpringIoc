package com.disfile.cn;

import com.disfile.cn.annotation.MyAutoried;
import com.disfile.cn.annotation.MyService;
import com.disfile.cn.util.PkgUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MyClassPathResources {

    public MyClassPathResources(){}  //手动无参构造器

    private static ConcurrentMap<String,Object> beans = null;  //my 容器  -使用线程安全的ConcurrenctMap接口

    public  MyClassPathResources(String packageName){
        /**初始化bean*/
        initBeans(packageName);
        //3.遍历容器所有加载好的bean,判断属性是否有myAutoriod ,有-属性注入。
        filedsSet(beans);
    }

    private void initBeans(String packageName) {
        //1.扫描packageName下的所有类
        Set<Class<?>> classes =  PkgUtil.getClzFromPkg(packageName);

        //2.拿到类--判断是否有myService注解。 有-实例化-存到容器beans
        beans = new ConcurrentHashMap<String,Object>();
        try {
        for(Class c : classes){
            Annotation myService = c.getAnnotation(MyService.class);
            if(null != myService){
                Object obj = c.newInstance();
                String beanId = c.getSimpleName();
                beanId = toLowerCase(beanId);
                beans.put(beanId,obj) ;   //容器存值 -key:beanId(默认为类小写) -value:类对象
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void filedsSet(ConcurrentMap<String, Object> beans){
        if(beans.isEmpty())
            throw new RuntimeException("该包下沒有需要实例化的bean");
        try {
            for (Map.Entry<String, Object> entry : beans.entrySet()) {
                Object obj = entry.getValue();
                Class clazz = obj.getClass();
                Field[] fileds  =clazz.getDeclaredFields();
                for(Field f :fileds){
                    Annotation myAutoried =  f.getAnnotation(MyAutoried.class);
                    if(null != myAutoried){
                        String filedName = f.getName();
                        filedName = toLowerCase(filedName);
                        f.setAccessible(true); //私有属性 能被访问
                        f.set(obj,beans.get(filedName));
                    }
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }


    String toLowerCase(String beanId){
        if(StringUtils.isEmpty(beanId))
            return null;
        StringBuilder sb = new StringBuilder(beanId);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }


    /**从my容器中获取对象*/
    public Object getMyBean(String userServiceImpl) {
        if(StringUtils.isEmpty(userServiceImpl))
            throw new RuntimeException("实例化对象不能为空");
        Object obj = beans.get(userServiceImpl);
        if(null == obj)
            throw new RuntimeException("该"+userServiceImpl+"未被初始化");
        return obj;
    }
}
