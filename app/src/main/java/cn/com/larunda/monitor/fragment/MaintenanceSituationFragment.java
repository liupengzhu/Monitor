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
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.larunda.monitor.CompanyListActivity;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.MaintenanceSituationAdapter;
import cn.com.larunda.monitor.bean.MaintenanceCompany;
import cn.com.larunda.monitor.gson.MaintenanceCompanyInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sddt on 18-3-21.
 */

public class MaintenanceSituationFragment extends Fragment implements View.OnClickListener {
    private static final String COMPANY_URL = MyApplication.URL + "integrated_maint_company/lists" + MyApplication.TOKEN;
    private HashMap<String, Integer> iconList = new HashMap<>();
    private List<MaintenanceCompany> companyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MaintenanceSituationAdapter adapter;
    private LinearLayoutManager manager;
    private SharedPreferences preferences;
    private String token;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout errorLayout;
    private RelativeLayout button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_situation, container, false);
        initData();
        initView(view);
        initEvent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        sendRequest();
        recyclerView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        iconList.clear();
        iconList.put("用电安全", R.drawable.electric_icon);
        iconList.put("空压机", R.drawable.air_compressor_icon);
        iconList.put("空调", R.drawable.air_conditioner_icon);
        iconList.put("锅炉", R.drawable.boiler_icon);
        iconList.put("水泵", R.drawable.water_pump_icon);
        iconList.put("冰机", R.drawable.ice_maker_icon);
        iconList.put("风机", R.drawable.fan_icon);
        iconList.put("照明", R.drawable.lighting_icon);
        iconList.put("监控", R.drawable.monitor_icon);
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);

        recyclerView = view.findViewById(R.id.maintenance_situation_recycler);
        manager = new LinearLayoutManager(getContext());
        adapter = new MaintenanceSituationAdapter(getContext(), companyList, iconList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        errorLayout = view.findViewById(R.id.maintenance_situation_error_layout);

        refreshLayout = view.findViewById(R.id.maintenance_situation_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });

        button = view.findViewById(R.id.maintenance_situation_button);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        adapter.setOnClickListener(new MaintenanceSituationAdapter.MaintenanceCompanyOnClickListener() {
            @Override
            public void onClick(View v, int id) {
                Intent intent = new Intent(getContext(), CompanyListActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        button.setOnClickListener(this);
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(COMPANY_URL + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                            recyclerView.setVisibility(View.GONE);
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
                                MaintenanceCompanyInfo info = Util.handleMaintenanceCompanyInfo(content);
                                if (info != null && info.getError() == null) {
                                    parseInfo(info);
                                    refreshLayout.setRefreshing(false);
                                    recyclerView.setVisibility(View.VISIBLE);
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
    private void parseInfo(MaintenanceCompanyInfo info) {
        companyList.clear();
        if (info.getData() != null) {
            for (MaintenanceCompanyInfo.DataBean dataBean : info.getData()) {
                MaintenanceCompany company = new MaintenanceCompany();
                List<String> typeList = new ArrayList<>();
                company.setId(dataBean.getId());
                company.setName(dataBean.getName());
                company.setTel(dataBean.getTel());
                company.setAddress(dataBean.getAddress());
                company.setUser(dataBean.getConnect_user());
                company.setPerson(dataBean.getPerson());
                company.setCar(dataBean.getCar());
                company.setCompany(dataBean.getCompany());
                if (dataBean.getLog() != null) {
                    company.setImg(MyApplication.IMG_URL + dataBean.getLog());
                }
                if (dataBean.getMaintenance_type() != null) {
                    for (MaintenanceCompanyInfo.DataBean.MaintenanceTypeBean typeBean : dataBean.getMaintenance_type()) {
                        typeList.add(typeBean.getName());
                    }
                }
                company.setTypeList(typeList);
                companyList.add(company);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maintenance_situation_button:
                Intent intent = new Intent(getContext(), CompanyListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
