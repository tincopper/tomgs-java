package com.tomgs.core.java8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *  If you wanna create new list, use Stream.map method:
 *
 * List<Fruit> newList = fruits.stream()
 *     .map(f -> new Fruit(f.getId(), f.getName() + "s", f.getCountry())
 *     .collect(Collectors.toList())
 * If you wanna modify current list, use Collection.forEach:
 *
 * fruits.forEach(f -> f.setName(f.getName() + "s"))
 *
 * @author tomgs
 * @version 2020/2/19 1.0 
 */
public class Demo1 {

    public static void main(String[] args) {
        List<User> groupList = new ArrayList<>();
        groupList.add(new User(1, "tomgs", 18));
        groupList.add(new User(2, "test", 19));
        groupList.add(new User(3, "testt", 20));

        Demo1 demo1 = new Demo1();
        List<User> users = demo1.testStream(groupList);
        System.out.println(users.toString());
    }

    private List<User> testStream(List<User> groupList) {
        List<User> user = Optional.ofNullable(groupList)
                .flatMap(this::extractClientAssignment)
                .orElse(null);
        return user;
    }

    private Optional<List<User>> extractClientAssignment(List<User> groupList) {
        Optional<User> any = groupList.stream()
                .filter(user -> "test".equals(user.getUserName()))
                .peek(user -> user.setAge(22)).findAny();

        return Optional.of(groupList);
    }
}
