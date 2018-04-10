package cn.com.larunda.monitor.util;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sddt on 18-3-13.
 */

public class HBarChartManager {

    private HLineChartText mBarChart;

    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    private XValueFormatter xValueFormatter;
    private YValueFormatter yValueFormatter;
    private Legend legend;

    public HBarChartManager(HLineChartText barChart) {
        this.mBarChart = barChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
    }

    /**
     * 初始化LineChart
     */
    private void initLineChart() {
        //背景颜色
        mBarChart.setBackgroundColor(Color.WHITE);
        //网格
        mBarChart.setDrawGridBackground(false);

        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);

        mBarChart.setDrawValueAboveBar(false);

        //显示边界
        mBarChart.setDrawBorders(false);
        //设置动画效果
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);
        //禁止缩放
        mBarChart.setScaleEnabled(false);

        //折线图例 标签 设置
        legend = mBarChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);


        //XY轴的设置
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(true);
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (yValueFormatter != null) {
                    return yValueFormatter.getFormattedValue(value, axis);
                }
                return (int) value + "";
            }
        });
        leftAxis.setEnabled(false);

        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        /*xAxis.setCenterAxisLabels(true);*/
        /*xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(31);*/
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (xValueFormatter != null) {
                    return xValueFormatter.getFormattedValue(value, axis);
                }
                return "";
            }
        });

    }

    /**
     * 展示柱状图(一条)
     *
     * @param yAxisValues
     * @param label
     * @param colors
     */
    public void showBarChart(float[] yAxisValues, String label, int[] colors) {
        initLineChart();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < yAxisValues.length; i++) {
            entries.add(new BarEntry((i + 1), yAxisValues[i]));
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, label);

        barDataSet.setColors(colors);
        barDataSet.setValueTextSize(9f);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        //设置X轴的刻度数
        xAxis.setLabelCount(6, false);
        data.setDrawValues(false);

        legend.setEnabled(false);//隐藏折标
        mBarChart.setData(data);
    }


    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        mBarChart.invalidate();
    }

    /**
     * 设置X轴的值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setXAxis(float max, float min, int labelCount) {
        xAxis.setAxisMaximum(max);
        xAxis.setAxisMinimum(min);
        xAxis.setLabelCount(labelCount, false);

        mBarChart.invalidate();
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftAxis.addLimitLine(hightLimit);
        mBarChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftAxis.addLimitLine(hightLimit);
        mBarChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }

    public void setContent(List<String> textList) {
        mBarChart.setTextList(textList);
    }

    public void setxValueFormatter(XValueFormatter xValueFormatter) {
        this.xValueFormatter = xValueFormatter;
    }

    public void setyValueFormatter(YValueFormatter yValueFormatter) {
        this.yValueFormatter = yValueFormatter;
    }
}
