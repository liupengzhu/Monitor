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
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.RenewableRecyclerAdapter;
import cn.com.larunda.monitor.bean.RenewableBean;
import cn.com.larunda.monitor.gson.DayElectricInfo;
import cn.com.larunda.monitor.gson.DayRenewableInfo;
import cn.com.larunda.monitor.gson.RenewableInfo;
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
 * Created by sddt on 18-3-20.
 */

public class RenewableFragment extends Fragment implements View.OnClickListener {
    private int[] colors = {MyApplication.getContext().getResources().getColor(R.color.valley_color),
            MyApplication.getContext().getResources().getColor(R.color.normal_color),
            MyApplication.getContext().getResources().getColor(R.color.peak_color),
            MyApplication.getContext().getResources().getColor(R.color.rush_color)};

    private final String RENEWABLE_URL = MyApplication.URL + "renewable/usage" + MyApplication.TOKEN;
    private String date_type = "month";

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

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout layout;
    private LinearLayout errorLayout;
    private String powerUnit;

    private RecyclerView recyclerView;
    private RenewableRecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private List<RenewableBean> renewableBeanList = new ArrayList<>();
    private String ratio;

    private XYMarkerView barMarkerView;
    private XYMarkerView lineMarkerView;
    private XYMarkerView pieMarkerView;
    private BarChartManager barChartManager;
    private LineChartManager lineChartManager;
    private PieChartManager pieChartManager;
    private List<String> dateXList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_renewable, container, false);
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

        mPieLayout = view.findViewById(R.id.renewable_fragment_pie_layout);
        textView1 = view.findViewById(R.id.renewable_fragment_chart_text);

        dateDialog = new DateDialog(getContext());
        yearDialog = new DateDialog(getContext(), false, false);
        monthDialog = new DateDialog(getContext(), true, false);
        dateText = view.findViewById(R.id.renewable_date_text);
        yearButton = view.findViewById(R.id.renewable_fragment_year_button);
        monthButton = view.findViewById(R.id.renewable_fragment_month_button);
        dayButton = view.findViewById(R.id.renewable_fragment_day_button);
        timeGroup = view.findViewById(R.id.renewable_fragment_time_group);

        layout = view.findViewById(R.id.renewable_fragment_layout);
        errorLayout = view.findViewById(R.id.renewable_fragment_error_layout);

        refreshLayout = view.findViewById(R.id.renewable_fragment_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });

        recyclerView = view.findViewById(R.id.renewable_fragment_recyclerView);
        adapter = new RenewableRecyclerAdapter(getContext(), renewableBeanList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        mBarChart = view.findViewById(R.id.renewable_fragment_chart);
        barChartManager = new BarChartManager(mBarChart);
        barChartManager.setDescription("");
        barChartManager.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (date_type.equals("year")) {
                    return (int) value + "月";
                } else {
                    return (int) value + "日";
                }
            }
        });
        barChartManager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) Math.ceil(value) + " " + ratio + powerUnit;
            }
        });


        mLineChart = view.findViewById(R.id.renewable_fragment_line);
        lineChartManager = new LineChartManager(mLineChart);
        lineChartManager.setDescription("");
        lineChartManager.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (dateXList != null && dateXList.size() == 288) {
                    return dateXList.get((int) value);
                }
                return (int) value + "";
            }
        });
        lineChartManager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) Math.ceil(value) + " " + ratio + powerUnit;
            }
        });


        mPieChart = view.findViewById(R.id.renewable_fragment_pie);
        pieChartManager = new PieChartManager(mPieChart);

        barMarkerView = new XYMarkerView(getContext());
        barMarkerView.setChartView(mBarChart);
        mBarChart.setMarker(barMarkerView);

        lineMarkerView = new XYMarkerView(getContext());
        lineMarkerView.setChartView(mLineChart);
        mLineChart.setMarker(lineMarkerView);

        pieMarkerView = new XYMarkerView(getContext());
        pieMarkerView.setChartView(mPieChart);
        mPieChart.setMarker(pieMarkerView);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        long time = System.currentTimeMillis();
        String date;
        if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_fragment_year_button) {
            date = Util.parseTime(time, 1);
        } else if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_fragment_day_button) {
            date = Util.parseTime(time, 3);
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
        dayButton.setOnClickListener(this);
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
                    content.append("发电量:" + e.getY() + ratio + powerUnit + "");
                    ((TextView) v).setText(content.toString());
                }
            }
        });

        lineMarkerView.setBarOnClickListener(new BarOnClickListener() {
            @Override
            public void onClick(Entry e, Highlight highlight, View v) {
                int position = (int) e.getX();
                StringBuffer content = new StringBuffer();
                if (e.getY() > 0) {
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.setVisibility(View.GONE);
                }
                if (dateXList != null && dateXList.size() == 288) {
                    content.append("时间:" + dateXList.get(position) + "\r\n");
                }
                content.append("发电量:" + e.getY() + ratio + powerUnit + "");
                ((TextView) v).setText(content.toString());
            }
        });

        pieMarkerView.setBarOnClickListener(new BarOnClickListener() {
            @Override
            public void onClick(Entry e, Highlight highlight, View v) {
                StringBuffer content = new StringBuffer();
                content.append("当日发电量:" + e.getY() + ratio + powerUnit);
                ((TextView) v).setText(content.toString());
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
        HttpUtil.sendGetRequestWithHttp(RENEWABLE_URL + token + "&date_type=" + date_type
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
                                if (date_type.equals("date")) {
                                    DayRenewableInfo renewableInfo = Util.handleDayRenewableInfo(content);
                                    if (renewableInfo != null && renewableInfo.getError() == null) {
                                        parseRenewableForLine(renewableInfo);
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
                                    RenewableInfo renewableInfo = Util.handleRenewableInfo(content);
                                    if (renewableInfo != null && renewableInfo.getError() == null) {
                                        parseRenewableForBar(renewableInfo);
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
            }
        });
    }

    /**
     * 解析服务器返回日数据
     *
     * @param renewableInfo
     */
    private void parseRenewableForLine(DayRenewableInfo renewableInfo) {
        mBarChart.setVisibility(View.GONE);
        mLineChart.setVisibility(View.VISIBLE);
        mPieChart.setVisibility(View.VISIBLE);
        mPieLayout.setVisibility(View.VISIBLE);
        ratio = renewableInfo.getTable_ratio();

        dateXList = renewableInfo.getX_name();
        if (renewableInfo.getChart() != null) {
            //设置x轴的数据
            ArrayList<Float> xValues = new ArrayList<>();
            for (int i = 0; i < 288; i++) {
                xValues.add((float) i);
            }
            //设置y轴的数据()
            List<Float> yValue = new ArrayList<>();
            for (int i = 0; i < renewableInfo.getChart().size(); i++) {
                if (renewableInfo.getChart().get(i) != null) {
                    yValue.add(Float.valueOf(renewableInfo.getChart().get(i)));
                } else {
                    yValue.add(0f);
                }
            }
            lineChartManager.showLineChart(xValues, yValue, "小时", Color.RED);
        }

        textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                + dateText.getText().toString().split("-")[1]
                + "月"
                + dateText.getText().toString().split("-")[2]
                + "日 区间发电量曲线图");


        if (renewableInfo.getPeak_valley_pie() != null) {
            List<DayRenewableInfo.PeakValleyPieBean> peakList = renewableInfo.getPeak_valley_pie();
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
            pieChartManager.showPieChart(entries, colors);
        }

        renewableBeanList.clear();
        if (renewableInfo.getTable_data() != null) {
            for (DayRenewableInfo.TableDataBean bean : renewableInfo.getTable_data()) {
                RenewableBean renewableBean = new RenewableBean();
                renewableBean.setTime(bean.getTime() + "");
                renewableBean.setTotal(bean.getValue() + "");
                renewableBean.setRatio(ratio + powerUnit + "");
                renewableBeanList.add(renewableBean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 解析服务器返回数据
     *
     * @param renewableInfo
     */
    private void parseRenewableForBar(RenewableInfo renewableInfo) {
        mBarChart.setVisibility(View.VISIBLE);
        mLineChart.setVisibility(View.GONE);
        mPieChart.setVisibility(View.GONE);
        mPieLayout.setVisibility(View.GONE);
        ratio = renewableInfo.getRatio();

        if (renewableInfo.getChart() != null) {
            float values[] = new float[renewableInfo.getChart().size()];
            for (int i = 0; i < renewableInfo.getChart().size(); i++) {
                String value = renewableInfo.getChart().get(i);
                if (value != null) {
                    values[i] = Float.valueOf(value);
                }
            }
            barChartManager.showBarChart(values, "", getResources().getColor(R.color.water_color));
        }

        if (date_type.equals("year")) {
            textView1.setText(dateText.getText().toString() + "年 区间发电量柱状图");
        } else {
            textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                    + dateText.getText().toString().split("-")[1]
                    + "月 区间发电量柱状图");
        }

        renewableBeanList.clear();
        if (renewableInfo.getTable_data() != null) {
            for (RenewableInfo.TableDataBean bean : renewableInfo.getTable_data()) {
                RenewableBean renewableBean = new RenewableBean();
                renewableBean.setTime(bean.getTime() + "");
                renewableBean.setTotal(bean.getData() + "");
                renewableBean.setHistory_average(bean.getHistory_average() + "");
                renewableBean.setRange(bean.getRange() + "");
                renewableBean.setRatio(ratio + powerUnit + "");
                renewableBeanList.add(renewableBean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.renewable_fragment_year_button:
                long time1 = System.currentTimeMillis();
                String date1 = Util.parseTime(time1, 1);
                dateText.setText(date1);
                sendRequest();
                break;
            case R.id.renewable_fragment_month_button:
                long time2 = System.currentTimeMillis();
                String date2 = Util.parseTime(time2, 2);
                dateText.setText(date2);
                sendRequest();
                break;
            case R.id.renewable_fragment_day_button:
                long time3 = System.currentTimeMillis();
                String date3 = Util.parseTime(time3, 3);
                dateText.setText(date3);
                sendRequest();
                break;

            case R.id.renewable_date_text:
                if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_fragment_year_button) {
                    yearDialog.show();
                } else if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_fragment_month_button) {
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

        if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_fragment_year_button) {
            date_type = "year";
        } else if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_fragment_day_button) {
            date_type = "date";
        } else {
            date_type = "month";
        }
    }
}
