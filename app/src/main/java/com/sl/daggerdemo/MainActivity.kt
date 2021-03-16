package com.sl.daggerdemo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.sl.daggerdemo.bean.User
import com.sl.daggerdemo.extension.getAppComponent
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var user: User

    @Inject
    lateinit var ctx:Context
    @Inject
    lateinit var ctx1:Context

    @Inject
    lateinit var gson1: Gson
    @Inject
    lateinit var gson2: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //DaggerMainComponent.builder().build().inject(this)
        getAppComponent().inject(this)
        setContentView(R.layout.activity_main)
        //显示 “name=sl, age=30” app = xxxx
        textView.text = "name=${user.name}, age=${user.age}"
        //findViewById<TextView>(R.id.textView).text = "app = ${ctx}"
        // false
        textView1.text = "gson1 = gson2 = ${gson1 === gson2}"
        textView2.text = "ctx = ctx1 = ${ctx === ctx1}"

        /**
         * 不是单例 每次都调用provideContext 但是因为Context都是ApplicationContext 所以还是相等
         * 2021-03-16 20:40:53.393 22957-22957/com.sl.daggerdemo W/AppModule: provideContext
         * 2021-03-16 20:40:53.393 22957-22957/com.sl.daggerdemo W/AppModule: provideContext
         *
         * 对于单例 只有调用一次provideGson
         * 2021-03-16 20:40:53.393 22957-22957/com.sl.daggerdemo W/AppModule: provideGson
         */
    }
}
