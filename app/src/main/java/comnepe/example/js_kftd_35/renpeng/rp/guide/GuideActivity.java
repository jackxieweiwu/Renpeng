package comnepe.example.js_kftd_35.renpeng.rp.guide;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import comnepe.example.js_kftd_35.renpeng.R;
import comnepe.example.js_kftd_35.renpeng.base.BaseActivity;
import comnepe.example.js_kftd_35.renpeng.rp.home.HomeActivity;

/**
 * Created by renpeng on 2016/8/1.
 */
public class GuideActivity extends BaseActivity {
    private ViewPager mGuideViewPager;
    private GuideViewPagerAdapter mGuideViewPagerAdapter;
    private List<View> viewList;

    public static void startGuideActivity(Activity activity){
        activity.startActivity(new Intent(activity,GuideActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init() {
        mGuideViewPager = getViewById(R.id.guide_view_pager);
        initViewPagerItemView();
        mGuideViewPagerAdapter = new GuideViewPagerAdapter(viewList);
        mGuideViewPager.setAdapter(mGuideViewPagerAdapter);
    }

    private void initViewPagerItemView(){
        viewList = new ArrayList<View>();
        View itemView1 = getLayoutInflater().inflate(R.layout.guide_first_pager_layout,null);
        View itemView2 = getLayoutInflater().inflate(R.layout.guide_second_pager_layout,null);
        View itemView3 = getLayoutInflater().inflate(R.layout.guide_third_pager_layout,null);
        itemView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.startHomeActivity(GuideActivity.this);
                finish();
            }
        });
        viewList.add(itemView1);
        viewList.add(itemView2);
        viewList.add(itemView3);
    }
}
