package com.sl.daggerdemo.bean;

import javax.inject.Inject;

public class Persion {

    @Inject
    public Persion(){

    }

    public String say(){
        return "person";
    }
}
