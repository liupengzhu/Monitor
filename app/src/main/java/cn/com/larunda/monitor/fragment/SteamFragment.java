package cn.com.larunda.monitor.fragment;

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

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.SteamRecyclerAdapter;
import cn.com.larunda.monitor.adapter.WaterRecyclerAdapter;
import cn.com.larunda.monitor.bean.SteamBean;
import cn.com.larunda.monitor.bean.WaterBean;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;

/**
 * Created by sddt on 18-3-20.
 */

public class SteamFragment extends Fragment implements View.OnClickListener {
    private final String WATER_URL = MyApplication.URL + "steam/usage" + MyApplication.TOKEN;
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
    private SteamRecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private List<SteamBean> steamBeanList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steam, container, false);
        initView(view);
        initData();
        initEvent();
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
        textView1 = view.findViewById(R.id.steam_fragment_chart_text);
        yearDialog = new DateDialog(getContext(), false, false);
        monthDialog = new DateDialog(getContext(), true, false);
        dateText = view.findViewById(R.id.steam_date_text);
        yearButton = view.findViewById(R.id.steam_fragment_year_button);
        monthButton = view.findViewById(R.id.steam_fragment_month_button);
        timeGroup = view.findViewById(R.id.steam_fragment_time_group);

        typeGroup = view.findViewById(R.id.steam_fragment_type_group);
        originalButton = view.findViewById(R.id.steam_fragment_original_button);
        foldButton = view.findViewById(R.id.steam_fragment_fold_button);

        layout = view.findViewById(R.id.steam_fragment_layout);
        errorLayout = view.findViewById(R.id.steam_fragment_error_layout);

        refreshLayout = view.findViewById(R.id.steam_fragment_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });

        recyclerView = view.findViewById(R.id.steam_fragment_recyclerView);
        adapter = new SteamRecyclerAdapter(getContext(), steamBeanList);
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
        if (timeGroup.getCheckedRadioButtonId() == R.id.steam_fragment_year_button) {
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
    }

    /**
     * 发送网络数据
     */
    private void sendRequest() {
    }

    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.steam_fragment_year_button:
                long time1 = System.currentTimeMillis();
                String date1 = Util.parseTime(time1, 1);
                dateText.setText(date1);
                sendRequest();
                break;
            case R.id.steam_fragment_month_button:
                long time2 = System.currentTimeMillis();
                String date2 = Util.parseTime(time2, 2);
                dateText.setText(date2);
                sendRequest();
                break;

            case R.id.steam_fragment_original_button:
                sendRequest();
                break;
            case R.id.steam_fragment_fold_button:
                sendRequest();
                break;
            case R.id.steam_date_text:
                if (timeGroup.getCheckedRadioButtonId() == R.id.steam_fragment_year_button) {
                    yearDialog.show();
                } else if (timeGroup.getCheckedRadioButtonId() == R.id.steam_fragment_month_button) {
                    monthDialog.show();
                }
                break;

        }
    }

    /**
     * 获取当前按钮组状态
     */
    private void getType() {
        if (typeGroup.getCheckedRadioButtonId() == R.id.steam_fragment_fold_button) {
            type = "fold";
        } else {
            type = "original";
        }
        if (timeGroup.getCheckedRadioButtonId() == R.id.steam_fragment_year_button) {
            date_type = "year";
        } else {
            date_type = "month";
        }
    }
}
