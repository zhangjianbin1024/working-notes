package com.myke.other;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjianbin
 * @date 2021年08月04日11:49
 */
public class OtherTest {

    @Test
    public void t20(){
        System.out.println(20/8);
        System.out.println(20%8+1);
    }

    @Test
    public void t1() {
        System.out.println(Arrays.asList(1, 2).contains(Integer.valueOf(2)));
    }

    @Test
    public void t2() {
        ArrayList<String> list = new ArrayList<>();
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
            if ("5".equals(next)) {
                iterator.remove();
            }
        }

        System.out.println(list);
    }

    @Test
    public void t3() {
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(new User(1L, "aa"));
        userArrayList.add(new User(2L, "bb"));
        userArrayList.add(new User(3L, "cc"));
        Map<Long, User> collect = userArrayList.stream().collect(Collectors.toMap(User::getId, obj -> obj, (key1, key2) -> key2));

        System.out.println(collect);
    }

    @Test
    public void t4() {
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(new User(1L, "aa"));
        userArrayList.add(new User(2L, "bb"));
        userArrayList.add(new User(3L, "cc"));
        userArrayList.add(new User(3L, "dd"));
        userArrayList.add(new User(1L, "dd"));

        Map<Long, List<String>> collect = userArrayList.stream()
                .collect(Collectors.groupingBy(User::getId, Collectors.mapping(User::getAddress, Collectors.toList())));


        System.out.println(collect);
    }

    @Test
    public void t5() {
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(new User(1L, "aa"));
        userArrayList.add(new User(2L, "bb"));
        userArrayList.add(new User(3L, "cc"));
        userArrayList.add(new User(3L, "dd"));
        userArrayList.add(new User(1L, "dd"));

        Map<Long, List<User>> collect = userArrayList.stream()
                .collect(Collectors.groupingBy(User::getId));


        System.out.println(collect);
    }

    @Test
    public void t6() {
        HashSet<Long> set = new HashSet<>();
        set.add(1L);
        set.add(3L);
        set.add(4L);

        HashMap<String, Set<Long>> map = new HashMap<>();

        String key = "a";
        map.put(key, set);

        Set<Long> value = map.get(key);
        value.add(5L);

        System.out.println(map);
    }

    public static class User {
        public User() {
        }

        public User(Long id, String address) {
            this.id = id;
            this.address = address;
        }

        public User(Long id, String address, List<String> list) {
            this.id = id;
            this.address = address;
            this.list = list;
        }

        private Long id;
        private String address;
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("User{");
            sb.append("id=").append(id);
            sb.append(", address='").append(address).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}