package cn.com.larunda.monitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.larunda.monitor.adapter.CompanyAdapter;
import cn.com.larunda.monitor.bean.Company;
import cn.com.larunda.monitor.gson.CompanyInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CompanyListActivity extends BaseActivity implements View.OnClickListener {
    private static final String COMPANY_URL = MyApplication.URL + "integrated_maint_company/detail" + MyApplication.TOKEN;
    private SharedPreferences preferences;
    private String token;
    private int id;
    private Button backButton;

    private HashMap<String, Integer> iconList = new HashMap<>();

    private List<Company> companyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CompanyAdapter adapter;
    private LinearLayoutManager manager;

    private int page = 1;
    private int maxPage;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        id = getIntent().getIntExtra("id", 0);
        initData();
        initView();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        page = 1;
        sendRequest();
        recyclerView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

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
     */
    private void initView() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);

        backButton = findViewById(R.id.company_list_back);

        recyclerView = findViewById(R.id.company_list_recycler);
        manager = new LinearLayoutManager(this);
        adapter = new CompanyAdapter(this, companyList, iconList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        errorLayout = findViewById(R.id.company_list_error_layout);

        refreshLayout = findViewById(R.id.company_list_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                sendRequest();
            }
        });
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        backButton.setOnClickListener(this);
        adapter.setAlarmOnClickListener(new CompanyAdapter.AlarmOnClickListener() {
            @Override
            public void onClick(View v, int id) {
                Intent intent = new Intent(CompanyListActivity.this, AlarmActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        adapter.setMaintenanceOnClickListener(new CompanyAdapter.MaintenanceOnClickListener() {
            @Override
            public void onClick(View v, int id) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.company_list_back:
                finish();
                break;
        }
    }

    private void sendRequest() {
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(COMPANY_URL + token + "&maintenance_company_id=" + id
                + "&page=" + page, new Callback() {
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
                final String content = response.body().string();
                Log.d("main", content);
                if (Util.isGoodJson(content)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CompanyInfo info = Util.handleCompanyInfo(content);
                            if (info != null && info.getError() == null) {
                                parseInfo(info);
                                refreshLayout.setRefreshing(false);
                                recyclerView.setVisibility(View.VISIBLE);
                                errorLayout.setVisibility(View.GONE);
                            } else {
                                Intent intent = new Intent(CompanyListActivity.this, LoginActivity.class);
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

    private void parseInfo(CompanyInfo info) {
        companyList.clear();
        if (info.getData() != null) {
            for (CompanyInfo.DataBeanX dataBean : info.getData()) {
                Company company = new Company();
                List<String> typeList = new ArrayList<>();
                List<String> deviceList = new ArrayList<>();
                if (dataBean.getCompany_pic() != null) {
                    company.setImg(MyApplication.IMG_URL + dataBean.getCompany_pic());
                }
                int normal = dataBean.getDevice_data().getDevice_total()
                        - dataBean.getDevice_data().getError_total();
                company.setAddress(dataBean.getCompany_address());
                company.setAlarm(dataBean.getAlarm_data() + "");
                company.setId(Integer.parseInt(dataBean.getCompany_id()));
                company.setElectric(dataBean.getDevice_data().getMeter_total_num() + "");
                company.setIndustry(dataBean.getCompany_industry());
                company.setMaintenance(dataBean.getMaintenance_num() + "");
                company.setName(dataBean.getCompany_name());
                company.setTel(dataBean.getCompany_tel());
                company.setTotal(normal + "");
                if (dataBean.getDevice_data().getDevice_total() != 0) {

                    company.setAngle((float) normal / (float) dataBean.getDevice_data().getDevice_total() * 360);
                }
                if (dataBean.getMaintenance_type() != null) {
                    for (CompanyInfo.DataBeanX.MaintenanceTypeBean typeBean : dataBean.getMaintenance_type()) {
                        typeList.add(typeBean.getName());
                    }
                }
                if (dataBean.getDevice_data().getOther_device() != null) {
                    for (CompanyInfo.DataBeanX.DeviceDataBean.OtherDeviceBean deviceBean : dataBean.getDevice_data().getOther_device()) {
                        deviceList.add(deviceBean.getName() + " " + deviceBean.getData().getTotal());
                    }
                }
                company.setTypeList(typeList);
                company.setDeviceList(deviceList);
                companyList.add(company);

            }
        }
        adapter.notifyDataSetChanged();
    }
}
