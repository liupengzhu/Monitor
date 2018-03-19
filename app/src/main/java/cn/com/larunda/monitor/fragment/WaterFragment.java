package cn.com.larunda.monitor.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;

/**
 * Created by sddt on 18-3-19.
 */

public class WaterFragment extends Fragment implements View.OnClickListener {

    private final String WATER_URL = MyApplication.URL + "power/usage" + MyApplication.TOKEN;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water, container, false);
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

        mBarChart = view.findViewById(R.id.water_fragment_chart);
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
     * 发送网络请求
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
}
