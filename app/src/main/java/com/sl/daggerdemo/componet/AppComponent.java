package com.sl.daggerdemo.componet;

import com.sl.daggerdemo.MainActivity;
import com.sl.daggerdemo.module.AppModule;

import dagger.Component;

@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
}
