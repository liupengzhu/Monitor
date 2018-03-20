package cn.com.larunda.monitor.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.github.mikephil.charting.highlight.Highlight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.ElectricRecyclerAdapter;
import cn.com.larunda.monitor.adapter.WaterRecyclerAdapter;
import cn.com.larunda.monitor.bean.WaterBean;
import cn.com.larunda.monitor.gson.DayElectricInfo;
import cn.com.larunda.monitor.gson.ElectricInfo;
import cn.com.larunda.monitor.gson.WaterInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BarChartManager;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.BarOnClickListener;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import cn.com.larunda.monitor.util.XValueFormatter;
import cn.com.larunda.monitor.util.XYMarkerView;
import cn.com.larunda.monitor.util.YValueFormatter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sddt on 18-3-19.
 */

public class WaterFragment extends Fragment implements View.OnClickListener {

    private final String WATER_URL = MyApplication.URL + "water/usage" + MyApplication.TOKEN;
    private String date_type = "month";
    private String type = "original";

    private SharedPreferences preferences;
    public static String token;
    private BarChartViewPager mBarChart;
    private TextView textView1;

    private DateDialog yearDialog;
    private DateDialog monthDialog;
    private TextView dateText;
    private RadioButton yearButton;
    private RadioButton monthButton;
    private RadioGroup timeGroup;

    private RadioGroup typeGroup;
    private RadioButton originalButton;
    private RadioButton foldButton;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout layout;
    private LinearLayout errorLayout;

