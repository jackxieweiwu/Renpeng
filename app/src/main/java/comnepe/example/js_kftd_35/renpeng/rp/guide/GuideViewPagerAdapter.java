package comnepe.example.js_kftd_35.renpeng.rp.guide;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by renpeng on 2016/8/1.
 */
public class GuideViewPagerAdapter extends PagerAdapter {

    private List<View> viewList;

    public GuideViewPagerAdapter(List<View> viewList){
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList != null ? viewList.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }
}
