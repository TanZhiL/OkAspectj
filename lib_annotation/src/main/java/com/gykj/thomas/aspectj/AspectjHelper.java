package com.gykj.thomas.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author: Thomas.<br/>
 * Date: 2019/10/15 14:57<br/>
 * Email: 1071931588@qq.com<br/>
 * Description:登陆跳转帮助类
 */
public class AspectjHelper {

    private static List<PointHandler> mHandlers;

    public  static void addPointHandler(PointHandler handler){
        if(null == mHandlers){
            mHandlers=new CopyOnWriteArrayList<>();
        }
        if(mHandlers.contains(handler))
            return;
        mHandlers.add(handler);
    }
    public  static void removePointHandler(PointHandler handler){
        if(null == mHandlers){
            return;
        }
        mHandlers.remove(handler);
    }

    public static void notifyHandler(ProceedingJoinPoint joinPoint){
        for (PointHandler handler:mHandlers) {
            handler.handlePoint(joinPoint);
        }
    }
}
