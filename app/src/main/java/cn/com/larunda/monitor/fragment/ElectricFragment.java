package cn.com.larunda.monitor.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.ElectricRecyclerAdapter;
import cn.com.larunda.monitor.bean.ElectricBean;
import cn.com.larunda.monitor.gson.DayElectricInfo;
import cn.com.larunda.monitor.gson.ElectricInfo;
import cn.com.larunda.monitor.gson.UnitInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BarChartManager;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.BarOnClickListener;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.LineChartManager;
import cn.com.larunda.monitor.util.LineChartViewPager;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.PieChartManager;
import cn.com.larunda.monitor.util.PieChartViewPager;
import cn.com.larunda.monitor.util.Util;
import cn.com.larunda.monitor.util.XValueFormatter;
import cn.com.larunda.monitor.util.XYMarkerView;
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

    private List<String> normal = new ArrayList<>();
    private List<String> rush = new ArrayList<>();
    private List<String> valley = new ArrayList<>();
    private List<String> peak = new ArrayList<>();
    private List<String> electricList = new ArrayList<>();

    private SharedPreferences preferences;
    public static String token;
    private BarChartViewPager mBarChart;
    private LineChartViewPager mLineChart;
    private PieChartViewPager mPieChart;
    private LinearLayout mPieLayout;
    private TextView textView1;

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

    private RecyclerView recyclerView;
    private ElectricRecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private List<ElectricBean> electricBeanList = new ArrayList<>();
    private String powerUnit;
    private BarChartManager manager3;
    private String radio;
    private LineChartManager manager1;
    private List<String> dateXList;
    private PieChartManager manager2;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout layout;
    private LinearLayout errorLayout;

    private XYMarkerView barMarkerView;
    private XYMarkerView lineMarkerView;
    private XYMarkerView pieMarerView;


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
        layout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);
        powerUnit = preferences.getString("power_unit", null);

        mPieLayout = view.findViewById(R.id.electric_fragment_pie_layout);
        textView1 = view.findViewById(R.id.electric_fragment_chart_text);

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

        recyclerView = view.findViewById(R.id.electric_fragment_recyclerView);
        adapter = new ElectricRecyclerAdapter(getContext(), electricBeanList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        layout = view.findViewById(R.id.electric_fragment_layout);
        errorLayout = view.findViewById(R.id.electric_fragment_error_layout);

        refreshLayout = view.findViewById(R.id.electric_fragment_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });

        mBarChart = view.findViewById(R.id.electric_fragment_chart);
        manager3 = new BarChartManager(mBarChart);
        manager3.setDescription("");
        manager3.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (date_type.equals("year")) {
                    return (int) value + "月";
                } else {
                    return (int) value + "日";
                }
            }
        });
        manager3.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (type.equals("original")) {
                    return (int) Math.ceil(value) + " " + radio + powerUnit;
                } else {
                    return (int) Math.ceil(value) + " tce";
                }

            }
        });


        mLineChart = view.findViewById(R.id.electric_fragment_line);
        manager1 = new LineChartManager(mLineChart);
        manager1.setDescription("");
        manager1.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (dateXList != null && dateXList.size() == 288) {
                    return dateXList.get((int) value);
                }
                return (int) value + "";
            }
        });
        manager1.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (type.equals("original")) {
                    return (int) Math.ceil(value) + " " + radio + powerUnit;
                } else {
                    return (int) Math.ceil(value) + " tce";
                }

            }
        });


        mPieChart = view.findViewById(R.id.electric_fragment_pie);
        manager2 = new PieChartManager(mPieChart);

        barMarkerView = new XYMarkerView(getContext());
        barMarkerView.setChartView(mBarChart);
        mBarChart.setMarker(barMarkerView);

        lineMarkerView = new XYMarkerView(getContext());
        lineMarkerView.setChartView(mLineChart);
        mLineChart.setMarker(lineMarkerView);

        pieMarerView = new XYMarkerView(getContext());
        pieMarerView.setChartView(mPieChart);
        mPieChart.setMarker(pieMarerView);
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
                    sendRequest();
                }
                dateDialog.cancel();
            }
        });
        yearDialog.setOnOkClickListener(new DateDialog.OnOkClickListener() {
            @Override
            public void OnClick(View view, String date) {
                if (dateText != null && date != null) {
                    dateText.setText(date);
                    sendRequest();
                }
                yearDialog.cancel();
            }
        });
        monthDialog.setOnOkClickListener(new DateDialog.OnOkClickListener() {
            @Override
            public void OnClick(View view, String date) {
                if (dateText != null && date != null) {
                    dateText.setText(date);
                    sendRequest();
                }
                monthDialog.cancel();
            }
        });


        barMarkerView.setBarOnClickListener(new BarOnClickListener() {
            @Override
            public void onClick(Entry e, Highlight highlight, View v) {
                if (v instanceof TextView) {
                    int position = (int) e.getX() - 1;
                    StringBuffer content = new StringBuffer();
                    if (isNoData(position)) {
                        v.setVisibility(View.GONE);
                    } else {
                        v.setVisibility(View.VISIBLE);
                    }
                    if (e.getX() < 10) {
                        content.append("时间:" + dateText.getText().toString() + "-0" + (int) e.getX() + "\r\n");
                    } else {
                        content.append("时间:" + dateText.getText().toString() + "-" + (int) e.getX() + "\r\n");
                    }
                    if (type.equals("original")) {
                        if (rush.get(position) != null) {
                            content.append("尖:" + rush.get(position) + radio + powerUnit + "\r\n");
                        }
                        if (peak.get(position) != null) {
                            content.append("峰:" + peak.get(position) + radio + powerUnit + "\r\n");
                        }
                        if (normal.get(position) != null) {
                            content.append("平:" + normal.get(position) + radio + powerUnit + "\r\n");
                        }
                        if (valley.get(position) != null) {
                            content.append("谷:" + valley.get(position) + radio + powerUnit);
                        }
                    } else {
                        if (rush.get(position) != null) {
                            content.append("尖:" + rush.get(position) + "tce" + "\r\n");
                        }
                        if (peak.get(position) != null) {
                            content.append("峰:" + peak.get(position) + "tce" + "\r\n");
                        }
                        if (normal.get(position) != null) {
                            content.append("平:" + normal.get(position) + "tce" + "\r\n");
                        }
                        if (valley.get(position) != null) {
                            content.append("谷:" + valley.get(position) + "tce");
                        }
                    }

                    ((TextView) v).setText(content.toString());
                }
            }
        });

        lineMarkerView.setBarOnClickListener(new BarOnClickListener() {
            @Override
            public void onClick(Entry e, Highlight highlight, View v) {
                int position = (int) e.getX();
                if (electricList.size() == 288 && electricList.get(position) != null) {
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.setVisibility(View.GONE);
                }
                StringBuffer content = new StringBuffer();
                if (dateXList != null && dateXList.size() == 288) {
                    content.append("时间:" + dateXList.get(position) + "\r\n");
                }
                if (type.equals("original")) {
                    if (electricList.size() == 288 && electricList.get(position) != null) {
                        content.append("电量:" + electricList.get(position) + radio + powerUnit);
                    }
                } else {
                    if (electricList.size() == 288 && electricList.get(position) != null) {
                        content.append("电量:" + electricList.get(position) + "tce");
                    }
                }
                ((TextView) v).setText(content.toString());
            }
        });

        pieMarerView.setBarOnClickListener(new BarOnClickListener() {
            @Override
            public void onClick(Entry e, Highlight highlight, View v) {
                StringBuffer content = new StringBuffer();
                if (type.equals("original")) {
                    content.append("当日能耗:" + e.getY() + radio + powerUnit);
                } else {
                    content.append("当日能耗:" + e.getY() + "tce");
                }
                ((TextView) v).setText(content.toString());
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
                sendRequest();
                break;
            case R.id.electric_fragment_month_button:
                long time2 = System.currentTimeMillis();
                String date2 = Util.parseTime(time2, 2);
                dateText.setText(date2);
                sendRequest();
                break;
            case R.id.electric_fragment_day_button:
                long time3 = System.currentTimeMillis();
                String date3 = Util.parseTime(time3, 3);
                dateText.setText(date3);
                sendRequest();
                break;

            case R.id.electric_fragment_original_button:
                sendRequest();
                break;
            case R.id.electric_fragment_fold_button:
                sendRequest();
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
            date_type = "date";
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
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(ELECTRIC_URL + token + "&date_type=" + date_type + "&type=" + type
                + "&time=" + time, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        layout.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (date_type.equals("date")) {
                                DayElectricInfo electricInfo = Util.handleDayElectricInfo(content);
                                if (electricInfo != null && electricInfo.getError() == null) {
                                    parseElectricForLine(electricInfo);
                                    refreshLayout.setRefreshing(false);
                                    layout.setVisibility(View.VISIBLE);
                                    errorLayout.setVisibility(View.GONE);
                                } else {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.putExtra("token_timeout", "登录超时");
                                    preferences.edit().putString("token", null).commit();
                                    startActivity(intent);
                                    ActivityCollector.finishAllActivity();
                                }
                            } else {
                                ElectricInfo electricInfo = Util.handleElectricInfo(content);
                                if (electricInfo != null && electricInfo.getError() == null) {
                                    parseElectricForBar(electricInfo);
                                    refreshLayout.setRefreshing(false);
                                    layout.setVisibility(View.VISIBLE);
                                    errorLayout.setVisibility(View.GONE);
                                } else {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.putExtra("token_timeout", "登录超时");
                                    preferences.edit().putString("token", null).commit();
                                    startActivity(intent);
                                    ActivityCollector.finishAllActivity();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 当选择时间为年月时
     *
     * @param electricInfo
     */
    private void parseElectricForBar(ElectricInfo electricInfo) {
        mBarChart.setVisibility(View.VISIBLE);
        mLineChart.setVisibility(View.GONE);
        mPieChart.setVisibility(View.GONE);
        mPieLayout.setVisibility(View.GONE);
        radio = electricInfo.getChart_ratio();


        if (electricInfo.getChart() != null) {
            valley = electricInfo.getChart().getValley();
            normal = electricInfo.getChart().getNormal();
            peak = electricInfo.getChart().getPeak();
            rush = electricInfo.getChart().getRush();

            //设置y轴的数据()
            float[][] values = new float[electricInfo.getChart().getValley().size()][4];
            for (int i = 0; i < electricInfo.getChart().getValley().size(); i++) {
                String valley = electricInfo.getChart().getValley().get(i);
                if (valley != null) {
                    values[i][0] = Float.valueOf(valley);
                }
            }
            for (int i = 0; i < electricInfo.getChart().getNormal().size(); i++) {
                String normal = electricInfo.getChart().getNormal().get(i);
                if (normal != null) {
                    values[i][1] = Float.valueOf(normal);
                }
            }
            for (int i = 0; i < electricInfo.getChart().getPeak().size(); i++) {
                String peak = electricInfo.getChart().getPeak().get(i);
                if (peak != null) {
                    values[i][2] = Float.valueOf(peak);
                }
            }
            for (int i = 0; i < electricInfo.getChart().getRush().size(); i++) {
                String rush = electricInfo.getChart().getRush().get(i);
                if (rush != null) {
                    values[i][3] = Float.valueOf(rush);
                }
            }
            manager3.showBarChart(values, colors);
        }

        if (date_type.equals("year")) {
            textView1.setText(dateText.getText().toString() + "年 区间用电量柱状图");
        } else {
            textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                    + dateText.getText().toString().split("-")[1]
                    + "月 区间用电量柱状图");
        }

        //显示列表信息
        electricBeanList.clear();
        if (electricInfo.getTable_data() != null) {
            for (ElectricInfo.TableDataBean tableDataBean : electricInfo.getTable_data()) {
                ElectricBean electricBean = new ElectricBean();
                electricBean.setTime(tableDataBean.getTime() + "");
                electricBean.setRush(tableDataBean.getRush() + "");
                electricBean.setPeak(tableDataBean.getPeak() + "");
                electricBean.setNormal(tableDataBean.getNormal() + "");
                electricBean.setValley(tableDataBean.getValley() + "");
                electricBean.setTotal(tableDataBean.getTotal() + "");
                electricBean.setHistory_average(tableDataBean.getHistory_average() + "");
                electricBean.setRange(tableDataBean.getRange() + "");
                if (type.equals("original")) {
                    electricBean.setRatio(electricInfo.getRatio() + powerUnit + "");
                } else {
                    electricBean.setRatio("tce");
                }
                electricBeanList.add(electricBean);
            }
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 当选择时间为日时
     *
     * @param electricInfo
     */
    private void parseElectricForLine(DayElectricInfo electricInfo) {
        mBarChart.setVisibility(View.GONE);
        mLineChart.setVisibility(View.VISIBLE);
        mPieChart.setVisibility(View.VISIBLE);
        mPieLayout.setVisibility(View.VISIBLE);

        radio = electricInfo.getChart_ratio();

        dateXList = electricInfo.getX_name();
        if (electricInfo.getChart() != null) {
            electricList = electricInfo.getChart();
            //设置x轴的数据
            ArrayList<Float> xValues = new ArrayList<>();
            for (int i = 0; i < 288; i++) {
                xValues.add((float) i);
            }
            //设置y轴的数据()
            List<Float> yValue = new ArrayList<>();
            for (int i = 0; i < electricInfo.getChart().size(); i++) {
                if (electricInfo.getChart().get(i) != null) {
                    yValue.add(Float.valueOf(electricInfo.getChart().get(i)));
                } else {
                    yValue.add(0f);
                }
            }
            manager1.showLineChart(xValues, yValue, "小时", Color.RED);
        }

        textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                + dateText.getText().toString().split("-")[1]
                + "月"
                + dateText.getText().toString().split("-")[2]
                + "日 区间用电量曲线图");


        if (electricInfo.getPeak_valley_pie() != null) {
            List<DayElectricInfo.PeakValleyPieBean> peakList = electricInfo.getPeak_valley_pie();
            //设置饼图数据
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            if (peakList.size() == 3) {
                entries.add(new PieEntry(Float.valueOf(peakList.get(2).getValue()), peakList.get(2).getName()));
                entries.add(new PieEntry(Float.valueOf(peakList.get(1).getValue()), peakList.get(1).getName()));
                entries.add(new PieEntry(Float.valueOf(peakList.get(0).getValue()), peakList.get(0).getName()));
            } else if (peakList.size() == 4) {
                entries.add(new PieEntry(Float.valueOf(peakList.get(3).getValue()), peakList.get(3).getName()));
                entries.add(new PieEntry(Float.valueOf(peakList.get(2).getValue()), peakList.get(2).getName()));
                entries.add(new PieEntry(Float.valueOf(peakList.get(1).getValue()), peakList.get(1).getName()));
                entries.add(new PieEntry(Float.valueOf(peakList.get(0).getValue()), peakList.get(0).getName()));
            }
            manager2.showPieChart(entries, colors);
        }


        electricBeanList.clear();
        if (electricInfo.getTable_data() != null) {
            for (DayElectricInfo.TableDataBean tableDataBean : electricInfo.getTable_data()) {
                ElectricBean electricBean = new ElectricBean();
                electricBean.setTime(tableDataBean.getTime() + "");
                electricBean.setTotal(tableDataBean.getValue() + "");
                if (type.equals("original")) {
                    electricBean.setRatio(electricInfo.getTable_ratio() + powerUnit + "");
                } else {
                    electricBean.setRatio("tce");
                }
                electricBeanList.add(electricBean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 判断是否有数据
     *
     * @param position
     * @return
     */
    private boolean isNoData(int position) {
        if (peak.get(position) == null && normal.get(position) == null && valley.get(position) == null
                && rush.get(position) == null) {
            return true;
        }
        return false;
    }
}
