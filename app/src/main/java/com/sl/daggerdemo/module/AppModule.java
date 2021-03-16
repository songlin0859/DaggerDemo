package com.sl.daggerdemo.module;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext(){
        return context;
    }

    @Provides
    Gson provideGson(){
        Log.w("AppModule","provideGson");
        return new Gson();
    }
}
