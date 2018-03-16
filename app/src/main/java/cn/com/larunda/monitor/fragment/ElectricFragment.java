package cn.com.larunda.monitor.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.PieEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.util.BarChartManager;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.LineChartManager;
import cn.com.larunda.monitor.util.LineChartViewPager;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.PieChartManager;
import cn.com.larunda.monitor.util.PieChartViewPager;
import cn.com.larunda.monitor.util.Util;
import cn.com.larunda.monitor.util.XValueFormatter;
import cn.com.larunda.monitor.util.YValueFormatter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sddt on 18-3-14.
 */

public class ElectricFragment extends Fragment implements View.OnClickListener {

    private final String ELECTRIC_URL = MyApplication.URL + "power/usage" + MyApplication.TOKEN;
    private String date_type = "month";
    private String type = "original";

    private int[] colors = {MyApplication.getContext().getResources().getColor(R.color.valley_color),
            MyApplication.getContext().getResources().getColor(R.color.normal_color),
            MyApplication.getContext().getResources().getColor(R.color.peak_color),
            MyApplication.getContext().getResources().getColor(R.color.rush_color)};
    private SharedPreferences preferences;
    public static String token;
    private BarChartViewPager mBarChart;
    private LineChartViewPager mLineChart;
    private PieChartViewPager mPieChart;

    private DateDialog dateDialog;
    private DateDialog yearDialog;
    private DateDialog monthDialog;
    private TextView dateText;
    private RadioButton yearButton;
    private RadioButton monthButton;
    private RadioButton dayButton;
    private RadioGroup timeGroup;

    private RadioGroup typeGroup;
    private RadioButton originalButton;
    private RadioButton foldButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_electric, container, false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        sendRequest();
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);

        dateDialog = new DateDialog(getContext());
        yearDialog = new DateDialog(getContext(), false, false);
        monthDialog = new DateDialog(getContext(), true, false);
        dateText = view.findViewById(R.id.electric_date_text);
        yearButton = view.findViewById(R.id.electric_fragment_year_button);
        monthButton = view.findViewById(R.id.electric_fragment_month_button);
        dayButton = view.findViewById(R.id.electric_fragment_day_button);
        timeGroup = view.findViewById(R.id.electric_fragment_time_group);

        typeGroup = view.findViewById(R.id.electric_fragment_type_group);
        originalButton = view.findViewById(R.id.electric_fragment_original_button);
        foldButton = view.findViewById(R.id.electric_fragment_fold_button);

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

    /**
     * 初始化数据
     */
    private void initData() {
        long time = System.currentTimeMillis();
        String date = Util.parseTime(time, 2);
        dateText.setText(date);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        yearButton.setOnClickListener(this);
        monthButton.setOnClickListener(this);
        dayButton.setOnClickListener(this);

        originalButton.setOnClickListener(this);
        foldButton.setOnClickListener(this);

        dateText.setOnClickListener(this);
        dateDialog.setOnCancelClickListener(new DateDialog.OnCancelClickListener() {
            @Override
            public void OnClick(View view) {
                dateDialog.cancel();
            }
        });
        yearDialog.setOnCancelClickListener(new DateDialog.OnCancelClickListener() {
            @Override
            public void OnClick(View view) {
                yearDialog.cancel();
            }
        });
        monthDialog.setOnCancelClickListener(new DateDialog.OnCancelClickListener() {
            @Override
            public void OnClick(View view) {
                monthDialog.cancel();
            }
        });

        dateDialog.setOnOkClickListener(new DateDialog.OnOkClickListener() {
            @Override
            public void OnClick(View view, String date) {
                if (dateText != null && date != null) {
                    dateText.setText(date);
                }
                dateDialog.cancel();
            }
        });
        yearDialog.setOnOkClickListener(new DateDialog.OnOkClickListener() {
            @Override
            public void OnClick(View view, String date) {
                if (dateText != null && date != null) {
                    dateText.setText(date);
                }
                yearDialog.cancel();
            }
        });
        monthDialog.setOnOkClickListener(new DateDialog.OnOkClickListener() {
            @Override
            public void OnClick(View view, String date) {
                if (dateText != null && date != null) {
                    dateText.setText(date);
                }
                monthDialog.cancel();
            }
        });

    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.electric_fragment_year_button:
                long time1 = System.currentTimeMillis();
                String date1 = Util.parseTime(time1, 1);
                dateText.setText(date1);

                break;
            case R.id.electric_fragment_month_button:
                long time2 = System.currentTimeMillis();
                String date2 = Util.parseTime(time2, 2);
                dateText.setText(date2);

                break;
            case R.id.electric_fragment_day_button:
                long time3 = System.currentTimeMillis();
                String date3 = Util.parseTime(time3, 3);
                dateText.setText(date3);

                break;

            case R.id.electric_fragment_original_button:
                break;
            case R.id.electric_fragment_fold_button:
                break;
            case R.id.electric_date_text:
                if (timeGroup.getCheckedRadioButtonId() == R.id.electric_fragment_year_button) {
                    yearDialog.show();
                } else if (timeGroup.getCheckedRadioButtonId() == R.id.electric_fragment_month_button) {
                    monthDialog.show();
                } else {
                    dateDialog.show();
                }
                break;
        }
    }

    /**
     * 获取当前按钮组状态
     */
    private void getType() {
        if (typeGroup.getCheckedRadioButtonId() == R.id.electric_fragment_fold_button) {
            type = "fold";
        } else {
            type = "original";
        }
        if (timeGroup.getCheckedRadioButtonId() == R.id.electric_fragment_year_button) {
            date_type = "year";
        } else if (timeGroup.getCheckedRadioButtonId() == R.id.electric_fragment_day_button) {
            date_type = "day";
        } else {
            date_type = "month";
        }
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
        getType();
        String time = dateText.getText().toString().trim();
        HttpUtil.sendGetRequestWithHttp(ELECTRIC_URL + token + "&date_type=" + date_type + "&type=" + type
                + "&time=" + time, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                Log.d("main", content);
            }
        });
    }
}
