package com.sl.daggerdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sl.daggerdemo.bean.User
import com.sl.daggerdemo.componet.DaggerMainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMainComponent.builder().build().inject(this)
        setContentView(R.layout.activity_main)
        //显示 “name=sl, age=30”
        findViewById<TextView>(R.id.textView).text = "name=${user.name}, age=${user.age}"
    }
}
