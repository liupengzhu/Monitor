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
import cn.com.larunda.monitor.adapter.GasRankingRecyclerAdapter;
import cn.com.larunda.monitor.adapter.RenewableRankingRecyclerAdapter;
import cn.com.larunda.monitor.bean.GasRankingBean;
import cn.com.larunda.monitor.bean.RenewableRankingBean;
import cn.com.larunda.monitor.gson.RankCompanyInfo;
import cn.com.larunda.monitor.gson.RenewableRankInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sddt on 18-3-20.
 */

public class RenewableRankingFragment extends Fragment implements View.OnClickListener {

    private int page = 1;
    private static final String RENEWABLE_RANK_URL = MyApplication.URL + "renewable/rank_lists" + MyApplication.TOKEN;
    private String date_type = "month";

    private SharedPreferences preferences;
    public static String token;

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
    private RenewableRankingRecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private List<RenewableRankingBean> renewableRankingBeanList = new ArrayList<>();
    private int maxPage;
    private int lastVisibleItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_renewable_ranking, container, false);
        initView(view);
        initDate();
        initEvent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        page = 1;
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

        yearDialog = new DateDialog(getContext(), false, false);
        yearButton = view.findViewById(R.id.renewable_ranking_fragment_year_button);
        monthDialog = new DateDialog(getContext(), true, false);
        dateText = view.findViewById(R.id.renewable_ranking_date_text);
        monthButton = view.findViewById(R.id.renewable_ranking_fragment_month_button);
        timeGroup = view.findViewById(R.id.renewable_ranking_fragment_time_group);

        layout = view.findViewById(R.id.renewable_ranking_fragment_layout);
        errorLayout = view.findViewById(R.id.renewable_ranking_fragment_error_layout);

        refreshLayout = view.findViewById(R.id.renewable_ranking_fragment_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                sendRequest();
            }
        });

        recyclerView = view.findViewById(R.id.renewable_ranking_fragment_recyclerView);
        adapter = new RenewableRankingRecyclerAdapter(getContext(), renewableRankingBeanList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        long time = System.currentTimeMillis();
        String date;
        if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_ranking_fragment_year_button) {
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (page < maxPage) {
                    //在newState为滑到底部时
                    if (lastVisibleItem + 1 == adapter.getItemCount()) {
                        sendAddRequest();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 发送添加请求
     */
    private void sendAddRequest() {
        getType();
        String time = dateText.getText().toString().trim();
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(RENEWABLE_RANK_URL + token + "&date_type=" + date_type
                + "&time=" + time + "&page=" + page, new Callback() {
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
                                RenewableRankInfo renewableRankInfo = Util.handleRenewableRankInfo(content);
                                if (renewableRankInfo != null && renewableRankInfo.getError() == null) {
                                    parseAddInfo(renewableRankInfo);
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

    private void parseAddInfo(RenewableRankInfo renewableRankInfo) {
        page = renewableRankInfo.getCurrent_page() + 1;
        maxPage = renewableRankInfo.getLast_page();
        if (renewableRankInfo.getData() != null) {
            for (RenewableRankInfo.DataBean bean : renewableRankInfo.getData()) {
                RenewableRankingBean renewableRankingBean = new RenewableRankingBean();
                renewableRankingBean.setRank(bean.getRank());
                renewableRankingBean.setName(bean.getCompany_name());
                renewableRankingBean.setInstalledCapacity(bean.getInstalled_capacity());
                renewableRankingBean.setTotal(bean.getTotal_generated());
                renewableRankingBean.setHistory_average(bean.getHistory_average());
                renewableRankingBean.setRange(bean.getRange());
                renewableRankingBean.setInstalledCapacityRatio(preferences.getString("installed_capacity_unit", null) + "");
                renewableRankingBean.setRatio(renewableRankInfo.getRatio() + preferences.getString("power_unit", null) + "");
                renewableRankingBeanList.add(renewableRankingBean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
        recyclerView.scrollToPosition(0);
        getType();
        String time = dateText.getText().toString().trim();
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(RENEWABLE_RANK_URL + token + "&date_type=" + date_type
                + "&time=" + time + "&page=" + page, new Callback() {
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
                                RenewableRankInfo renewableRankInfo = Util.handleRenewableRankInfo(content);
                                if (renewableRankInfo != null && renewableRankInfo.getError() == null) {
                                    parseInfo(renewableRankInfo);
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
     * 处理服务器返回信息
     *
     * @param renewableRankInfo
     */
    private void parseInfo(RenewableRankInfo renewableRankInfo) {
        page = renewableRankInfo.getCurrent_page() + 1;
        maxPage = renewableRankInfo.getLast_page();
        renewableRankingBeanList.clear();
        if (renewableRankInfo.getData() != null) {
            for (RenewableRankInfo.DataBean bean : renewableRankInfo.getData()) {
                RenewableRankingBean renewableRankingBean = new RenewableRankingBean();
                renewableRankingBean.setRank(bean.getRank());
                renewableRankingBean.setName(bean.getCompany_name());
                renewableRankingBean.setInstalledCapacity(bean.getInstalled_capacity());
                renewableRankingBean.setTotal(bean.getTotal_generated());
                renewableRankingBean.setHistory_average(bean.getHistory_average());
                renewableRankingBean.setRange(bean.getRange());
                renewableRankingBean.setInstalledCapacityRatio(preferences.getString("installed_capacity_unit", null) + "");
                renewableRankingBean.setRatio(renewableRankInfo.getRatio() + preferences.getString("power_unit", null) + "");
                renewableRankingBeanList.add(renewableRankingBean);
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
            case R.id.renewable_ranking_fragment_year_button:
                long time1 = System.currentTimeMillis();
                String date1 = Util.parseTime(time1, 1);
                dateText.setText(date1);
                sendRequest();
                break;
            case R.id.renewable_ranking_fragment_month_button:
                long time2 = System.currentTimeMillis();
                String date2 = Util.parseTime(time2, 2);
                dateText.setText(date2);
                sendRequest();
                break;

            case R.id.renewable_ranking_date_text:
                if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_ranking_fragment_year_button) {
                    yearDialog.show();
                } else if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_ranking_fragment_month_button) {
                    monthDialog.show();
                }
                break;
        }
    }

    /**
     * 获取当前按钮组状态
     */
    private void getType() {

        if (timeGroup.getCheckedRadioButtonId() == R.id.renewable_ranking_fragment_year_button) {
            date_type = "year";
        } else {
            date_type = "month";
        }
    }
}
