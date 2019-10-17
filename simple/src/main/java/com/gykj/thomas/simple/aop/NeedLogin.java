package com.gykj.thomas.simple.aop;

import com.gykj.thomas.aspectj.OkAspectj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Author: Thomas.<br/>
 * Date: 2019/10/17 13:52<br/>
 * GitHub: https://github.com/TanZhiL<br/>
 * CSDN: https://blog.csdn.net/weixin_42703445<br/>
 * Email: 1071931588@qq.com<br/>
 * Description:
 */
@OkAspectj
@Target(ElementType.METHOD)
public @interface NeedLogin {
    int value()default 0;
}
