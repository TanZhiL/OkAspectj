package com.thomas.simple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.thomas.okaspectj.OkAspectjHelper;
import com.thomas.okaspectj.PointHandler;

import org.aspectj.lang.ProceedingJoinPoint;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkAspectjHelper.init(new PointHandler() {
            @Override
            public void handlePoint(Class clazz, ProceedingJoinPoint joinPoint) {
                Log.d(TAG, "handlePoint() called with: clazz = [" + clazz + "], joinPoint = [" + joinPoint + "]");
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }
    @Test
    public  void test(){

    }
}