    private RecyclerView recyclerView;
    private WaterRecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private List<WaterBean> waterBeanList = new ArrayList<>();
    private BarChartManager barManager;
    private XYMarkerView barMarkerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water, container, false);
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
        textView1 = view.findViewById(R.id.water_fragment_chart_text);
        yearDialog = new DateDialog(getContext(), false, false);
        monthDialog = new DateDialog(getContext(), true, false);
        dateText = view.findViewById(R.id.water_date_text);
        yearButton = view.findViewById(R.id.water_fragment_year_button);
        monthButton = view.findViewById(R.id.water_fragment_month_button);
        timeGroup = view.findViewById(R.id.water_fragment_time_group);

        typeGroup = view.findViewById(R.id.water_fragment_type_group);
        originalButton = view.findViewById(R.id.water_fragment_original_button);
        foldButton = view.findViewById(R.id.water_fragment_fold_button);

        layout = view.findViewById(R.id.water_fragment_layout);
        errorLayout = view.findViewById(R.id.water_fragment_error_layout);

        refreshLayout = view.findViewById(R.id.water_fragment_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });

        recyclerView = view.findViewById(R.id.water_fragment_recyclerView);
        adapter = new WaterRecyclerAdapter(getContext(), waterBeanList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        mBarChart = view.findViewById(R.id.water_fragment_chart);
        barManager = new BarChartManager(mBarChart);
        barManager.setDescription("");
        barManager.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (date_type.equals("year")) {
                    return (int) value + "月";
                } else {
                    return (int) value + "日";
                }
            }
        });
        barManager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (type.equals("original")) {
                    return (int) Math.ceil(value) + " " + preferences.getString("water_unit", null) + "";
                } else {
                    return (int) Math.ceil(value) + " tce";
                }

            }
        });

        barMarkerView = new XYMarkerView(getContext());
        barMarkerView.setChartView(mBarChart);
        mBarChart.setMarker(barMarkerView);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        long time = System.currentTimeMillis();
        String date;
        if (timeGroup.getCheckedRadioButtonId() == R.id.water_fragment_year_button) {
            date = Util.parseTime(time, 1);
        } else {
            date = Util.parseTime(time, 2);
        }
        dateText.setText(date);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        yearButton.setOnClickListener(this);
        monthButton.setOnClickListener(this);

        originalButton.setOnClickListener(this);
        foldButton.setOnClickListener(this);

        dateText.setOnClickListener(this);

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
                    StringBuffer content = new StringBuffer();
                    if (e.getY() > 0) {
                        v.setVisibility(View.VISIBLE);
                    } else {
                        v.setVisibility(View.GONE);
                    }
                    if (e.getX() < 10) {
                        content.append("时间:" + dateText.getText().toString() + "-0" + (int) e.getX() + "\r\n");
                    } else {
                        content.append("时间:" + dateText.getText().toString() + "-" + (int) e.getX() + "\r\n");
                    }
                    if (type.equals("original")) {

                        content.append("用水量:" + e.getY() + preferences.getString("water_unit", null) + "");
                    } else {
                        content.append("用水量:" + e.getY() + "tce");
                    }

                    ((TextView) v).setText(content.toString());
                }
            }
        });
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
        getType();
        String time = dateText.getText().toString().trim();
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(WATER_URL + token + "&date_type=" + date_type + "&type=" + type
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
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WaterInfo waterInfo = Util.handleWaterInfo(content);
                                if (waterInfo != null && waterInfo.getError() == null) {
                                    parseWater(waterInfo);
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
                        });
                    }
                }
            }
        });
    }

    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.water_fragment_year_button:
                long time1 = System.currentTimeMillis();
                String date1 = Util.parseTime(time1, 1);
                dateText.setText(date1);
                sendRequest();
                break;
            case R.id.water_fragment_month_button:
                long time2 = System.currentTimeMillis();
                String date2 = Util.parseTime(time2, 2);
                dateText.setText(date2);
                sendRequest();
                break;

            case R.id.water_fragment_original_button:
                sendRequest();
                break;
            case R.id.water_fragment_fold_button:
                sendRequest();
                break;
            case R.id.water_date_text:
                if (timeGroup.getCheckedRadioButtonId() == R.id.water_fragment_year_button) {
                    yearDialog.show();
                } else if (timeGroup.getCheckedRadioButtonId() == R.id.water_fragment_month_button) {
                    monthDialog.show();
                }
                break;

        }
    }

    /**
     * 获取当前按钮组状态
     */
    private void getType() {
        if (typeGroup.getCheckedRadioButtonId() == R.id.water_fragment_fold_button) {
            type = "fold";
        } else {
            type = "original";
        }
        if (timeGroup.getCheckedRadioButtonId() == R.id.water_fragment_year_button) {
            date_type = "year";
        } else {
            date_type = "month";
        }
    }

    /**
     * 解析服务器返回数据
     *
     * @param waterInfo
     */
    private void parseWater(WaterInfo waterInfo) {

        if (waterInfo.getChart() != null) {
            float values[] = new float[waterInfo.getChart().size()];
            for (int i = 0; i < waterInfo.getChart().size(); i++) {
                String value = waterInfo.getChart().get(i);
                if (value != null) {
                    values[i] = Float.valueOf(value);
                }
            }
            barManager.showBarChart(values, "", getResources().getColor(R.color.water_color));
        }

        if (date_type.equals("year")) {
            textView1.setText(dateText.getText().toString() + "年 区间用水量柱状图");
        } else {
            textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                    + dateText.getText().toString().split("-")[1]
                    + "月 区间用水量柱状图");
        }

        waterBeanList.clear();
        if (waterInfo.getTable_data() != null) {
            for (WaterInfo.TableDataBean bean : waterInfo.getTable_data()) {
                WaterBean waterBean = new WaterBean();
                waterBean.setTime(bean.getTime() + "");
                waterBean.setTotal(bean.getData() + "");
                waterBean.setHistory_average(bean.getHistory_average() + "");
                waterBean.setRange(bean.getRange() + "");
                if (type.equals("original")) {
                    waterBean.setRatio(preferences.getString("water_unit", null) + "");
                } else {
                    waterBean.setRatio("tce");
                }
                waterBeanList.add(waterBean);
            }
        }
        adapter.notifyDataSetChanged();
    }


}
