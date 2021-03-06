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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.dialog.ChooseDialog;
import cn.com.larunda.dialog.DateDialog;
import cn.com.larunda.monitor.FilterCompanyActivity;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.WorksheetActivity;
import cn.com.larunda.monitor.adapter.WorksheetAdapter;
import cn.com.larunda.monitor.bean.WarningBean;
import cn.com.larunda.monitor.bean.WorksheetBean;
import cn.com.larunda.monitor.gson.WarningInfo;
import cn.com.larunda.monitor.gson.WorksheetInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import cn.com.larunda.recycler.OnLoadListener;
import cn.com.larunda.recycler.PTLLinearLayoutManager;
import cn.com.larunda.recycler.PullToLoadRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sddt on 18-3-21.
 */

public class MaintenanceWorksheetFragment extends Fragment implements View.OnClickListener {

    private final String WORKSHEET_URL = MyApplication.URL + "integrated_maint_company/worksheet_lists" + MyApplication.TOKEN;
    private static final int REQUEST_CODE = 12;
    private SharedPreferences preferences;
    private String token;

    private TextView typeText;
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

    private List<String> types = new ArrayList<>();
    private ChooseDialog chooseDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_worksheet, container, false);
        intData();
        initView(view);
        initEvent();
        initType();
        sendRequest();
        errorLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        return view;
    }

    /**
     * 初始化数据
     */
    private void intData() {
        types.clear();
        types.add("全部");
        types.add("已取消");
        types.add("已完成");
        types.add("处理中");
        types.add("未开始");
    }


    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);

        chooseDialog = new ChooseDialog(getContext(), types);
        typeText = view.findViewById(R.id.maintenance_worksheet_types_text);

        errorLayout = view.findViewById(R.id.maintenance_worksheet_error_layout);
        layout = view.findViewById(R.id.maintenance_worksheet_layout);

        refreshLayout = view.findViewById(R.id.maintenance_worksheet_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initType();
                sendRequest();
            }
        });

        recyclerView = view.findViewById(R.id.maintenance_worksheet_recycler);
        adapter = new WorksheetAdapter(getContext(), worksheetBeanList);
        manager = new PTLLinearLayoutManager();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshEnable(false);

        textView = view.findViewById(R.id.maintenance_worksheet_date_text);
        button = view.findViewById(R.id.maintenance_worksheet_search_button);
        dialog = new DateDialog(getContext(), true, true);


    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {

        typeText.setOnClickListener(this);
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
                status = position;
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
            case R.id.maintenance_worksheet_date_text:
                dialog.show();
                break;
            case R.id.maintenance_worksheet_search_button:
                Intent intent = new Intent(getContext(), FilterCompanyActivity.class);
                intent.putExtra("type", "worksheet");
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.maintenance_worksheet_types_text:
                chooseDialog.show();
                break;
            default:
                break;
        }
    }


    /**
     * 重置状态
     */
    private void initType() {
        company_id = 0;
        textView.setText("选择时间");
        typeText.setText("全部");
        status = 0;
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
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                            errorLayout.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.GONE);
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
                                WorksheetInfo info = Util.handleWorksheetInfo(content);
                                if (info != null && info.getError() == null) {
                                    parseInfo(info);
                                    refreshLayout.setRefreshing(false);
                                    errorLayout.setVisibility(View.GONE);
                                    layout.setVisibility(View.VISIBLE);
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
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
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
                                WorksheetInfo info = Util.handleWorksheetInfo(content);
                                if (info != null && info.getError() == null) {
                                    parseLoadInfo(info);
                                    refreshLayout.setRefreshing(false);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    company_id = data.getIntExtra("id", 0);
                    sendRequest();
                }
                break;
        }
    }
}
