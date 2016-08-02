package comnepe.example.js_kftd_35.renpeng.rp.home.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by renpeng on 2016/8/2.
 */
public class MyDragGridView extends GridView {

    //按下多久才可以拖动
    private long pressTime = 1000;

    //按下是的x,y坐标
    private int xDown;
    private int yDown;

    //移动时的x,y坐标
    private int xMove;
    private int yMove;

    private WindowManager mWindow;
    private WindowManager.LayoutParams layoutParams;

    //长按的item的view的position
    private int selectViewPosition;

    //长按的item的view；
    private View selectView;

    private Handler mHandler = new Handler();

    private int statusHeight;

    private Bitmap dragViewBitmap;

    //GridView距离屏幕顶部的距离（除状态栏）
    private int offset2Top;
    //GridView距离屏幕左边的距离
    private int offset2Left;

    //按下的点距离item上边缘的距离
    private int point2Top;
    //按下的点距离左边缘的距离
    private int point2Left;

    //自动上滑的边界
    private int scrollTopBorder;
    //自动下滑的边界
    private int scrollBottomBorder;

    //用于拖动的view
    private ImageView dragView;

    //长按时的响应事件
    private Runnable dragRunning = new Runnable() {
        @Override
        public void run() {
            isDrag = true;
            selectView.setVisibility(INVISIBLE);
            createDragView(dragViewBitmap,xDown,yDown);
        }
    };

    private boolean mAnimationEnd = true;

