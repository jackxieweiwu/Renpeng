package comnepe.example.js_kftd_35.renpeng.rp.home.view;

/**
 * Created by renpeng on 2016/8/1.
 */
public interface DragGridBaseAdapter {
    /**
     * 重新排列数据
     * @param oldPosition
     * @param newPosition
     */
    public void reorderItems(int oldPosition, int newPosition);


    /**
     * 设置某个item隐藏
     * @param hidePosition
     */
    public void setHideItem(int hidePosition);


}
