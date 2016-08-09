package comnepe.example.js_kftd_35.renpeng.rp.hookdemo;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Field;

import comnepe.example.js_kftd_35.renpeng.base.BaseApplication;

/**
 * Created by renpeng on 2016/8/9.
 */
public class CustomInstrumentation extends Instrumentation{

    private Instrumentation base;

    public CustomInstrumentation(Instrumentation base) {
        this.base = base;
    }

    private void  getLoaderApk() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        BaseApplication myApplication=BaseApplication.getInstance();
        Field mLoadedApk=myApplication.getClass().getSuperclass().getDeclaredField("mLoadedApk");
        mLoadedApk.setAccessible(true);
        Object mLoadedApkObject=mLoadedApk.get(myApplication);
        Log.d("[app]", "获取的mLoadedApkObject=" + mLoadedApkObject);
    }

    //重写创建Activity的方法
    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Log.d("renpeng","哈哈，你被Hook了");
        try {
            getLoaderApk();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Log.d("renpeng","className="+className+" intent="+intent);
        return super.newActivity(cl, className, intent);
    }
}
