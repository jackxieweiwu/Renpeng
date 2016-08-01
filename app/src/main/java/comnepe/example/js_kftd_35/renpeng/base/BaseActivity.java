package comnepe.example.js_kftd_35.renpeng.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by renpeng on 2016/8/1.
 */
public abstract class BaseActivity extends FragmentActivity {

    private static final String TAG = "BaseActivity";
    private static final int NO_LAYOUT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        View contentView = null;
        if(layoutId != NO_LAYOUT){
            contentView = getLayoutInflater().inflate(layoutId,null);
            setContentView(contentView);
        }else{
            Log.d(TAG,"no content view");
        }

        init();
    }

    protected abstract int getLayoutId();

    protected abstract void init();
}
