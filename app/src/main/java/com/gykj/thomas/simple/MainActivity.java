package com.gykj.thomas.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.gykj.thomas.aspectj.OkAspectjHelper;
import com.gykj.thomas.aspectj.PointHandler;
import com.gykj.thomas.simple.aop.NeedLogin;
import com.gykj.thomas.simple.aop.TestAnnotaion;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class MainActivity extends AppCompatActivity  {

    public TextView tvHello;
    private static final String TAG = "MainActivity";

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
