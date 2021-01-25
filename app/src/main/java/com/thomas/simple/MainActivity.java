package com.thomas.simple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.thomas.okaspectj.OkAspectjHelper;
import com.thomas.okaspectj.IPointHandler;

import org.aspectj.lang.ProceedingJoinPoint;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static {
        OkAspectjHelper.init(new IPointHandler() {
            @Override
            public void onHandlePoint(Class clazz, ProceedingJoinPoint joinPoint) {
                Log.d(TAG, "handlePoint() called with: clazz = [" + clazz + "], joinPoint = [" + joinPoint + "]");
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
