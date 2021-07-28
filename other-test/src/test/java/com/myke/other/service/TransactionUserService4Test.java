package com.myke.other.service;

import com.myke.other.OtherApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionUserService4Test extends OtherApplicationTest {

    @Autowired
    private TransactionUserService4 userService4;


    @Test(expected = RuntimeException.class)
    public void test1() {
        userService4.test1();
    }

    @Test(expected = RuntimeException.class)
    public void test2() {
        userService4.test2();
    }

    @Test(expected = RuntimeException.class)
    public void test3() {
        userService4.test3();
    }

    @Test(expected = RuntimeException.class)
    public void test4() {
        userService4.test4();
    }

    @Test(expected = RuntimeException.class)
    public void test5() {
        userService4.test5();
    }

    @Test(expected = RuntimeException.class)
    public void test6() {
        userService4.test6();
    }

    @Test(expected = RuntimeException.class)
    public void test7() {
        userService4.test7();
    }

    @Test(expected = RuntimeException.class)
    public void test8() {
        userService4.test8();
    }

    @Test(expected = RuntimeException.class)
    public void test9() {
        userService4.test9();
    }

    @Test(expected = RuntimeException.class)
    public void test10() {
        userService4.test10();
    }

    @Test
    public void test11() {
        userService4.test11();
    }

    @Test(expected = RuntimeException.class)
    public void test12() {
        userService4.test12();
    }

    @Test(expected = RuntimeException.class)
    public void test13() {
        userService4.test13();
    }

    @Test(expected = RuntimeException.class)
    public void test14() {
        userService4.test14();
    }

    @Test(expected = RuntimeException.class)
    public void test15() {
        userService4.test15();
    }

    @Test
    public void test16() {
        userService4.test16();
    }



}