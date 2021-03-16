package com.sl.daggerdemo.componet;

import com.sl.daggerdemo.MainActivity;
import com.sl.daggerdemo.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity activity);
}
