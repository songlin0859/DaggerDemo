package com.sl.daggerdemo;

import android.app.Application;

import com.sl.daggerdemo.componet.AppComponent;
import com.sl.daggerdemo.componet.DaggerAppComponent;
import com.sl.daggerdemo.module.AppModule;

public class App extends Application {
    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
