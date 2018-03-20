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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.RenewableRecyclerAdapter;
import cn.com.larunda.monitor.bean.GasBean;
import cn.com.larunda.monitor.bean.RenewableBean;
import cn.com.larunda.monitor.gson.DayRenewableInfo;
import cn.com.larunda.monitor.gson.GasInfo;
import cn.com.larunda.monitor.gson.RenewableInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.LineChartViewPager;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.PieChartViewPager;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sddt on 18-3-20.
 */

public class RenewableFragment extends Fragment implements View.OnClickListener {

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
        ratio = renewableInfo.getTable_ratio();
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

        ratio = renewableInfo.getRatio();
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
