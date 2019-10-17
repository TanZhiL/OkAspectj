package com.gykj.thomas.aspectj;

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
   void handlePoint(ProceedingJoinPoint joinPoint);
}
