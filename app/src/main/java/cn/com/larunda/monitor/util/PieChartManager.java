package cn.com.larunda.monitor.util;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sddt on 18-3-15.
 */

public class PieChartManager {
    private PieChartViewPager pieChart;

    public PieChartManager(PieChartViewPager pieChart) {
        this.pieChart = pieChart;
    }

    private void initChart() {
        //饼状图

        pieChart.setUsePercentValues(true);//设置为TRUE的话，图标中的数据自动变为percent
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);//设置额外的偏移量(在图表视图周围)

        pieChart.setDragDecelerationFrictionCoef(0.95f);//设置滑动减速摩擦系数，在0~1之间
        //设置中间文件
        // pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawSliceText(false);//设置隐藏饼图上文字，只显示百分比
        pieChart.setDrawHoleEnabled(false);//设置为TRUE时，饼中心透明
        pieChart.setHoleColor(Color.WHITE);//设置饼中心颜色

        pieChart.setTransparentCircleColor(Color.WHITE);//透明的圆
        pieChart.setTransparentCircleAlpha(110);//透明度

        //pieChart.setHoleRadius(58f);//中间圆的半径占总半径的百分数
        pieChart.setHoleRadius(0);//实心圆
        //pieChart.setTransparentCircleRadius(61f);//// 半透明圈

        pieChart.setDrawCenterText(true);//绘制显示在饼图中心的文本

        pieChart.setRotationAngle(0);//设置一个抵消RadarChart的旋转度
        // 触摸旋转
        pieChart.setRotationEnabled(true);//通过触摸使图表旋转
        pieChart.setHighlightPerTapEnabled(true);//通过点击手势突出显示的值

        /**
         * 设置比例图
         */
        Legend legend = pieChart.getLegend();

        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        /*mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);  //在左边中间显示比例图
        mLegend.setFormSize(14f);//比例块字体大小
        mLegend.setXEntrySpace(4f);//设置距离饼图的距离，防止与饼图重合
        mLegend.setYEntrySpace(4f);
        //设置比例块换行...
        mLegend.setWordWrapEnabled(true);
        mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//设置字跟图表的左右顺序

        //mLegend.setTextColor(getResources().getColor(R.color.alpha_80));
        mLegend.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块
//        mLegend.setEnabled(false);//设置禁用比例块*/


        //变化监听
        // pieChart.setOnChartValueSelectedListener(this);


        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

      /*  Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f)*/
        ;

        // 输入标签样式
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);

    }

    //设置中间文字
//    private SpannableString generateCenterSpannableText() {
//        //原文：MPAndroidChart\ndeveloped by Philipp Jahoda
//        SpannableString s = new SpannableString("项目");
//        //s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        //s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        // s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        // s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
//        return s;
//    }

    //设置数据
    public void showPieChart(ArrayList<PieEntry> entries, int[] colors) {
        initChart();
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);//饼图区块之间的距离
        dataSet.setSelectionShift(5f);//

        //添加对应的颜色值
        List<Integer> colorSum = new ArrayList<>();
        for (Integer color : colors) {
            colorSum.add(color);
        }
        dataSet.setColors(colorSum);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setDrawValues(false);
        pieChart.setData(data);
        pieChart.highlightValues(null);//在给定的数据集中突出显示给定索引的值
        //刷新
        pieChart.invalidate();
    }

}
