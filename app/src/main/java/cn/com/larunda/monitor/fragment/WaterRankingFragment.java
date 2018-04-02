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

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.WaterRankingRecyclerAdapter;
import cn.com.larunda.monitor.bean.WaterRankingBean;
import cn.com.larunda.monitor.gson.RankCompanyInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BarOnClickListener;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.PieChartManager;
import cn.com.larunda.monitor.util.PieChartViewPager;
import cn.com.larunda.monitor.util.Util;
import cn.com.larunda.monitor.util.XYMarkerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sddt on 18-3-19.
 */

public class WaterRankingFragment extends Fragment implements View.OnClickListener {
    private int[] colors = {MyApplication.getContext().getResources().getColor(R.color.rank_color1),
            MyApplication.getContext().getResources().getColor(R.color.rank_color2),
            MyApplication.getContext().getResources().getColor(R.color.rank_color3),
            MyApplication.getContext().getResources().getColor(R.color.rank_color4),
            MyApplication.getContext().getResources().getColor(R.color.rank_color5),
            MyApplication.getContext().getResources().getColor(R.color.rank_color6),
            MyApplication.getContext().getResources().getColor(R.color.rank_color7),
            MyApplication.getContext().getResources().getColor(R.color.rank_color8),
            MyApplication.getContext().getResources().getColor(R.color.rank_color9),
            MyApplication.getContext().getResources().getColor(R.color.rank_color10),
            MyApplication.getContext().getResources().getColor(R.color.rank_color11)};
    private final String COMPANY_URL = MyApplication.URL + "water/rank_company" + MyApplication.TOKEN;
    private final String INDUSTRY_URL = MyApplication.URL + "water/rank_industry" + MyApplication.TOKEN;

    private String date_type = "month";

    private String type = "original";
    private String style = "company";
    private SharedPreferences preferences;
    public static String token;
    private String waterUnit;

    private PieChartViewPager mPieChart;
    private TextView textView1;
    private DateDialog dateDialog;
    private DateDialog monthDialog;
    private TextView dateText;
    private RadioButton monthButton;

    private RadioButton dayButton;
    private RadioGroup timeGroup;
    private RadioGroup typeGroup;

    private RadioButton originalButton;
    private RadioButton foldButton;
    private RadioGroup styleGroup;

    private RadioButton companyButton;
    private RadioButton industryButton;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout layout;
    private LinearLayout errorLayout;

    private RecyclerView recyclerView;
    private WaterRankingRecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private List<WaterRankingBean> waterRankingBeanList = new ArrayList<>();

