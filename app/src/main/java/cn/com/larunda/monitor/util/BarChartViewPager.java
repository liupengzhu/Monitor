package cn.com.larunda.monitor.util;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.BarChart;

/**
 * Created by sddt on 18-3-15.
 */

public class BarChartViewPager extends BarChart {
    PointF downPoint = new PointF();

    public BarChartViewPager(Context context) {
        super(context);
    }

    public BarChartViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChartViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
