﻿### 有人想要Android面向切面编程,今天他来了!😜,轻松完成各种骚操作!登录状态拦截,日志拦截,权限拦截,轻松搞定!

[![](https://jitpack.io/v/TanZhiL/OkAspectj.svg)](https://jitpack.io/#TanZhiL/OkAspectj)

### 更新日志：
###### v1.02  2019-10.17
* 第一次发布
#### 快速对指定函数进行切面拦截：
 - 注解完全自定义
 - 拦截规则自定义
 - 无需手动编写切面代码,APT自动生成切面文件
 - 支持组件化


## Installation：[![](https://jitpack.io/v/TanZhiL/OkAspectj.svg)](https://jitpack.io/#TanZhiL/OkAspectj)
1.project.gradle 
```java
    buildscript {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.2.1'
      
        classpath 'org.aspectj:aspectjtools:1.9.6'
        classpath 'com.github.TanZhiL.OkAspectj:okaspectj-plugin:+'
    }
}
```
2.app.gradle 添加(注意每个需要生成切面的文件的组件都需要添加annotationProcessor)
```java

apply plugin: 'com.xtc.thomas.okaspectj'

dependencies {
     implementation 'org.aspectj:aspectjrt:1.9.6'
     implementation 'com.github.TanZhiL.OkAspectj:okaspectj:+'
     annotationProcessor 'com.github.TanZhiL.OkAspectj:okaspectj-compiler:+'
}
```
## Usage：
1. 在自己想要拦截的注解之上添加 @OkAspectj注解
```java
@OkAspectj
@Target(ElementType.METHOD)
public @interface NeedLogin {
    int value()default 0;
}

```

2. 在想要拦截的方法加入自己的注解

```java
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
        test1();
    }
    @NeedLogin(2)
    private void test() {

    }
    @TestAnnotaion
    private void test1() {

    }
}

```
3.在Application设置全局切面拦截处理

```java
public class App extends Application {
    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        OkAspectjHelper.init(new PointHandler() {
            @Override
            public void onHandlePoint(Class clazz, ProceedingJoinPoint joinPoint) {
                     Log.d(TAG, "handlePoint() called with: clazz = [" + clazz + "]");
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
```
4.配置完成,可以在onHandlePoint(Class clazz, ProceedingJoinPoint joinPoint)中自由发挥你的骚操作了!
5.也可自己编写切面文件,然后通过调用OkAspectjHelper.notifyHandler(Class clazz,ProceedingJoinPoint joinPoint),发送切点信息进行统一处理.

6.拦截系统方法请参考以下案例.

## 经典案例:

1.点击事件

```java
@OkAspectj("execution(* android.view.View.OnClickListener.onClick(..))")
@Target(ElementType.METHOD)
public @interface TestClick {
}
```

2.生命周期

```java
@OkAspectj("execution(* android.app.Activity.on**(..))")
@Target(ElementType.METHOD)
public @interface TestLifeCycle {
}
```

### 配置出错的请参考 高仿喜马拉雅听Android客户端 https://github.com/TanZhiL/Zhumulangma

### 致谢
* 感谢所有开源库的大佬
* 借鉴大佬 https://github.com/JakeWharton/butterknife
### 问题反馈
欢迎加星，打call https://github.com/TanZhiL/OkAspectj
* email：1071931588@qq.com
### 关于作者
谭志龙
### 开源项目
* 快速切面编程开源库 https://github.com/TanZhiL/OkAspectj
* 高仿喜马拉雅听Android客户端 https://github.com/TanZhiL/Zhumulangma
* 骨架屏弹性块 https://github.com/TanZhiL/SkeletonBlock
* RxPersistence是基于面向对象设计的快速持久化框架 https://github.com/TanZhiL/RxPersistence
### License
```
Copyright (C)  tanzhilong OkAspectjFramework Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
