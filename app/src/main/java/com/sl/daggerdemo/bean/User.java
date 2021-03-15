package com.sl.daggerdemo.bean;

import javax.inject.Inject;

public class User {
    private String name = "sl";
    private int age = 30;

    @Inject
    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