    //自动滚动响应事件
    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollY;
            if(getFirstVisiblePosition() == 0 || getLastVisiblePosition() == getCount() - 1){
                mHandler.removeCallbacks(mScrollRunnable);
            }
            if(yMove < scrollTopBorder){
                scrollY = - speed;
                mHandler.postDelayed(mScrollRunnable,25);
            }else if(yMove > scrollBottomBorder){
                scrollY = speed;
                mHandler.postDelayed(mScrollRunnable,20);
            }else{
                scrollY = 0;
                mHandler.removeCallbacks(mScrollRunnable);
            }
            smoothScrollBy(scrollY,10);
        }
    };

    //自动划定的速度
    private static int speed = 20;

    private DragGridBaseAdapter mDragGridBaseAdapter;

    //列数
    private int colums;

    //列宽
    private int columnWidth;

    //是否设置了列数
    private boolean isSetColumn;

    private int mHorizontalSpacing;

    //是否可以拖拽
    private boolean isDrag = false;

    public MyDragGridView(Context context) {
        super(context);
        init(context);
    }

    public MyDragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyDragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mWindow = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        statusHeight = getStatusHeight(context);
        if(!isSetColumn){
            colums = AUTO_FIT;
        }
    }

    //设置长按相应时间
    private void setPressTime(long pressTime){
        this.pressTime = pressTime;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if(adapter instanceof DragGridBaseAdapter){
            mDragGridBaseAdapter = (DragGridBaseAdapter) adapter;
        }else{
            throw new IllegalStateException("the adapter must be implements DragGridAdapter");
        }
    }

    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        colums = numColumns;
        isSetColumn = true;
    }

    @Override
    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
        this.columnWidth = columnWidth;
    }

    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {
        super.setHorizontalSpacing(horizontalSpacing);
        this.mHorizontalSpacing = horizontalSpacing;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(colums == AUTO_FIT){
            int numColumnFit;
            if(columnWidth > 0){
                int width = Math.max(MeasureSpec.getSize(widthMeasureSpec),0);
                numColumnFit = width/columnWidth;
                if(numColumnFit > 0){
                    while(numColumnFit > 0){
                        if(numColumnFit * columnWidth + (numColumnFit-1) * mHorizontalSpacing > width){
                            numColumnFit -- ;
                        }else{
                            break;
                        }
                    }
                }else{
                    numColumnFit = 1;
                }
            }else{
                numColumnFit = 2;
            }
            colums = numColumnFit;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = (int) ev.getX();
                yDown = (int) ev.getY();

                //根据按下的点来获取相应的item的view
                selectViewPosition = pointToPosition(xDown, yDown);

                //当点击的位置不存在时，交给上层处理
                if(selectViewPosition == AdapterView.INVALID_POSITION){
                    return super.dispatchTouchEvent(ev);
                }

                //这里注意，必须减去getFirstVisiblePosition()，来获取当前屏幕显示的第几个item的view，如果不减去getFirstVisiblePosition()，将会崩溃
                selectView = getChildAt(selectViewPosition - getFirstVisiblePosition());
                mHandler.postDelayed(dragRunning,pressTime);

                point2Top =  yDown - selectView.getTop();
                point2Left = xDown - selectView.getLeft();

                offset2Top = (int)(ev.getRawY() - yDown);
                offset2Left = (int) (ev.getRawX() - xDown);

                scrollTopBorder = getHeight()/4;
                scrollBottomBorder = getHeight() * 4/5;

                selectView.setDrawingCacheEnabled(true);
                dragViewBitmap = Bitmap.createBitmap(selectView.getDrawingCache());
                selectView.destroyDrawingCache();

                break;
            case MotionEvent.ACTION_MOVE:
                xMove = (int) ev.getX();
                yMove = (int) ev.getY();
                if(!isTouchInItem(selectView,xMove,yMove)){
                    mHandler.removeCallbacks(dragRunning);
                }
                break;
            case MotionEvent.ACTION_UP:
                removeDragHandler();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchInItem(View view,int x,int y){
        if(view == null){
            return false;
        }
        int leftOffset = view.getLeft();
        int topOffset = view.getTop();
        if(x < leftOffset || x > leftOffset + view.getWidth()){
            return false;
        }
        if(y <topOffset || y > topOffset + view.getHeight()){
            return false;
        }
        return true;
    }

    //移除长按相应操作
    private void removeDragHandler(){
        mHandler.removeCallbacks(dragRunning);
        mHandler.removeCallbacks(mScrollRunnable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isDrag && dragView != null){
            switch (ev.getAction()){
                case MotionEvent.ACTION_MOVE:
                    xMove = (int) ev.getX();
                    yMove = (int) ev.getY();
                    onDragItem(xMove,yMove);
                    break;
                case MotionEvent.ACTION_UP:
                    onStopDrag();
                    break;
            }
        }

        return super.onTouchEvent(ev);
    }

    private void onDragItem(int x,int y){
        layoutParams.x = x + offset2Left - point2Left;
        layoutParams.y = y + offset2Top - point2Top - statusHeight;
        mWindow.updateViewLayout(dragView, layoutParams);
        swapItem(x,y);
        mHandler.post(mScrollRunnable);

    }

    private void onStopDrag(){
        View view = getChildAt(selectViewPosition - getFirstVisiblePosition());
        if(view != null){
            view.setVisibility(View.VISIBLE);
        }
        mDragGridBaseAdapter.setHideItem(-1);
        removeDragImage();
    }

    private void removeDragImage(){
        if(dragView != null){
            mWindow.removeView(dragView);
            dragView = null;
        }
    }

    private void swapItem(int x,int y){
        final int tempPosition = pointToPosition(x,y);

        if(tempPosition != selectViewPosition && tempPosition != AdapterView.INVALID_POSITION && mAnimationEnd){
            mDragGridBaseAdapter.reorderItems(selectViewPosition, tempPosition);
            mDragGridBaseAdapter.setHideItem(tempPosition);

            final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    viewTreeObserver.removeOnPreDrawListener(this);
                    animatorReOrder(selectViewPosition,tempPosition);
                    selectViewPosition = tempPosition;
                    return false;
                }
            });
        }
    }

    private void animatorReOrder(int oldPosition,int newPosition){
        boolean isForward = newPosition > oldPosition;
        List<Animator> resultList = new LinkedList<Animator>();
        if (isForward) {
            for (int pos = oldPosition; pos < newPosition; pos++) {
                View view = getChildAt(pos - getFirstVisiblePosition());
                System.out.println(pos);

                if ((pos + 1) % colums == 0) {
                    resultList.add(createTranslationAnimations(view,
                            - view.getWidth() * (colums - 1), 0,
                            view.getHeight(), 0));
                } else {
                    resultList.add(createTranslationAnimations(view,
                            view.getWidth(), 0, 0, 0));
                }
            }
        } else {
            for (int pos = oldPosition; pos > newPosition; pos--) {
                View view = getChildAt(pos - getFirstVisiblePosition());
                if ((pos + colums) % colums == 0) {
                    resultList.add(createTranslationAnimations(view,
                            view.getWidth() * (colums - 1), 0,
                            -view.getHeight(), 0));
                } else {
                    resultList.add(createTranslationAnimations(view,
                            -view.getWidth(), 0, 0, 0));
                }
            }
        }


        AnimatorSet resultSet = new AnimatorSet();
        resultSet.playTogether(resultList);
        resultSet.setDuration(300);
        resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
        resultSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationEnd = true;
            }
        });
        resultSet.start();


    }

    private AnimatorSet createTranslationAnimations(View view, float startX,
                                                    float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
                startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
                startY, endY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }

    private void createDragView(Bitmap bitmap,int x,int y){
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.x = x + offset2Left - point2Left;
        layoutParams.y = y + offset2Top - point2Top - statusHeight;
        layoutParams.gravity = Gravity.TOP| Gravity.LEFT;
        layoutParams.alpha = 0.5f;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        dragView = new ImageView(getContext());
        dragView.setImageBitmap(bitmap);
        mWindow.addView(dragView,layoutParams);

    }

    //获取状态栏高度
    private static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
}
