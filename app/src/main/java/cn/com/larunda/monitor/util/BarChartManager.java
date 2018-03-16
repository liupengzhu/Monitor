package cn.com.larunda.monitor.util;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.R;

/**
 * Created by sddt on 18-3-13.
 */

public class BarChartManager {

    private BarChartViewPager mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    private XValueFormatter xValueFormatter;
    private YValueFormatter yValueFormatter;

    public BarChartManager(BarChartViewPager barChart) {
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
        Legend legend = mBarChart.getLegend();
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
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (yValueFormatter != null) {
                    return yValueFormatter.getFormattedValue(value, axis);
                }
                return (int) value + "";
            }
        });

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
                return (int) value + "";
            }
        });


    }

    /**
     * 展示柱状图(一条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param label
     * @param color
     */
    public void showBarChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, int color) {
        initLineChart();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < xAxisValues.size(); i++) {
            entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, label);

        barDataSet.setColor(color);
        barDataSet.setValueTextSize(9f);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        //设置X轴的刻度数
        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        data.setDrawValues(false);
        mBarChart.setData(data);
    }

    /**
     * 展示柱状图(多条)
     *
     * @param yAxisValues
     */
    public void showBarChart(float[][] yAxisValues, int[] colors) {
        initLineChart();
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        for (int i = 0; i < yAxisValues.length; i++) {
            yVals1.add(new BarEntry(i + 1, yAxisValues[i]));
        }
        BarDataSet set1;

        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(colors);
            set1.setStackLabels(new String[]{"谷", "平", "峰", "尖"});
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            /*data.setValueFormatter(new MainActivity.MyValueFormatter());*/
            data.setValueTextColor(Color.BLACK);
            data.setDrawValues(false);
            mBarChart.setData(data);
        }
        mBarChart.setFitBars(true);
        mBarChart.invalidate();
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

    public void setxValueFormatter(XValueFormatter xValueFormatter) {
        this.xValueFormatter = xValueFormatter;
    }

    public void setyValueFormatter(YValueFormatter yValueFormatter) {
        this.yValueFormatter = yValueFormatter;
    }
}
