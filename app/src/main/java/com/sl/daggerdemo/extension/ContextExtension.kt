package com.sl.daggerdemo.extension

import android.content.Context
import com.sl.daggerdemo.App
import com.sl.daggerdemo.componet.AppComponent

fun Context.getAppComponent(): AppComponent {
     return (applicationContext as App).appComponent
}