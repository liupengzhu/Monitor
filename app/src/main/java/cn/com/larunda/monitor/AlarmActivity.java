package cn.com.larunda.monitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.dialog.ChooseDialog;
import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.adapter.WarningAdapter;
import cn.com.larunda.monitor.bean.WarningBean;
import cn.com.larunda.monitor.gson.WarningInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import cn.com.larunda.recycler.OnLoadListener;
import cn.com.larunda.recycler.PTLLinearLayoutManager;
import cn.com.larunda.recycler.PullToLoadRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static cn.com.larunda.monitor.util.MyApplication.getContext;

public class AlarmActivity extends BaseActivity implements View.OnClickListener {

    private Button backButton;
    private final String ALARM_URL = MyApplication.URL + "integrated_maint_company/alarm_lists" + MyApplication.TOKEN;
    private static final int REQUEST_CODE = 11;
    private SharedPreferences preferences;
    private String token;

    private TextView typeText;
    private TextView textView;
    private LinearLayout button;
    private DateDialog dialog;
    private int company_id;
    private int status;

    private PullToLoadRecyclerView recyclerView;
    private WarningAdapter adapter;
    private PTLLinearLayoutManager manager;
    private List<WarningBean> warningBeanList = new ArrayList<>();

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout errorLayout;
    private FrameLayout layout;

    private int page;
    private int maxPage;

    private List<String> types = new ArrayList<>();
    private ChooseDialog chooseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        company_id = getIntent().getIntExtra("id", 0);
        intData();
        initView();
        initEvent();
        initType();
        sendRequest();
        errorLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void intData() {
        types.clear();
        types.add("全部");
        types.add("处理中");
        types.add("未开始");
    }

    /**
     * 初始化view
     */
    private void initView() {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);

        chooseDialog = new ChooseDialog(this, types);
        typeText = findViewById(R.id.alarm_types_text);

        backButton = findViewById(R.id.alarm_back_button);
        errorLayout = findViewById(R.id.alarm_error_layout);
        layout = findViewById(R.id.alarm_layout);

        refreshLayout = findViewById(R.id.alarm_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initType();
                sendRequest();
            }
        });

        recyclerView = findViewById(R.id.alarm_recycler);
        adapter = new WarningAdapter(this, warningBeanList);
        manager = new PTLLinearLayoutManager();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshEnable(false);

        textView = findViewById(R.id.alarm_date_text);
        button = findViewById(R.id.alarm_search_button);
        button.setVisibility(View.GONE);
        dialog = new DateDialog(this, true, true);


    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {

        typeText.setOnClickListener(this);
        backButton.setOnClickListener(this);
        textView.setOnClickListener(this);
        dialog.setOnCancelClickListener(new DateDialog.OnCancelClickListener() {
            @Override
            public void OnClick(View view) {
                dialog.cancel();
            }
        });
        dialog.setOnOkClickListener(new DateDialog.OnOkClickListener() {
            @Override
            public void OnClick(View view, String date) {
                if (textView != null && date != null) {
                    textView.setText(date);
                    sendRequest();
                }
                dialog.cancel();
            }
        });

        chooseDialog.setOnClickListener(new ChooseDialog.OnClickListener() {
            @Override
            public void OnClick(View v, int position) {
                if (position == 1) {
                    status = 2;
                } else if (position == 2) {
                    status = 3;
                } else {
                    status = 0;
                }
                typeText.setText(types.get(position) + "");
                sendRequest();
                chooseDialog.cancel();
            }
        });

        button.setOnClickListener(this);

        recyclerView.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onStartLoading(int skip) {
                sendLoadRequest();
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
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_date_text:
                dialog.show();
                break;
            case R.id.alarm_search_button:
                Intent intent = new Intent(this, FilterCompanyActivity.class);
                intent.putExtra("type", "alarm");
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.alarm_back_button:
                finish();
                break;
            case R.id.alarm_types_text:
                chooseDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
        final String companyData;
        String statusData;
        String timeData;
        if (textView.getText().equals("选择时间")) {
            timeData = "";
        } else {
            timeData = "&time=" + textView.getText().toString();
        }
        if (status == 0) {
            statusData = "";
        } else {
            statusData = "&status=" + status;
        }
        if (company_id == 0) {
            companyData = "";
        } else {
            companyData = "&company_id=" + company_id;
        }
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(ALARM_URL + token + timeData + statusData + companyData + "&page=" + 1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        errorLayout.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WarningInfo info = Util.handleWarningInfo(content);
                            if (info != null && info.getError() == null) {
                                parseInfo(info);
                                refreshLayout.setRefreshing(false);
                                errorLayout.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            } else {
                                Intent intent = new Intent(AlarmActivity.this, LoginActivity.class);
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

    /**
     * 解析服务器返回数据
     *
     * @param info
     */
    private void parseInfo(WarningInfo info) {
        warningBeanList.clear();
        page = info.getCurrent_page() + 1;
        maxPage = info.getLast_page();
        if (page > maxPage) {
            recyclerView.setNoMore(true);
        } else {
            recyclerView.setNoMore(false);
        }
        if (info.getData() != null) {
            for (WarningInfo.DataBean dataBean : info.getData()) {
                WarningBean bean = new WarningBean();
                bean.setData(dataBean.getContent());
                bean.setName(dataBean.getCompany_name());
                bean.setTime(dataBean.getCreated_at());
                bean.setTitle(dataBean.getType());
                bean.setType(dataBean.getStatus());
                bean.setStatus(dataBean.getIs_send());
                warningBeanList.add(bean);
            }
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    /**
     * 发送网络请求
     */
    private void sendLoadRequest() {
        final String companyData;
        String statusData;
        String timeData;
        if (textView.getText().equals("选择时间")) {
            timeData = "";
        } else {
            timeData = "&time=" + textView.getText().toString();
        }
        if (status == 0) {
            statusData = "";
        } else {
            statusData = "&status=" + status;
        }
        if (company_id == 0) {
            companyData = "";
        } else {
            companyData = "&company_id=" + company_id;
        }
        HttpUtil.sendGetRequestWithHttp(ALARM_URL + token + timeData + statusData + companyData + "&page=" + page, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WarningInfo info = Util.handleWarningInfo(content);
                            if (info != null && info.getError() == null) {
                                parseLoadInfo(info);
                                refreshLayout.setRefreshing(false);
                                errorLayout.setVisibility(View.GONE);
                            } else {
                                Intent intent = new Intent(AlarmActivity.this, LoginActivity.class);
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

    /**
     * 解析服务器返回数据
     *
     * @param info
     */
    private void parseLoadInfo(WarningInfo info) {
        page = info.getCurrent_page() + 1;
        maxPage = info.getLast_page();
        if (page > maxPage) {
            recyclerView.setNoMore(true);
        } else {
            recyclerView.setNoMore(false);
        }
        if (info.getData() != null) {
            for (WarningInfo.DataBean dataBean : info.getData()) {
                WarningBean bean = new WarningBean();
                bean.setData(dataBean.getContent());
                bean.setName(dataBean.getCompany_name());
                bean.setTime(dataBean.getCreated_at());
                bean.setTitle(dataBean.getType());
                bean.setType(dataBean.getStatus());
                bean.setStatus(dataBean.getIs_send());
                warningBeanList.add(bean);
            }
        }
        recyclerView.completeLoad(0);
    }

    /**
     * 重置状态
     */
    private void initType() {

        textView.setText("选择时间");
        typeText.setText("全部");
        status = 0;
    }


}
