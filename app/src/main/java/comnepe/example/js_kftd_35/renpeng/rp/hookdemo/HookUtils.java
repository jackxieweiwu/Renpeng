package comnepe.example.js_kftd_35.renpeng.rp.hookdemo;

import android.app.Instrumentation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by renpeng on 2016/8/9.
 */
public class HookUtils {

    public static void hookInstrumentation() throws Exception{
        Class<?> activityThread=Class.forName("android.app.ActivityThread");
        Method currentActivityThread=activityThread.getDeclaredMethod("currentActivityThread");
        currentActivityThread.setAccessible(true);
        //获取主线程对象
        Object activityThreadObject=currentActivityThread.invoke(null);

        //获取Instrumentation字段
        Field mInstrumentation=activityThread.getDeclaredField("mInstrumentation");
        mInstrumentation.setAccessible(true);
        Instrumentation instrumentation= (Instrumentation) mInstrumentation.get(activityThreadObject);
        CustomInstrumentation customInstrumentation=new CustomInstrumentation(instrumentation);
        //替换掉原来的,就是把系统的instrumentation替换为自己的Instrumentation对象
        mInstrumentation.set(activityThreadObject,customInstrumentation);
        Log.d("[app]", "Hook Instrumentation成功");

    }
}
