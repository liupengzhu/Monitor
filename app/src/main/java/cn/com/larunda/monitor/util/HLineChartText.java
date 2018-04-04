package cn.com.larunda.monitor.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sddt on 18-4-4.
 */

public class HLineChartText extends HorizontalBarChart {
    private int width;
    private int height;
    private int bottom;
    private int top;
    private Paint textPaint;
    private int barHeight;
    private List<String> textList = new ArrayList<>();

    public HLineChartText(Context context) {
        this(context, null);
    }

    public HLineChartText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HLineChartText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        bottom = getBottom();
        top = getTop();
        barHeight = height - 90;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < textList.size(); i++) {
            canvas.drawText(textList.get(i), 65, height - 62 - i * (barHeight / textList.size()), textPaint);
        }
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
        invalidate();
    }
}
