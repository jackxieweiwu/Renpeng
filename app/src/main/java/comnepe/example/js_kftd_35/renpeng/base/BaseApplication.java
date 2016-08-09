package comnepe.example.js_kftd_35.renpeng.base;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import comnepe.example.js_kftd_35.renpeng.rp.hookdemo.CustomInstrumentation;

/**
 * Created by renpeng on 2016/8/9.
 */
public class BaseApplication extends Application {

    private static BaseApplication instance = null;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        Class<?> activityThreadClass = null;
        try {
            //加载activity thread的class
            activityThreadClass = Class.forName("android.app.ActivityThread", false, getClassLoader());

            //找到方法currentActivityThread
            Method method = activityThreadClass.getDeclaredMethod("currentActivityThread");
            //由于这个方法是静态的，所以传入Null就行了
            Object currentActivityThread = method.invoke(null);

            //把之前ActivityThread中的mInstrumentation替换成我们自己的
            Field field = activityThreadClass.getDeclaredField("mInstrumentation");
            field.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) field.get(currentActivityThread);
            CustomInstrumentation instrumentationProxy = new CustomInstrumentation(instrumentation);
            field.set(currentActivityThread, instrumentationProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized BaseApplication getInstance() {
        return instance;
    }
}
