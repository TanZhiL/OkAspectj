package com.gykj.thomas.okaspectj;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test(){
        List list =new ArrayList();
        A a;
        for (int i = 0; i < 4; i++) {
            a=new A(i);
            list.add(a);
        }
        System.out.println(list);
    }

    class A{
        A(int a){
            this.a=a;
        }
        int a;

        @Override
        public String toString() {
            return "A{" +
                    "a=" + a +
                    '}';
        }
    }
}