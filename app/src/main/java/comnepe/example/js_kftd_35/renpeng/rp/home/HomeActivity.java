package comnepe.example.js_kftd_35.renpeng.rp.home;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import comnepe.example.js_kftd_35.renpeng.R;
import comnepe.example.js_kftd_35.renpeng.base.BaseActivity;
import comnepe.example.js_kftd_35.renpeng.rp.home.view.DragAdapter;
import comnepe.example.js_kftd_35.renpeng.rp.home.view.MyDragGridView;

/**
 * Created by renpeng on 2016/8/1.
 */
public class HomeActivity extends BaseActivity {

    private List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();

    public static void startHomeActivity(Activity activity){
        activity.startActivity(new Intent(activity,HomeActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void init() {
        MyDragGridView mDragGridView = (MyDragGridView) findViewById(R.id.dragGridView);
        for (int i = 0; i < 60; i++) {
            HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
            itemHashMap.put("item_image",R.mipmap.com_tencent_open_notice_msg_icon_big);
            itemHashMap.put("item_text", "拖 " + Integer.toString(i));
            dataSourceList.add(itemHashMap);
        }

        final DragAdapter mDragAdapter = new DragAdapter(this, dataSourceList);

        mDragGridView.setAdapter(mDragAdapter);
        mDragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this,position +"",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
