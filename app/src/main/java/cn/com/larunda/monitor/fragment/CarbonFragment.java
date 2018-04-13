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
import cn.com.larunda.monitor.adapter.CarbonRecyclerAdapter;
import cn.com.larunda.monitor.bean.CarbonBean;
import cn.com.larunda.monitor.gson.CarbonInfo;
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
 * Created by sddt on 18-3-20.
 */

public class CarbonFragment extends Fragment implements View.OnClickListener {
    private final String CARBON_URL = MyApplication.URL + "carbon/data" + MyApplication.TOKEN;
    private String date_type = "month";
    private String type = "usage";

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

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout layout;
    private LinearLayout errorLayout;

    private RecyclerView recyclerView;
    private CarbonRecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private List<CarbonBean> carbonBeanList = new ArrayList<>();

    private BarChartManager barManager;
    private XYMarkerView barMarkerView;
    private TextView bgText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carbon, container, false);
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
        bgText = view.findViewById(R.id.carbon_fragment_bg);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);
        textView1 = view.findViewById(R.id.carbon_fragment_chart_text);
        yearDialog = new DateDialog(getContext(), false, false);
        monthDialog = new DateDialog(getContext(), true, false);
        dateText = view.findViewById(R.id.carbon_date_text);
        yearButton = view.findViewById(R.id.carbon_fragment_year_button);
        monthButton = view.findViewById(R.id.carbon_fragment_month_button);
        timeGroup = view.findViewById(R.id.carbon_fragment_time_group);

        layout = view.findViewById(R.id.carbon_fragment_layout);
        errorLayout = view.findViewById(R.id.carbon_fragment_error_layout);

        refreshLayout = view.findViewById(R.id.carbon_fragment_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });

        recyclerView = view.findViewById(R.id.carbon_fragment_recyclerView);
        adapter = new CarbonRecyclerAdapter(getContext(), carbonBeanList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        mBarChart = view.findViewById(R.id.carbon_fragment_chart);
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
                return Util.formatNum(value) + " " + preferences.getString("carbon_unit", null) + "";
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
        if (timeGroup.getCheckedRadioButtonId() == R.id.carbon_fragment_year_button) {
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
                    content.append("排放量:" + Util.formatNum(e.getY()) + preferences.getString("carbon_unit", null) + "");

                    ((TextView) v).setText(content.toString());
                }
            }
        });

        errorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    /**
     * 发送网络数据
     */
    private void sendRequest() {

        getType();
        String time = dateText.getText().toString().trim();
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(CARBON_URL + token + "&date_type=" + date_type + "&type=" + type
                + "&time=" + time, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                            layout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CarbonInfo carbonInfo = Util.handleCarbonInfo(content);
                                if (carbonInfo != null && carbonInfo.getError() == null) {
                                    parseCarbon(carbonInfo);
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
     * 解析服务器返回数据
     *
     * @param carbonInfo
     */
    private void parseCarbon(CarbonInfo carbonInfo) {

        if (carbonInfo.getChart() != null) {
            if (carbonInfo.getChart().size() > 0) {
                bgText.setVisibility(View.GONE);
            } else {
                bgText.setVisibility(View.VISIBLE);
            }
            float values[] = new float[carbonInfo.getChart().size()];
            for (int i = 0; i < carbonInfo.getChart().size(); i++) {
                String value = carbonInfo.getChart().get(i);
                if (value != null) {
                    values[i] = Float.valueOf(value);
                }
            }
            barManager.showBarChart(values, "", getResources().getColor(R.color.water_color));
        }

        if (date_type.equals("year")) {
            textView1.setText(dateText.getText().toString() + "年 二氧化碳排放量柱状图");
        } else {
            textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                    + dateText.getText().toString().split("-")[1]
                    + "月 二氧化碳排放量柱状图");
        }

        carbonBeanList.clear();
        if (carbonInfo.getTable_data() != null) {
            for (CarbonInfo.TableDataBean bean : carbonInfo.getTable_data()) {
                CarbonBean carbonBean = new CarbonBean();
                carbonBean.setTime(bean.getTime() + "");
                carbonBean.setTotal(bean.getData() + "");
                carbonBean.setHistory_average(bean.getHistory_average() + "");
                carbonBean.setRange(bean.getRange() + "");
                carbonBean.setRatio(preferences.getString("carbon_unit", null) + "");
                carbonBeanList.add(carbonBean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carbon_fragment_year_button:
                long time1 = System.currentTimeMillis();
                String date1 = Util.parseTime(time1, 1);
                dateText.setText(date1);
                sendRequest();
                break;
            case R.id.carbon_fragment_month_button:
                long time2 = System.currentTimeMillis();
                String date2 = Util.parseTime(time2, 2);
                dateText.setText(date2);
                sendRequest();
                break;

            case R.id.carbon_date_text:
                if (timeGroup.getCheckedRadioButtonId() == R.id.carbon_fragment_year_button) {
                    yearDialog.show();
                } else if (timeGroup.getCheckedRadioButtonId() == R.id.carbon_fragment_month_button) {
                    monthDialog.show();
                }
                break;
        }
    }

    /**
     * 获取当前按钮组状态
     */
    private void getType() {

        if (timeGroup.getCheckedRadioButtonId() == R.id.carbon_fragment_year_button) {
            date_type = "year";
        } else {
            date_type = "month";
        }
    }
}
