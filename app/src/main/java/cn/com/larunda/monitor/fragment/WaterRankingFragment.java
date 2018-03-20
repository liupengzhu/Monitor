package cn.com.larunda.monitor.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.PieChartViewPager;
import cn.com.larunda.monitor.util.Util;

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
    private String date_type = "month";

    private String type = "original";
    private String style = "company";
    private SharedPreferences preferences;
    public static String token;

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_ranking,container,false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    /**
     * 初始化view
     * @param view
     */
    private void initView(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);

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
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
    }

    /**
     * 点击事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
}