    private PieChartManager pieChartManager;
    private XYMarkerView pieMarkerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_ranking, container, false);
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
        waterUnit = preferences.getString("water_unit", null);

        textView1 = view.findViewById(R.id.water_ranking_fragment_chart_text);

        dateDialog = new DateDialog(getContext());
        monthDialog = new DateDialog(getContext(), true, false);
        dateText = view.findViewById(R.id.water_ranking_date_text);
        monthButton = view.findViewById(R.id.water_ranking_fragment_month_button);
        dayButton = view.findViewById(R.id.water_ranking_fragment_day_button);
        timeGroup = view.findViewById(R.id.water_ranking_fragment_time_group);

        typeGroup = view.findViewById(R.id.water_ranking_fragment_type_group);
        originalButton = view.findViewById(R.id.water_ranking_fragment_original_button);
        foldButton = view.findViewById(R.id.water_ranking_fragment_fold_button);

        styleGroup = view.findViewById(R.id.water_ranking_fragment_style_group);
        companyButton = view.findViewById(R.id.water_ranking_fragment_company_button);
        industryButton = view.findViewById(R.id.water_ranking_fragment_industry_button);

        layout = view.findViewById(R.id.water_ranking_fragment_layout);
        errorLayout = view.findViewById(R.id.water_ranking_fragment_error_layout);

        refreshLayout = view.findViewById(R.id.water_ranking_fragment_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });

        recyclerView = view.findViewById(R.id.water_ranking_fragment_recyclerView);
        adapter = new WaterRankingRecyclerAdapter(getContext(), waterRankingBeanList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        mPieChart = view.findViewById(R.id.water_ranking_fragment_pie);
        pieChartManager = new PieChartManager(mPieChart);
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
        if (timeGroup.getCheckedRadioButtonId() == R.id.water_ranking_fragment_day_button) {
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
        monthButton.setOnClickListener(this);
        dayButton.setOnClickListener(this);

        originalButton.setOnClickListener(this);
        foldButton.setOnClickListener(this);

        companyButton.setOnClickListener(this);
        industryButton.setOnClickListener(this);

        dateText.setOnClickListener(this);
        dateDialog.setOnCancelClickListener(new DateDialog.OnCancelClickListener() {
            @Override
            public void OnClick(View view) {
                dateDialog.cancel();
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

        pieMarkerView.setBarOnClickListener(new BarOnClickListener() {
            @Override
            public void onClick(Entry e, Highlight highlight, View v) {
                StringBuffer content = new StringBuffer();
                if (date_type.equals("date")) {
                    if (type.equals("original")) {
                        content.append("当日能耗:" + Util.formatNum(e.getY()) + waterUnit);
                    } else {
                        content.append("当日能耗:" + Util.formatNum(e.getY()) + "tce");
                    }
                } else {
                    if (type.equals("original")) {
                        content.append("当月能耗:" + Util.formatNum(e.getY()) + waterUnit);
                    } else {
                        content.append("当月能耗:" + Util.formatNum(e.getY()) + "tce");
                    }
                }
                ((TextView) v).setText(content.toString());
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
     * 发送网络请求
     */
    private void sendRequest() {
        getType();
        String time = dateText.getText().toString().trim();
        refreshLayout.setRefreshing(true);
        if (style.equals("company")) {
            HttpUtil.sendGetRequestWithHttp(COMPANY_URL + token + "&date_type=" + date_type + "&type=" + type
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
                                    RankCompanyInfo rankCompanyInfo = Util.handleRankCompanyInfo(content);
                                    if (rankCompanyInfo != null && rankCompanyInfo.getError() == null) {
                                        parseInfo(rankCompanyInfo);
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
        } else {
            HttpUtil.sendGetRequestWithHttp(INDUSTRY_URL + token + "&date_type=" + date_type + "&type=" + type
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
                                    RankCompanyInfo rankCompanyInfo = Util.handleRankCompanyInfo(content);
                                    if (rankCompanyInfo != null && rankCompanyInfo.getError() == null) {
                                        parseInfo(rankCompanyInfo);
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
    }

    /**
     * 解析服务器返回数据
     *
     * @param rankCompanyInfo
     */
    private void parseInfo(RankCompanyInfo rankCompanyInfo) {
        if (style.equals("company")) {
            if (date_type.equals("month")) {
                textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                        + dateText.getText().toString().split("-")[1]
                        + "月 企业耗水排行占比图");
            } else {
                textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                        + dateText.getText().toString().split("-")[1] + "月"
                        + dateText.getText().toString().split("-")[2]
                        + "日 企业耗水排行占比图");
            }
        } else {
            if (date_type.equals("month")) {
                textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                        + dateText.getText().toString().split("-")[1]
                        + "月 行业耗水排行占比图");
            } else {
                textView1.setText(dateText.getText().toString().split("-")[0] + "年"
                        + dateText.getText().toString().split("-")[1] + "月"
                        + dateText.getText().toString().split("-")[2]
                        + "日 行业耗水排行占比图");
            }
        }

        if (rankCompanyInfo.getChart() != null) {
            //设置饼图数据
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            for (int i = 0; i < rankCompanyInfo.getChart().getData().size(); i++) {
                entries.add(new PieEntry(Float.valueOf(rankCompanyInfo.getChart().getData().get(i).getValue()),
                        rankCompanyInfo.getChart().getData().get(i).getName()));
            }

            pieChartManager.showPieChart(entries, colors);
            pieChartManager.setLegendPosition();
        }

        waterRankingBeanList.clear();
        if (rankCompanyInfo.getTable_data() != null) {
            for (RankCompanyInfo.TableDataBean bean : rankCompanyInfo.getTable_data()) {
                WaterRankingBean waterRankingBean = new WaterRankingBean();
                waterRankingBean.setStyle(style);
                waterRankingBean.setName(bean.getName() + "");
                waterRankingBean.setData(bean.getData() + "");
                waterRankingBean.setPercent(bean.getPercent() + "");
                if (type.equals("original")) {
                    waterRankingBean.setRatio(waterUnit + "");
                } else {
                    waterRankingBean.setRatio("tce");
                }
                waterRankingBeanList.add(waterRankingBean);
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
            case R.id.water_ranking_fragment_month_button:
                long time2 = System.currentTimeMillis();
                String date2 = Util.parseTime(time2, 2);
                dateText.setText(date2);
                sendRequest();
                break;
            case R.id.water_ranking_fragment_day_button:
                long time3 = System.currentTimeMillis();
                String date3 = Util.parseTime(time3, 3);
                dateText.setText(date3);
                sendRequest();
                break;

            case R.id.water_ranking_fragment_original_button:
                sendRequest();
                break;
            case R.id.water_ranking_fragment_fold_button:
                sendRequest();
                break;
            case R.id.water_ranking_fragment_company_button:
                sendRequest();
                break;
            case R.id.water_ranking_fragment_industry_button:
                sendRequest();
                break;
            case R.id.water_ranking_date_text:
                if (timeGroup.getCheckedRadioButtonId() == R.id.water_ranking_fragment_month_button) {
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
        if (typeGroup.getCheckedRadioButtonId() == R.id.water_ranking_fragment_fold_button) {
            type = "fold";
        } else {
            type = "original";
        }
        if (timeGroup.getCheckedRadioButtonId() == R.id.water_ranking_fragment_day_button) {
            date_type = "date";
        } else {
            date_type = "month";
        }

        if (styleGroup.getCheckedRadioButtonId() == R.id.water_ranking_fragment_industry_button) {
            style = "industry";
        } else {
            style = "company";
        }
    }
}
