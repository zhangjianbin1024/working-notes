package com.myke.other;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ztkj-hzb
 * @Date 2019/7/18 10:55
 * @Description
 */
public class Test {

    @org.junit.Test
    public void testAll() {
        // 取差集
        removeAllTest();

        //两个集合去交集
        //retianAllTest();

        //测试并集
        //addAllTest();
    }

    /**
     * 两个集合去交集
     */
    public static void retianAllTest() {
        List<Integer> list1 = new ArrayList<Integer>() {{
            this.add(1);
            this.add(2);
            this.add(3);
        }};

        List<Integer> list2 = new ArrayList<Integer>() {{
            this.add(2);
            this.add(3);
            this.add(4);
        }};

        //取交集
        list1.retainAll(list2);

        list1.forEach(System.out::println);
        System.out.println("-----------------------");
        list2.forEach(System.out::println);
    }


    /**
     * 测试并集  注意区分 List的addAll是不去重的。  而Set的addAll会去重
     */
    public static void addAllTest() {
        List<Integer> list1 = new ArrayList<Integer>() {{
            this.add(1);
            this.add(2);
            this.add(3);
        }};

        List<Integer> list2 = new ArrayList<Integer>() {{
            this.add(2);
            this.add(3);
            this.add(4);
        }};

        //取并集
        list1.addAll(list2);

        list1.forEach(System.out::println);
        System.out.println("-----------------------");
        list2.forEach(System.out::println);

        System.out.println("**********************************");


        Set<Integer> list3 = new HashSet<Integer>() {{
            this.add(1);
            this.add(2);
            this.add(3);
        }};

        Set<Integer> list4 = new HashSet<Integer>() {{
            this.add(2);
            this.add(3);
            this.add(4);
        }};

        //取并集
        list3.addAll(list4);

        list3.forEach(System.out::println);
        System.out.println("-----------------------");
        list4.forEach(System.out::println);
    }

    /**
     * 取差集
     */
    public static void removeAllTest() {
        List<Integer> list1 = new ArrayList<Integer>() {{
            this.add(1);
            this.add(2);
            this.add(3);
        }};

        List<Integer> list2 = new ArrayList<Integer>() {{
            this.add(2);
            this.add(3);
            this.add(4);
        }};

        //取并集
        list1.removeAll(list2);

        list1.forEach(System.out::println);
        System.out.println("-----------------------");
        list2.forEach(System.out::println);
    }


}