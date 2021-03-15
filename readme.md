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

2. 最简单的示例
2.1 用@Inject标记User无参构造方法
```java
public class User {
    private String name = "sl";
    private int age = 30;

    @Inject
    public User(){

    }

    //... getter setter
}
```
2.2 在MainActivity中用@Inject标记需要注入的属性
```kotlin
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var user: User

    //...
}
```
2.3 编写一个MainComponent用于对MainActivity进行注入
```java
@Component
public interface MainComponent {
    /**
     * 对MainActivity进行注入 方法名称随意 取 xxx 都可以
     * @param activity 要注入的对象
     */
    void inject(MainActivity activity);
}
```
2.4 在MainActivity中调用MainComponent生成的DaggerMainComponent进行属性注入&使用
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMainComponent.builder().build().inject(this)
        setContentView(R.layout.activity_main)
        //显示 “name=sl, age=30”
        findViewById<TextView>(R.id.textView).text = "name=${user.name}, age=${user.age}"
    }
```