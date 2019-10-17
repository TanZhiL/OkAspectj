package com.gykj.thomas.okaspectj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.gykj.thomas.aspectj.AspectjHelper;
import com.gykj.thomas.aspectj.PointHandler;
import com.gykj.thomas.okaspectj.aop.NeedLogin;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class MainActivity extends AppCompatActivity implements PointHandler {

    public TextView tvHello;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AspectjHelper.addPointHandler(this);
        test();
    }
    @NeedLogin(2)
    private void test() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AspectjHelper.removePointHandler(this);
    }

    @Override
    public void handlePoint(Class clazz,ProceedingJoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        NeedLogin annotation = methodSignature.getMethod().getAnnotation(NeedLogin.class);
        Log.d(TAG, "handlePoint() called with: joinPoint = [" + annotation.value() + "]");
    }
}
