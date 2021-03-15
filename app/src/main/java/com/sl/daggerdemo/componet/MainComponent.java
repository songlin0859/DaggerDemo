package com.sl.daggerdemo.componet;

import com.sl.daggerdemo.MainActivity;

import dagger.Component;

@Component
public interface MainComponent {
    void inject(MainActivity activity);
}
