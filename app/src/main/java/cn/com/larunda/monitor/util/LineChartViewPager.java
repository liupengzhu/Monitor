package cn.com.larunda.monitor.util;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by sddt on 18-3-15.
 */

public class LineChartViewPager extends LineChart {

    PointF downPoint = new PointF();

    public LineChartViewPager(Context context) {
        super(context);
    }

    public LineChartViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
