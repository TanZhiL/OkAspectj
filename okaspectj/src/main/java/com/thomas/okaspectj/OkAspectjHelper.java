package com.thomas.okaspectj;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Author: Thomas.<br/>
 * Date: 2019/10/15 14:57<br/>
 * Email: 1071931588@qq.com<br/>
 * Description:登陆跳转帮助类
 */
public class OkAspectjHelper {

    private volatile static IPointHandler mHandler;

    public synchronized static void init(IPointHandler mHandler) {
        if(null==mHandler){
            throw new IllegalArgumentException("mHandler can not be null");
        }
        OkAspectjHelper.mHandler = mHandler;
    }


    public synchronized static void notifyHandler(Class clazz,ProceedingJoinPoint joinPoint){
            mHandler.onHandlePoint(clazz,joinPoint);
    }
}
