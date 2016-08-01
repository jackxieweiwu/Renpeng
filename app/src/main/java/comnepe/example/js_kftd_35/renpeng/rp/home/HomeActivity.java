package comnepe.example.js_kftd_35.renpeng.rp.home;

import android.app.Activity;
import android.content.Intent;

import comnepe.example.js_kftd_35.renpeng.base.BaseActivity;

/**
 * Created by renpeng on 2016/8/1.
 */
public class HomeActivity extends BaseActivity {

    public static void startHomeActivity(Activity activity){
        activity.startActivity(new Intent(activity,HomeActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void init() {

    }
}
