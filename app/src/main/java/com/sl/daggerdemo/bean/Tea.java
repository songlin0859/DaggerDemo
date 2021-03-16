package com.sl.daggerdemo.bean;

import javax.inject.Inject;

public class Tea extends Persion{
    
    @Override
    public String say(){
        return "Teacher";
    }
}
