### 有人想要Android面向切面编程,今天他来了!😜,轻松完成各种骚操作!登录状态拦截,日志拦截,权限拦截,轻松搞定!

### 更新日志：
###### v1.02  2019-10.17
* 第一次发布
#### 快速对指定函数进行切面拦截：
 - 注解完全自定义
 - 拦截规则自定义
 - 无需手动编写切面代码,APT自动生成切面文件
 - 支持组件化
 

## Installation：
1.project.gradle 添加(同步完成后再进行下一步!!!)
```java
    buildscript {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.2.1'
        classpath 'org.aspectj:aspectjtools:1.8.9'
        classpath 'org.aspectj:aspectjweaver:1.8.9'
    }
}
```
2.app.gradle 添加
```java
dependencies {
     implementation 'com.github.TanZhiL:OkAspectjAnnotation:1.0.4'
     annotationProcessor 'com.github.TanZhiL:OkAspectjCompiler:1.0.4'
}
/*******************独立运行时**********************************/
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

final def log = project.logger
final def variants = project.android.applicationVariants

variants.all { variant ->
    if (!variant.buildType.isDebuggable()) {
        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
        return;
    }

    JavaCompile javaCompile = variant.javaCompile
    javaCompile.doLast {
        String[] args = ["-showWeaveInfo",
                         "-1.8",
                         "-inpath", javaCompile.destinationDir.toString(),
                         "-aspectpath", javaCompile.classpath.asPath,
                         "-d", javaCompile.destinationDir.toString(),
                         "-classpath", javaCompile.classpath.asPath,
                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true);
        new Main().run(args, handler);
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break;
                case IMessage.WARNING:
                    log.warn message.message, message.thrown
                    break;
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break;
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break;
            }
        }
    }
}
/***********************END****************************/
/*******************作为组件时**********************************/
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

android.libraryVariants.all { variant ->
    LibraryPlugin plugin = project.plugins.getPlugin(LibraryPlugin)
    JavaCompile javaCompile = variant.javaCompile
    javaCompile.doLast {
        String[] args = ["-showWeaveInfo",
                         "-1.5",
                         "-inpath", javaCompile.destinationDir.toString(),
                         "-aspectpath", javaCompile.classpath.asPath,
                         "-d", javaCompile.destinationDir.toString(),
                         "-classpath", javaCompile.classpath.asPath,
                         "-bootclasspath", plugin.project.android.bootClasspath.join(
                File.pathSeparator)]

        MessageHandler handler = new MessageHandler(true);
        new Main().run(args, handler)

        def log = project.logger
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break;
                case IMessage.WARNING:
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break;
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break;
            }
        }
    }
}

/***********************END****************************/
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
        OkAspectjHelper.setmHandler(new PointHandler() {
            @Override
            public void handlePoint(Class clazz, ProceedingJoinPoint joinPoint) {
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
4.配置完成,可以在handlePoint(Class clazz, ProceedingJoinPoint joinPoint)中自由发挥你的骚操作了!


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
