[dagger2](https://github.com/google/dagger)

[当定义Dagger2 Scope时，你在定义什么？](https://www.jianshu.com/p/2ba83fe00479)
[Dagger2在Android平台上的新魔法](https://www.jianshu.com/p/c01fdda42434)
[使用Dagger 2依赖注入 - 自定义Scope](https://www.cnblogs.com/tiantianbyconan/p/5095426.html)

### 使用
1. 引入依赖
```groovy
dependencies {
  implementation 'com.google.dagger:dagger:2.33'
  annotationProcessor 'com.google.dagger:dagger-compiler:2.33'
  //或者 kapt 'com.google.dagger:dagger-compiler:2.33'
}
```