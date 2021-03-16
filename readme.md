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

3. 注解 @Module @Component @Provides
对于有些类 构造方法是有参的 或者类似第三方jar中提供的 没法直接使用@Inject对构造方法进行标注 就需要用到@Provides

4. @Qulifier

5. @Scope & 单例
@Singleton注解标记的provide方法 产生的实例是单例的（比例本例中的Gson）

提供类的实例有两种途径 @Inject标记构造方法 或者 Module中通过@Provides提供provide方法
实验了一下 
```java
    @Singleton
    @Inject
    public User(){

    }
```
报错 `@Scope annotations are not allowed on @Inject constructors; annotate the class instead @Singleton`
直接使用`@Singleton` 
报错 `annot be provided without an @Inject constructor or an @Provides-annotated method.`
差不多@Singleton只能用在module中的provide方法上

如果给component依赖的module中的provide方法用了@Scope注解 那component也必须用相同的@Scope标记
否则报错`AppComponent (unscoped) may not reference scoped bindings`

Component如果用了@Scope标记 那Component依赖的module中的provide要么只能用相同的@Scope要么就不能用@Scope标记
```java
@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @ActivityScope
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
}

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity activity);
}
```

用了不同的@Scope会报错 
***AppComponent scoped with @Singleton may not reference bindings with different scopes:***

其实单例就是 Component 容器单例
如果Module中的provide方法用了和Component相同的@Scope 则会提供Component相同生命周期的单例
如果用了不同的@Scope 会报错！
如果没有用@Scope 那就每次需要实例的时候都去调用Module中的provide方法获取实例

/**
 * 不是单例 每次都调用provideContext 但是因为Context都是ApplicationContext 所以还是相等
 * 2021-03-16 20:40:53.393 22957-22957/com.sl.daggerdemo W/AppModule: provideContext
 * 2021-03-16 20:40:53.393 22957-22957/com.sl.daggerdemo W/AppModule: provideContext
 *
 * 对于单例 只有调用一次provideGson
 * 2021-03-16 20:40:53.393 22957-22957/com.sl.daggerdemo W/AppModule: provideGson
 */
 
通过代码中的MainActivity流程看注入原来
首先会根据生成一个对应的MainActivity_MembersInjector
getAppComponent().inject(this)就把需要的对象注入到MainActivity中
```java

public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {

  @InjectedFieldSignature("com.sl.daggerdemo.MainActivity.user")
  public static void injectUser(MainActivity instance, User user) {
    instance.user = user;
  }

  @InjectedFieldSignature("com.sl.daggerdemo.MainActivity.ctx")
  public static void injectCtx(MainActivity instance, Context ctx) {
    instance.ctx = ctx;
  }

  @InjectedFieldSignature("com.sl.daggerdemo.MainActivity.ctx1")
  public static void injectCtx1(MainActivity instance, Context ctx1) {
    instance.ctx1 = ctx1;
  }

  @InjectedFieldSignature("com.sl.daggerdemo.MainActivity.gson1")
  public static void injectGson1(MainActivity instance, Gson gson1) {
    instance.gson1 = gson1;
  }

  @InjectedFieldSignature("com.sl.daggerdemo.MainActivity.gson2")
  public static void injectGson2(MainActivity instance, Gson gson2) {
    instance.gson2 = gson2;
  }
}

public final class DaggerAppComponent implements AppComponent {

  @Override
  public void inject(MainActivity activity) {
    injectMainActivity(activity);
  }

  private MainActivity injectMainActivity(MainActivity instance) {
    MainActivity_MembersInjector.injectUser(instance, new User());
    MainActivity_MembersInjector.injectCtx(instance, AppModule_ProvideContextFactory.provideContext(appModule));
    MainActivity_MembersInjector.injectCtx1(instance, AppModule_ProvideContextFactory.provideContext(appModule));
    MainActivity_MembersInjector.injectGson1(instance, provideGsonProvider.get());
    MainActivity_MembersInjector.injectGson2(instance, provideGsonProvider.get());
    return instance;
  }
}

```

注入的关键方法
```java
private MainActivity injectMainActivity(MainActivity instance) {
    MainActivity_MembersInjector.injectUser(instance, new User());
    MainActivity_MembersInjector.injectCtx(instance, AppModule_ProvideContextFactory.provideContext(appModule));
    MainActivity_MembersInjector.injectCtx1(instance, AppModule_ProvideContextFactory.provideContext(appModule));
    MainActivity_MembersInjector.injectGson1(instance, provideGsonProvider.get());
    MainActivity_MembersInjector.injectGson2(instance, provideGsonProvider.get());
    return instance;
  }
```

User是通过构造方法添加@Inject这里是直接new User
ctx ctx1 都通过AppModule_ProvideContextFactory.provideContext(appModule)提供实例
```java
public final class AppModule_ProvideContextFactory implements Factory<Context> {
  private final AppModule module;

  public AppModule_ProvideContextFactory(AppModule module) {
    this.module = module;
  }

  public static Context provideContext(AppModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideContext());
  }
}
```
 其实每次通过com.sl.daggerdemo.module.AppModule.provideContext方法获取一个实例
 
 gson1 gson2 通过provideGsonProvider.get()获取
 ```java
public final class DaggerAppComponent implements AppComponent {
  private final AppModule appModule;

  private Provider<Gson> provideGsonProvider;

  private DaggerAppComponent(AppModule appModuleParam) {
    this.appModule = appModuleParam;
    initialize(appModuleParam);
  }

  @SuppressWarnings("unchecked")
  private void initialize(final AppModule appModuleParam) {
    this.provideGsonProvider = DoubleCheck.provider(AppModule_ProvideGsonFactory.create(appModuleParam));
  }
} 
```
provideGsonProvider在DaggerAppComponent构造方法中初始化

DoubleCheck.provider(AppModule_ProvideGsonFactory.create(appModuleParam));
AppModule_ProvideGsonFactory.create获取一个AppModule_ProvideGsonFactory实例
```java
public static <P extends Provider<T>, T> Provider<T> provider(P delegate) {
    checkNotNull(delegate);
    if (delegate instanceof DoubleCheck) {
      /* This should be a rare case, but if we have a scoped @Binds that delegates to a scoped
       * binding, we shouldn't cache the value again. */
      return delegate;
    }
    return new DoubleCheck<T>(delegate);
  }
```

```java
public final class DoubleCheck<T> implements Provider<T>, Lazy<T> {
  private static final Object UNINITIALIZED = new Object();

  private volatile Provider<T> provider;
  private volatile Object instance = UNINITIALIZED;

  private DoubleCheck(Provider<T> provider) {
    assert provider != null;
    this.provider = provider;
  }

  @SuppressWarnings("unchecked") // cast only happens when result comes from the provider
  @Override
  public T get() {
    Object result = instance;
    if (result == UNINITIALIZED) {
      synchronized (this) {
        result = instance;
        if (result == UNINITIALIZED) {
          result = provider.get();
          instance = reentrantCheck(instance, result);
          /* Null out the reference to the provider. We are never going to need it again, so we
           * can make it eligible for GC. */
          provider = null;
        }
      }
    }
    return (T) result;
  }
}
```
其实就是通过dagger.internal.DoubleCheck.get获取一个实例（单例）
 