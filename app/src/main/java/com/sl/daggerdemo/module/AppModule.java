package com.sl.daggerdemo.module;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.sl.daggerdemo.anno.ActivityScope;
import com.sl.daggerdemo.anno.Student;
import com.sl.daggerdemo.anno.Teacher;
import com.sl.daggerdemo.bean.Persion;
import com.sl.daggerdemo.bean.Stu;
import com.sl.daggerdemo.bean.Tea;

import javax.inject.Singleton;

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
        Log.w("AppModule","provideContext");
        return context;
    }

    @Singleton
    @Provides
    Gson provideGson(){
        Log.w("AppModule","provideGson");
        return new Gson();
    }

    @Teacher
    @Provides
    Persion provideTeacher(){
        return new Tea();
    }

    @Student
    @Provides
    Persion provideStudent(){
        return new Stu();
    }
}
