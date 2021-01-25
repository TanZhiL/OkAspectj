package com.thomas.aspectj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Thomas.<br/>
 * Date: 2019/10/14 11:41<br/>
 * Email: 1071931588@qq.com<br/>
 * Description:
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface OkAspectj {
  String value() default "";
}
