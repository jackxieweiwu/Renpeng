package comnepe.example.js_kftd_35.renpeng.rp.guide;

import android.app.Activity;
import android.content.Intent;

import comnepe.example.js_kftd_35.renpeng.base.BaseActivity;

/**
 * Created by renpeng on 2016/8/1.
 */
public class GuideActivity extends BaseActivity {

    public static void startGuideActivity(Activity activity){
        activity.startActivity(new Intent(activity,GuideActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void init() {

    }
}
