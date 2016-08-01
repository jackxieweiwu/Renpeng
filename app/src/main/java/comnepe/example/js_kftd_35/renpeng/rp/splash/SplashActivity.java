package comnepe.example.js_kftd_35.renpeng.rp.splash;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import comnepe.example.js_kftd_35.renpeng.R;
import comnepe.example.js_kftd_35.renpeng.rp.guide.GuideActivity;

public class SplashActivity extends Activity {
    RelativeLayout parentRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initAnimation();
        initShowTime();
    }

    private void initView(){
        parentRl = (RelativeLayout) findViewById(R.id.parent_rl);
    }

    private void initAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f,1.0f);
        alphaAnimation.setDuration(2000);
        parentRl.startAnimation(alphaAnimation);
    }

    private void initShowTime(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                GuideActivity.startGuideActivity(SplashActivity.this);
                finish();
            }
        };
        timer.schedule(timerTask,3000);
    }
}
