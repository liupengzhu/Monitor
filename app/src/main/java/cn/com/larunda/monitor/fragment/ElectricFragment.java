package cn.com.larunda.monitor.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.util.BarChartManager;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.LineChartManager;
import cn.com.larunda.monitor.util.LineChartViewPager;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.PieChartManager;
import cn.com.larunda.monitor.util.PieChartViewPager;
import cn.com.larunda.monitor.util.XValueFormatter;
import cn.com.larunda.monitor.util.YValueFormatter;

/**
 * Created by sddt on 18-3-14.
 */

public class ElectricFragment extends Fragment {

    private int[] colors = {MyApplication.getContext().getResources().getColor(R.color.valley_color),
            MyApplication.getContext().getResources().getColor(R.color.normal_color),
            MyApplication.getContext().getResources().getColor(R.color.peak_color),
            MyApplication.getContext().getResources().getColor(R.color.rush_color)};
    private SharedPreferences preferences;
    public static String token;
    private BarChartViewPager mBarChart;
    private LineChartViewPager mLineChart;
    private PieChartViewPager mPieChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_electric, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);

        mBarChart = view.findViewById(R.id.electric_fragment_chart);
        BarChartManager manager = new BarChartManager(mBarChart);
        manager.setDescription("");
        manager.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "日";
            }
        });
        manager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + " kwh";
            }
        });
        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i <= 29; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                yValue.add((float) (Math.random() * 80));
            }
            yValues.add(yValue);
        }
        manager.showBarChart(yValues, colors);

        mLineChart = view.findViewById(R.id.electric_fragment_line);
        LineChartManager manager1 = new LineChartManager(mLineChart);
        manager1.setDescription("");
        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            xValues.add((float) i);
        }
        //设置y轴的数据()
        List<Float> yValue = new ArrayList<>();
        for (int j = 0; j <= 100; j++) {
            yValue.add((float) (Math.random() * 80 + 50));
        }
        manager1.showLineChart(xValues, yValue, "小时", Color.RED);
        manager1.setDescription("");

        mPieChart = view.findViewById(R.id.electric_fragment_pie);
        PieChartManager manager2 = new PieChartManager(mPieChart);

        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(40, "谷"));
        entries.add(new PieEntry(10, "平"));
        entries.add(new PieEntry(20, "峰"));
        entries.add(new PieEntry(15, "尖"));

        manager2.showPieChart(entries, colors);

    }
}
