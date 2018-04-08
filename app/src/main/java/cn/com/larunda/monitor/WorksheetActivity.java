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

import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.adapter.WorksheetAdapter;
import cn.com.larunda.monitor.bean.WorksheetBean;
import cn.com.larunda.monitor.gson.WorksheetInfo;
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

public class WorksheetActivity extends BaseActivity implements View.OnClickListener {

    private Button backButton;

    private final String WORKSHEET_URL = MyApplication.URL + "integrated_maint_company/worksheet_lists" + MyApplication.TOKEN;
    private static final int REQUEST_CODE = 12;
    private SharedPreferences preferences;
    private String token;

    private Spinner spinner;
    private TextView textView;
    private LinearLayout button;
    private DateDialog dialog;
    private int company_id;
    private int status;

    private PullToLoadRecyclerView recyclerView;
    private WorksheetAdapter adapter;
    private PTLLinearLayoutManager manager;
    private List<WorksheetBean> worksheetBeanList = new ArrayList<>();

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout errorLayout;

    private int page;
    private int maxPage;
    private FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worksheet);
        company_id = getIntent().getIntExtra("id", 0);
        initView();
        initEvent();
        initType();
        sendRequest();
        errorLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
    }


    /**
     * 初始化view
     */
    private void initView() {
        backButton = findViewById(R.id.worksheet_back_button);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);

        errorLayout = findViewById(R.id.worksheet_error_layout);
        layout = findViewById(R.id.worksheet_layout);

        refreshLayout = findViewById(R.id.worksheet_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initType();
                sendRequest();
            }
        });

        recyclerView = findViewById(R.id.worksheet_recycler);
        adapter = new WorksheetAdapter(this, worksheetBeanList);
        manager = new PTLLinearLayoutManager();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshEnable(false);

        textView = findViewById(R.id.worksheet_date_text);
        button = findViewById(R.id.worksheet_search_button);
        button.setVisibility(View.GONE);
        dialog = new DateDialog(this, true, true);

        spinner = findViewById(R.id.worksheet_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner_showed) {

            @Override
            public int getCount() {
                return super.getCount();
            }
        };
        adapter.setDropDownViewResource(R.layout.item_spinner_option);
        adapter.add("-请选择-");
        adapter.add("已取消");
        adapter.add("已完成");
        adapter.add("处理中");
        adapter.add("未开始");
        spinner.setAdapter(adapter);

    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {

        backButton.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        status = 0;
                        break;
                    case 1:
                        status = 1;
                        break;
                    case 2:
                        status = 2;
                        break;
                    case 3:
                        status = 3;
                        break;
                    case 4:
                        status = 4;
                        break;
                }
                sendRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            case R.id.worksheet_date_text:
                dialog.show();
                break;
            case R.id.worksheet_back_button:
                finish();
                break;
        }
    }


    /**
     * 重置状态
     */
    private void initType() {

        textView.setText("选择时间");
        spinner.setSelection(0);
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
            statusData = "&status=" + (status - 1);
        }
        if (company_id == 0) {
            companyData = "";
        } else {
            companyData = "&company_id=" + company_id;
        }
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(WORKSHEET_URL + token + timeData + statusData + companyData + "&page=" + 1, new Callback() {
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
                            WorksheetInfo info = Util.handleWorksheetInfo(content);
                            if (info != null && info.getError() == null) {
                                parseInfo(info);
                                refreshLayout.setRefreshing(false);
                                errorLayout.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            } else {
                                Intent intent = new Intent(WorksheetActivity.this, LoginActivity.class);
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
    private void parseInfo(WorksheetInfo info) {
        worksheetBeanList.clear();
        page = info.getCurrent_page() + 1;
        maxPage = info.getLast_page();
        if (page > maxPage) {
            recyclerView.setNoMore(true);
        } else {
            recyclerView.setNoMore(false);
        }
        if (info.getData() != null) {
            for (WorksheetInfo.DataBean dataBean : info.getData()) {
                WorksheetBean bean = new WorksheetBean();
                bean.setData(dataBean.getTitle());
                bean.setName(dataBean.getCompany_name());
                bean.setTime(dataBean.getCreated_at());
                bean.setTitle(dataBean.getWorksheet_number());
                bean.setType(dataBean.getStatus());
                worksheetBeanList.add(bean);
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
            statusData = "&status=" + (status - 1);
        }
        if (company_id == 0) {
            companyData = "";
        } else {
            companyData = "&company_id=" + company_id;
        }
        HttpUtil.sendGetRequestWithHttp(WORKSHEET_URL + token + timeData + statusData + companyData + "&page=" + page, new Callback() {
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
                            WorksheetInfo info = Util.handleWorksheetInfo(content);
                            if (info != null && info.getError() == null) {
                                parseLoadInfo(info);
                                refreshLayout.setRefreshing(false);
                                errorLayout.setVisibility(View.GONE);
                            } else {
                                Intent intent = new Intent(WorksheetActivity.this, LoginActivity.class);
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
    private void parseLoadInfo(WorksheetInfo info) {
        page = info.getCurrent_page() + 1;
        maxPage = info.getLast_page();
        if (page > maxPage) {
            recyclerView.setNoMore(true);
        } else {
            recyclerView.setNoMore(false);
        }
        if (info.getData() != null) {
            for (WorksheetInfo.DataBean dataBean : info.getData()) {
                WorksheetBean bean = new WorksheetBean();
                bean.setData(dataBean.getTitle());
                bean.setName(dataBean.getCompany_name());
                bean.setTime(dataBean.getCreated_at());
                bean.setTitle(dataBean.getWorksheet_number());
                bean.setType(dataBean.getStatus());
                worksheetBeanList.add(bean);
            }

        }
        recyclerView.completeLoad(0);

    }

}
