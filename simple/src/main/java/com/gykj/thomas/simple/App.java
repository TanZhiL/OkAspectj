package com.gykj.thomas.simple;

import android.app.Application;
import android.util.Log;

import com.gykj.thomas.simple.aop.NeedLogin;
import com.gykj.thomas.aspectj.AspectjHelper;
import com.gykj.thomas.aspectj.PointHandler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Author: Thomas.<br/>
 * Date: 2019/10/17 16:46<br/>
 * GitHub: https://github.com/TanZhiL<br/>
 * CSDN: https://blog.csdn.net/weixin_42703445<br/>
 * Email: 1071931588@qq.com<br/>
 * Description:
 */
public class App extends Application {
    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        AspectjHelper.addPointHandler(new PointHandler() {
            @Override
            public void handlePoint(Class clazz, ProceedingJoinPoint joinPoint) {
                if(clazz==NeedLogin.class){
                    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
                    NeedLogin annotation = methodSignature.getMethod().getAnnotation(NeedLogin.class);
                    Log.d(TAG, "handlePoint() called with: joinPoint = [" + annotation.value() + "]");
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        });
    }
}
