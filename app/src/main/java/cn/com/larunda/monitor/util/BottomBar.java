package cn.com.larunda.monitor.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import cn.com.larunda.monitor.R;

/**
 * Created by sddt on 18-4-9.
 */

public class BottomBar extends LinearLayout {
    private int downX;
    private int downY;
    private int scrollOffset;
    private Scroller mScroller;
    private View bottomBar;
    private View bottomContent;
    private float downY2;
    private float offset;
    private boolean isShow = false;
    private ImageView imageView;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bottomBar = getChildAt(0);
        bottomContent = getChildAt(1);
        bottomBar.layout(0, getMeasuredHeight() - bottomBar.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        bottomContent.layout(0, getMeasuredHeight(), getMeasuredWidth(), bottomBar.getBottom() + bottomContent.getMeasuredHeight());
        imageView = bottomBar.findViewById(R.id.map_up_view);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                downY2 = event.getY();
                //当内容布局未显示时不消费点击事件
                if (!isShow) {
                    if (downY2 < bottomContent.getMeasuredHeight()) {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int endY = (int) event.getY();
                int dy = (int) (endY - downY);
                int toScroll = getScrollY() - dy;
                if (toScroll < 0) {
                    toScroll = 0;
                } else if (toScroll > bottomContent.getMeasuredHeight()) {
                    toScroll = bottomContent.getMeasuredHeight();
                }
                scrollTo(0, toScroll);
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                scrollOffset = getScrollY();
                offset = event.getY() - downY2;
                if (offset > 0) {
                    closeNavigation();
                } else if (offset < 0) {
                    if (scrollOffset > bottomContent.getMeasuredHeight() / 4) {
                        showNavigation();
                    } else {
                        closeNavigation();
                    }

                } else {
                    if (isShow) {
                        closeNavigation();
                    } else {
                        showNavigation();
                    }
                }
                break;
        }

        return true;
    }

    private void showNavigation() {
        int dy = bottomContent.getMeasuredHeight() - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
        isShow = true;
        imageView.setRotation(180);
    }

    private void closeNavigation() {
        int dy = 0 - scrollOffset;
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);
        invalidate();
        isShow = false;
        imageView.setRotation(0);
    }

    @Override
    public void computeScroll() {
        //判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

}
