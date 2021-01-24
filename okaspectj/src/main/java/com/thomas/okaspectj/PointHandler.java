package com.thomas.okaspectj;


import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Author: Thomas.<br/>
 * Date: 2019/10/17 11:01<br/>
 * GitHub: https://github.com/TanZhiL<br/>
 * CSDN: https://blog.csdn.net/weixin_42703445<br/>
 * Email: 1071931588@qq.com<br/>
 * Description:
 */
public interface PointHandler {
   /**
    * 当进入切面时回调
    * @param clazz 当前触发的注解
    * @param joinPoint 切点,joinPoint.proceed();放过
    */
   void onHandlePoint(Class clazz, ProceedingJoinPoint joinPoint);
}
