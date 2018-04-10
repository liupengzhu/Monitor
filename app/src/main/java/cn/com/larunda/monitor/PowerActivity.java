package cn.com.larunda.monitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.com.larunda.monitor.gson.CompanyRankInfo;
import cn.com.larunda.monitor.gson.PowerUsageInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BarChartManager;
import cn.com.larunda.monitor.util.BarChartViewPager;
import cn.com.larunda.monitor.util.BarOnClickListener;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.HBarChartManager;
import cn.com.larunda.monitor.util.HLineChartText;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.LineChartManager;
import cn.com.larunda.monitor.util.LineChartViewPager;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import cn.com.larunda.monitor.util.XValueFormatter;
import cn.com.larunda.monitor.util.XYMarkerView;
import cn.com.larunda.monitor.util.YValueFormatter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PowerActivity extends BaseActivity implements View.OnClickListener {

    private int[] colors = {MyApplication.getContext().getResources().getColor(R.color.company_color10),
            MyApplication.getContext().getResources().getColor(R.color.company_color9),
            MyApplication.getContext().getResources().getColor(R.color.company_color8),
            MyApplication.getContext().getResources().getColor(R.color.company_color7),
            MyApplication.getContext().getResources().getColor(R.color.company_color6),
            MyApplication.getContext().getResources().getColor(R.color.company_color5),
            MyApplication.getContext().getResources().getColor(R.color.company_color4),
            MyApplication.getContext().getResources().getColor(R.color.company_color3),
            MyApplication.getContext().getResources().getColor(R.color.company_color2),
            MyApplication.getContext().getResources().getColor(R.color.company_color1)};
    private final String USAGE_URL = MyApplication.URL + "home/load_usage" + MyApplication.TOKEN;
    private final String WATER_URL = MyApplication.URL + "home/water_usage" + MyApplication.TOKEN;
    private final String STEAM_URL = MyApplication.URL + "home/steam_usage" + MyApplication.TOKEN;
    private final String GAS_URL = MyApplication.URL + "home/gas_usage" + MyApplication.TOKEN;
    private final String RENEWABLE_URL = MyApplication.URL + "home/load_generated" + MyApplication.TOKEN;
    private final String COMPANY_URL = MyApplication.URL + "home/company_energy" + MyApplication.TOKEN;
    private Button backButton;

    private String date;
    private String date2;

    private SharedPreferences preferences;
    private String token;

    private LineChartViewPager mLineChart;
    private LineChartManager lineChartManager;
    private List<String> dateXList = new ArrayList<>();
    private XYMarkerView lineMarkerView;

    private BarChartViewPager mWaterBarChart;
    private BarChartManager barChartManager;
    private XYMarkerView barMarkerView;

    private BarChartViewPager mSteamBarChart;
    private BarChartManager steamBarChartManager;
    private XYMarkerView steamBarMarkerView;

    private BarChartViewPager mGasBarChart;
    private BarChartManager gasBarChartManager;
    private XYMarkerView gasBarMarkerView;

    private BarChartViewPager mRenewableBarChart;
    private BarChartManager renewableBarChartManager;
    private XYMarkerView renewableBarMarkerView;
    private TextView textView;

    private String ratio;
    private String powerUnit;

    private HLineChartText companyBarChart;
    private HBarChartManager companyBarChartManager;
    private List<String> names;
    private XYMarkerView companyBarMarkerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);
        initData();
        initView();
        initEvent();
        sendRequest();
    }

    private void initData() {
        long time = System.currentTimeMillis();
        date = Util.parseTime(time, 3);
        date2 = Util.parseTime(time, 2);
    }

    /**
     * 初始化view
     */
    private void initView() {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);

        powerUnit = preferences.getString("power_unit", null);

        backButton = findViewById(R.id.power_back);
        textView = findViewById(R.id.power_text_view);

        mLineChart = findViewById(R.id.power_line);
        lineChartManager = new LineChartManager(mLineChart);
        lineChartManager.setDescription("");

        lineMarkerView = new XYMarkerView(this);
        lineMarkerView.setChartView(mLineChart);
        mLineChart.setMarker(lineMarkerView);

        mWaterBarChart = findViewById(R.id.power_bar_water);
        barChartManager = new BarChartManager(mWaterBarChart);
        barChartManager.setDescription("");
        barChartManager.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "日";
            }
        });
        barChartManager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Util.formatNum(value) + "";
            }
        });
        barMarkerView = new XYMarkerView(this);
        barMarkerView.setChartView(mWaterBarChart);
        mWaterBarChart.setMarker(barMarkerView);

        mSteamBarChart = findViewById(R.id.power_bar_steam);
        steamBarChartManager = new BarChartManager(mSteamBarChart);
        steamBarChartManager.setDescription("");
        steamBarChartManager.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "日";
            }
        });
        steamBarChartManager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Util.formatNum(value) + "";
            }
        });
        steamBarMarkerView = new XYMarkerView(this);
        steamBarMarkerView.setChartView(mSteamBarChart);
        mSteamBarChart.setMarker(steamBarMarkerView);

        mGasBarChart = findViewById(R.id.power_bar_gas);
        gasBarChartManager = new BarChartManager(mGasBarChart);
        gasBarChartManager.setDescription("");
        gasBarChartManager.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "日";
            }
        });
        gasBarChartManager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Util.formatNum(value) + "";
            }
        });
        gasBarMarkerView = new XYMarkerView(this);
        gasBarMarkerView.setChartView(mGasBarChart);
        mGasBarChart.setMarker(gasBarMarkerView);

        mRenewableBarChart = findViewById(R.id.power_bar_renewable);
        renewableBarChartManager = new BarChartManager(mRenewableBarChart);
        renewableBarChartManager.setDescription("");
        renewableBarChartManager.setxValueFormatter(new XValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "日";
            }
        });
        renewableBarChartManager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Util.formatNum(value) + "";
            }
        });
        renewableBarMarkerView = new XYMarkerView(this);
        renewableBarMarkerView.setChartView(mRenewableBarChart);
        mRenewableBarChart.setMarker(renewableBarMarkerView);

        companyBarChart = findViewById(R.id.power_bar_rank);
        companyBarChartManager = new HBarChartManager(companyBarChart);
        companyBarChartManager.setDescription("");
        companyBarChartManager.setyValueFormatter(new YValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Util.formatNum(value);
            }
        });
        companyBarMarkerView = new XYMarkerView(this);
        companyBarMarkerView.setChartView(companyBarChart);
        companyBarChart.setMarker(companyBarMarkerView);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        backButton.setOnClickListener(this);
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
                return Util.formatNum(value) + " ";
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
                    content.append("时间:" + date + " " + dateXList.get(position) + "\r\n");
                }
                content.append("用电负荷:" + Util.formatNum(e.getY()) + "kW");
                ((TextView) v).setText(content.toString());
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
                        content.append("时间:" + date2 + "-0" + (int) e.getX() + "\r\n");
                    } else {
                        content.append("时间:" + date2 + "-" + (int) e.getX() + "\r\n");
                    }
                    content.append("用水量:" + Util.formatNum(e.getY()) + "m³");
                    ((TextView) v).setText(content.toString());
                }
            }
        });

        steamBarMarkerView.setBarOnClickListener(new BarOnClickListener() {
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
                        content.append("时间:" + date2 + "-0" + (int) e.getX() + "\r\n");
                    } else {
                        content.append("时间:" + date2 + "-" + (int) e.getX() + "\r\n");
                    }
                    content.append("用蒸汽量:" + Util.formatNum(e.getY()) + "kg");
                    ((TextView) v).setText(content.toString());
                }
            }
        });

        gasBarMarkerView.setBarOnClickListener(new BarOnClickListener() {
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
                        content.append("时间:" + date2 + "-0" + (int) e.getX() + "\r\n");
                    } else {
                        content.append("时间:" + date2 + "-" + (int) e.getX() + "\r\n");
                    }
                    content.append("用天然气量:" + Util.formatNum(e.getY()) + "m³");
                    ((TextView) v).setText(content.toString());
                }
            }
        });

        renewableBarMarkerView.setBarOnClickListener(new BarOnClickListener() {
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
                        content.append("时间:" + date2 + "-0" + (int) e.getX() + "\r\n");
                    } else {
                        content.append("时间:" + date2 + "-" + (int) e.getX() + "\r\n");
                    }
                    content.append("发电量:" + Util.formatNum(e.getY()) + ratio + powerUnit);
                    ((TextView) v).setText(content.toString());
                }
            }
        });

        companyBarMarkerView.setBarOnClickListener(new BarOnClickListener() {
            @Override
            public void onClick(Entry e, Highlight highlight, View v) {
                if (v instanceof TextView) {
                    StringBuffer content = new StringBuffer();
                    content.append("企业能耗:" + Util.formatNum(e.getY()) + "tce");
                    ((TextView) v).setText(content.toString());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.power_back:
                finish();
                break;
        }
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
        HttpUtil.sendGetRequestWithHttp(USAGE_URL + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    final PowerUsageInfo usageInfo = Util.handlePowerUsageInfo(content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (usageInfo != null && usageInfo.getError() == null) {
                                parseUsageInfo(usageInfo);
                            } else {
                                Intent intent = new Intent(PowerActivity.this, LoginActivity.class);
                                intent.putExtra("token_timeout", "登录超时");
                                preferences.edit().putString("token", null).commit();
                                startActivity(intent);
                                ActivityCollector.finishAllActivity();
                            }
                        }
                    });

                }
            }
        });
        HttpUtil.sendGetRequestWithHttp(WATER_URL + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    final PowerUsageInfo usageInfo = Util.handlePowerUsageInfo(content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (usageInfo != null && usageInfo.getError() == null) {
                                parseUsageWaterInfo(usageInfo);
                            } else {
                                Intent intent = new Intent(PowerActivity.this, LoginActivity.class);
                                intent.putExtra("token_timeout", "登录超时");
                                preferences.edit().putString("token", null).commit();
                                startActivity(intent);
                                ActivityCollector.finishAllActivity();
                            }
                        }
                    });
                }
            }
        });
        HttpUtil.sendGetRequestWithHttp(STEAM_URL + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    final PowerUsageInfo usageInfo = Util.handlePowerUsageInfo(content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (usageInfo != null && usageInfo.getError() == null) {
                                parseUsageSteamInfo(usageInfo);
                            } else {
                                Intent intent = new Intent(PowerActivity.this, LoginActivity.class);
                                intent.putExtra("token_timeout", "登录超时");
                                preferences.edit().putString("token", null).commit();
                                startActivity(intent);
                                ActivityCollector.finishAllActivity();
                            }
                        }
                    });
                }
            }
        });
        HttpUtil.sendGetRequestWithHttp(GAS_URL + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    final PowerUsageInfo usageInfo = Util.handlePowerUsageInfo(content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (usageInfo != null && usageInfo.getError() == null) {
                                parseUsageGasInfo(usageInfo);
                            } else {
                                Intent intent = new Intent(PowerActivity.this, LoginActivity.class);
                                intent.putExtra("token_timeout", "登录超时");
                                preferences.edit().putString("token", null).commit();
                                startActivity(intent);
                                ActivityCollector.finishAllActivity();
                            }
                        }
                    });
                }
            }
        });
        HttpUtil.sendGetRequestWithHttp(RENEWABLE_URL + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    final PowerUsageInfo usageInfo = Util.handlePowerUsageInfo(content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (usageInfo != null && usageInfo.getError() == null) {
                                parseUsageRenewableInfo(usageInfo);
                            } else {
                                Intent intent = new Intent(PowerActivity.this, LoginActivity.class);
                                intent.putExtra("token_timeout", "登录超时");
                                preferences.edit().putString("token", null).commit();
                                startActivity(intent);
                                ActivityCollector.finishAllActivity();
                            }
                        }
                    });
                }
            }
        });
        HttpUtil.sendGetRequestWithHttp(COMPANY_URL + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    final CompanyRankInfo usageInfo = Util.handleCompanyRankInfo(content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (usageInfo != null && usageInfo.getError() == null) {
                                parseCompanyRankInfo(usageInfo);
                            } else {
                                Intent intent = new Intent(PowerActivity.this, LoginActivity.class);
                                intent.putExtra("token_timeout", "登录超时");
                                preferences.edit().putString("token", null).commit();
                                startActivity(intent);
                                ActivityCollector.finishAllActivity();
                            }
                        }
                    });
                }
            }
        });
    }

    private void parseCompanyRankInfo(CompanyRankInfo usageInfo) {
        if (usageInfo.getName() != null) {
            names = usageInfo.getName();
            Collections.reverse(names);
        }
        if (usageInfo.getData() != null) {
            List<String> data = usageInfo.getData();
            Collections.reverse(data);
            float values[] = new float[data.size()];
            for (int i = 0; i < data.size(); i++) {
                String value = data.get(i);
                if (value != null) {
                    values[i] = Float.valueOf(value);
                }
            }
            companyBarChartManager.showBarChart(values, "", colors);
            companyBarChartManager.setContent(names);
        }
    }

    private void parseUsageRenewableInfo(PowerUsageInfo usageInfo) {
        ratio = usageInfo.getRatio();
        textView.setText("当月可再生能源发电量(" + ratio + powerUnit + ")");
        if (usageInfo.getSeries() != null) {
            float values[] = new float[usageInfo.getSeries().size()];
            for (int i = 0; i < usageInfo.getSeries().size(); i++) {
                String value = usageInfo.getSeries().get(i);
                if (value != null) {
                    values[i] = Float.valueOf(value);
                }
            }
            renewableBarChartManager.showBarChart(values, "", getResources().getColor(R.color.color2));
        }
    }

    private void parseUsageGasInfo(PowerUsageInfo usageInfo) {
        if (usageInfo.getSeries() != null) {
            float values[] = new float[usageInfo.getSeries().size()];
            for (int i = 0; i < usageInfo.getSeries().size(); i++) {
                String value = usageInfo.getSeries().get(i);
                if (value != null) {
                    values[i] = Float.valueOf(value);
                }
            }
            gasBarChartManager.showBarChart(values, "", getResources().getColor(R.color.color5));
        }
    }

    private void parseUsageSteamInfo(PowerUsageInfo usageInfo) {
        if (usageInfo.getSeries() != null) {
            float values[] = new float[usageInfo.getSeries().size()];
            for (int i = 0; i < usageInfo.getSeries().size(); i++) {
                String value = usageInfo.getSeries().get(i);
                if (value != null) {
                    values[i] = Float.valueOf(value);
                }
            }
            steamBarChartManager.showBarChart(values, "", getResources().getColor(R.color.color4));
        }
    }


    private void parseUsageWaterInfo(PowerUsageInfo usageInfo) {
        if (usageInfo.getSeries() != null) {
            float values[] = new float[usageInfo.getSeries().size()];
            for (int i = 0; i < usageInfo.getSeries().size(); i++) {
                String value = usageInfo.getSeries().get(i);
                if (value != null) {
                    values[i] = Float.valueOf(value);
                }
            }
            barChartManager.showBarChart(values, "", getResources().getColor(R.color.color3));
        }
    }

    /**
     * 解析数据
     *
     * @param usageInfo
     */
    private void parseUsageInfo(PowerUsageInfo usageInfo) {
        dateXList = usageInfo.getX_name();
        if (usageInfo.getSeries() != null) {
            //设置x轴的数据
            ArrayList<Float> xValues = new ArrayList<>();
            for (int i = 0; i < 288; i++) {
                xValues.add((float) i);
            }
            //设置y轴的数据()
            List<Float> yValue = new ArrayList<>();
            for (int i = 0; i < usageInfo.getSeries().size(); i++) {
                if (usageInfo.getSeries().get(i) != null) {
                    yValue.add(Float.valueOf(usageInfo.getSeries().get(i)));
                } else {
                    yValue.add(0f);
                }
            }
            lineChartManager.showLineChart(xValues, yValue, "小时", getResources().getColor(R.color.color1));
        }
    }
}
