package cn.com.larunda.monitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.adapter.FilterCompanyAdapter;
import cn.com.larunda.monitor.bean.FilterCompanyBean;
import cn.com.larunda.monitor.gson.FilterCompanyInfo;
import cn.com.larunda.monitor.gson.FilterCompanyWorksheetInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FilterCompanyActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String COMPANY_URL = MyApplication.URL + "company/lists" + MyApplication.TOKEN;
    private Button backButton;
    private String type;

    private SharedPreferences preferences;
    private String token;

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private FilterCompanyAdapter adapter;
    private List<FilterCompanyBean> companyBeanList = new ArrayList<>();

    private EditText editText;
    private TextView searchButton;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_company);
        type = getIntent().getStringExtra("type");
        initView();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sendRequest("");
        recyclerView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     *
     */
    private void sendRequest(String searchText) {
        String search;
        if (searchText.equals("")) {
            search = "";
        } else {
            search = "&search=" + searchText;
        }
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(COMPANY_URL + token + "&type=" + type + search, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        recyclerView.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    if (type.equals("alarm")) {
                        final FilterCompanyInfo info = Util.handleFilterCompanyInfo(content);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (info != null && info.getError() == null) {
                                    parseInfo(info);
                                    refreshLayout.setRefreshing(false);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    errorLayout.setVisibility(View.GONE);
                                } else {
                                    Intent intent = new Intent(FilterCompanyActivity.this, LoginActivity.class);
                                    intent.putExtra("token_timeout", "登录超时");
                                    preferences.edit().putString("token", null).commit();
                                    startActivity(intent);
                                    ActivityCollector.finishAllActivity();
                                }
                            }
                        });
                    } else {
                        final FilterCompanyWorksheetInfo info = Util.handleFilterCompanyWorksheetInfo(content);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (info != null && info.getError() == null) {
                                    parseInfoForWorksheet(info);
                                    refreshLayout.setRefreshing(false);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    errorLayout.setVisibility(View.GONE);
                                } else {
                                    Intent intent = new Intent(FilterCompanyActivity.this, LoginActivity.class);
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

    private void parseInfoForWorksheet(FilterCompanyWorksheetInfo info) {
        companyBeanList.clear();
        if (info.getData() != null) {
            for (FilterCompanyWorksheetInfo.DataBean dataBean : info.getData()) {
                FilterCompanyBean companyBean = new FilterCompanyBean();
                companyBean.setId(dataBean.getId());
                companyBean.setName(dataBean.getName());
                companyBean.setTotal(dataBean.getNum() + "");
                companyBean.setType("worksheet");
                companyBeanList.add(companyBean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void parseInfo(FilterCompanyInfo info) {
        companyBeanList.clear();
        if (info.getData() != null) {
            for (FilterCompanyInfo.DataBean dataBean : info.getData()) {
                FilterCompanyBean companyBean = new FilterCompanyBean();
                companyBean.setId(dataBean.getId());
                companyBean.setName(dataBean.getName());
                companyBean.setTotal(dataBean.getNum().getTotal());
                companyBean.setUnderway(dataBean.getNum().getUnderway());
                companyBean.setType("alarm");
                companyBeanList.add(companyBean);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化view
     */
    private void initView() {
        backButton = findViewById(R.id.filter_company_back_button);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);

        recyclerView = findViewById(R.id.filter_company_recycler);
        adapter = new FilterCompanyAdapter(this, companyBeanList);
        manager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        editText = findViewById(R.id.filter_company_edit);
        searchButton = findViewById(R.id.filter_company_button);

        errorLayout = findViewById(R.id.filter_company_error_layout);

        refreshLayout = findViewById(R.id.filter_company_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest("");
            }
        });
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        backButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendRequest(editText.getText().toString());
                return false;
            }
        });

        adapter.setItemOnClickListener(new FilterCompanyAdapter.ItemOnClickListener() {
            @Override
            public void OnClick(View v, int id) {
                if (id != 0) {
                    Intent intent = new Intent();
                    intent.putExtra("id", id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_company_back_button:
                finish();
            case R.id.filter_company_button:
                sendRequest(searchButton.getText().toString() + "");
                break;

        }
    }
}
