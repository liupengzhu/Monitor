package cn.com.larunda.monitor.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.larunda.monitor.AlarmActivity;
import cn.com.larunda.monitor.CompanyListActivity;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.WorksheetActivity;
import cn.com.larunda.monitor.adapter.CompanyAdapter;
import cn.com.larunda.monitor.bean.Company;
import cn.com.larunda.monitor.bean.MaintenanceCompany;
import cn.com.larunda.monitor.gson.CompanyInfo;
import cn.com.larunda.monitor.gson.MaintenanceCompanyInfo;
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

/**
 * Created by sddt on 18-4-9.
 */

public class CompanyFragment extends Fragment {
    private final String COMPANY_URL = MyApplication.URL + "integrated_maint_company/detail" + MyApplication.TOKEN;
    private SharedPreferences preferences;
    private String token;
    private int id;
    private Button backButton;

    private HashMap<String, Integer> iconList = new HashMap<>();

    private List<Company> companyList = new ArrayList<>();
    private PullToLoadRecyclerView recyclerView;
    private CompanyAdapter adapter;
    private PTLLinearLayoutManager manager;

    private int page;
    private int maxPage;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout errorLayout;
    private FrameLayout layout;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_company_list, container, false);
        id = 0;
        initData();
        initView(view);
        initEvent();
        sendRequest();
        errorLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        return view;
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
    private void initView(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);

        backButton = view.findViewById(R.id.company_list_back);
        toolbar = view.findViewById(R.id.company_list_toolbar);
        toolbar.setTitle("");
        toolbar.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.company_list_recycler);
        manager = new PTLLinearLayoutManager();
        adapter = new CompanyAdapter(getContext(), companyList, iconList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setRefreshEnable(false);

        errorLayout = view.findViewById(R.id.company_list_error_layout);
        layout = view.findViewById(R.id.company_list_layout);

        refreshLayout = view.findViewById(R.id.company_list_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        adapter.setAlarmOnClickListener(new CompanyAdapter.AlarmOnClickListener() {
            @Override
            public void onClick(View v, int id) {
                Intent intent = new Intent(getContext(), AlarmActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        adapter.setMaintenanceOnClickListener(new CompanyAdapter.MaintenanceOnClickListener() {
            @Override
            public void onClick(View v, int id) {
                Intent intent = new Intent(getContext(), WorksheetActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        errorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        recyclerView.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onStartLoading(int skip) {
                sendLoadRequest();
            }
        });
    }


    /**
     * 发送网络请求
     */
    private void sendRequest() {
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(COMPANY_URL + token + "&maintenance_company_id=" + id
                + "&page=" + 1, new Callback() {
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
                                CompanyInfo info = Util.handleCompanyInfo(content);
                                if (info != null && info.getError() == null) {
                                    parseInfo(info);
                                    refreshLayout.setRefreshing(false);
                                    errorLayout.setVisibility(View.GONE);
                                    layout.setVisibility(View.VISIBLE);
                                } else {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
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

    private void parseInfo(CompanyInfo info) {
        page = info.getCurrent_page() + 1;
        maxPage = info.getLast_page();
        if (page > maxPage) {
            recyclerView.setNoMore(true);
        } else {
            recyclerView.setNoMore(false);
        }
        companyList.clear();
        if (info.getData() != null) {
            for (CompanyInfo.DataBeanX dataBean : info.getData()) {
                Company company = new Company();
                List<String> typeList = new ArrayList<>();
                List<String> deviceList = new ArrayList<>();
                List<MaintenanceCompany> maintenanceCompanyList = new ArrayList<>();
                if (dataBean.getCompany_pic() != null) {
                    company.setImg(MyApplication.IMG_URL + dataBean.getCompany_pic());
                }
                int normal = dataBean.getDevice_data().getDevice_total()
                        - dataBean.getDevice_data().getError_total();
                if (dataBean.getCompany_address() == null) {
                    company.setAddress("");
                } else {
                    company.setAddress(dataBean.getCompany_address());
                }
                company.setAlarm(dataBean.getAlarm_data() + "");
                company.setId(Integer.parseInt(dataBean.getCompany_id()));
                company.setElectric(dataBean.getDevice_data().getMeter_total_num() + "");
                company.setIndustry(dataBean.getCompany_industry());
                company.setMaintenance(dataBean.getMaintenance_num() + "");
                company.setName(dataBean.getCompany_name());
                if (dataBean.getCompany_tel() == null) {
                    company.setTel("");
                } else {
                    company.setTel(dataBean.getCompany_tel());
                }
                company.setTotal(normal + "");
                if (dataBean.getDevice_data().getDevice_total() != 0) {

                    company.setAngle((float) normal / (float) dataBean.getDevice_data().getDevice_total() * 360);
                }
                deviceList.add("安全用电" + " " + dataBean.getDevice_data().getMeter_total_num());
                if (dataBean.getDevice_data().getOther_device() != null) {
                    for (CompanyInfo.DataBeanX.DeviceDataBean.OtherDeviceBean deviceBean : dataBean.getDevice_data().getOther_device()) {
                        deviceList.add(deviceBean.getName() + " " + deviceBean.getData().getTotal());
                    }
                }
                if (dataBean.getMaintenanceCompanyInfoList() != null) {
                    for (MaintenanceCompanyInfo.DataBean dataBean1 : dataBean.getMaintenanceCompanyInfoList()) {
                        typeList.clear();
                        if (dataBean1.getMaintenance_type() != null) {
                            for (MaintenanceCompanyInfo.DataBean.MaintenanceTypeBean typeBean : dataBean1.getMaintenance_type()) {
                                typeList.add(typeBean.getName());
                            }
                        }
                        MaintenanceCompany maintenanceCompany = new MaintenanceCompany();
                        maintenanceCompany.setName(dataBean1.getName() + " ( " + dataBean1.getTel() + " ) ");
                        maintenanceCompany.setTypeList(typeList);
                        maintenanceCompanyList.add(maintenanceCompany);
                    }
                    if (dataBean.getMaintenanceCompanyInfoList().size() == 0) {
                        MaintenanceCompany maintenanceCompany = new MaintenanceCompany();
                        maintenanceCompany.setName("无");
                        maintenanceCompanyList.add(maintenanceCompany);
                    }
                }
                company.setDeviceList(deviceList);
                company.setMaintenanceCompanyList(maintenanceCompanyList);
                companyList.add(company);

            }
        }
        /*adapter.notifyDataSetChanged();*/
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    /**
     * 发送网络请求
     */
    private void sendLoadRequest() {
        HttpUtil.sendGetRequestWithHttp(COMPANY_URL + token + "&maintenance_company_id=" + id
                + "&page=" + page, new Callback() {
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
                                CompanyInfo info = Util.handleCompanyInfo(content);
                                if (info != null && info.getError() == null) {
                                    parseLoadInfo(info);
                                    refreshLayout.setRefreshing(false);
                                    errorLayout.setVisibility(View.GONE);
                                } else {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
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

    private void parseLoadInfo(CompanyInfo info) {
        page = info.getCurrent_page() + 1;
        maxPage = info.getLast_page();
        if (page > maxPage) {
            recyclerView.setNoMore(true);
        } else {
            recyclerView.setNoMore(false);
        }
        if (info.getData() != null) {
            for (CompanyInfo.DataBeanX dataBean : info.getData()) {
                Company company = new Company();
                List<String> typeList = new ArrayList<>();
                List<String> deviceList = new ArrayList<>();
                List<MaintenanceCompany> maintenanceCompanyList = new ArrayList<>();
                if (dataBean.getCompany_pic() != null) {
                    company.setImg(MyApplication.IMG_URL + dataBean.getCompany_pic());
                }
                int normal = dataBean.getDevice_data().getDevice_total()
                        - dataBean.getDevice_data().getError_total();
                if (dataBean.getCompany_address() == null) {
                    company.setAddress("");
                } else {
                    company.setAddress(dataBean.getCompany_address());
                }
                company.setAlarm(dataBean.getAlarm_data() + "");
                company.setId(Integer.parseInt(dataBean.getCompany_id()));
                company.setElectric(dataBean.getDevice_data().getMeter_total_num() + "");
                company.setIndustry(dataBean.getCompany_industry());
                company.setMaintenance(dataBean.getMaintenance_num() + "");
                company.setName(dataBean.getCompany_name());
                if (dataBean.getCompany_tel() == null) {
                    company.setTel("");
                } else {
                    company.setTel(dataBean.getCompany_tel());
                }
                company.setTotal(normal + "");
                if (dataBean.getDevice_data().getDevice_total() != 0) {

                    company.setAngle((float) normal / (float) dataBean.getDevice_data().getDevice_total() * 360);
                }
                deviceList.add("安全用电" + " " + dataBean.getDevice_data().getMeter_total_num());
                if (dataBean.getDevice_data().getOther_device() != null) {
                    for (CompanyInfo.DataBeanX.DeviceDataBean.OtherDeviceBean deviceBean : dataBean.getDevice_data().getOther_device()) {
                        deviceList.add(deviceBean.getName() + " " + deviceBean.getData().getTotal());
                    }
                }
                if (dataBean.getMaintenanceCompanyInfoList() != null) {
                    for (MaintenanceCompanyInfo.DataBean dataBean1 : dataBean.getMaintenanceCompanyInfoList()) {
                        typeList.clear();
                        if (dataBean1.getMaintenance_type() != null) {
                            for (MaintenanceCompanyInfo.DataBean.MaintenanceTypeBean typeBean : dataBean1.getMaintenance_type()) {
                                typeList.add(typeBean.getName());
                            }
                        }
                        MaintenanceCompany maintenanceCompany = new MaintenanceCompany();
                        maintenanceCompany.setName(dataBean1.getName() + " ( " + dataBean1.getTel() + " ) ");
                        maintenanceCompany.setTypeList(typeList);
                        maintenanceCompanyList.add(maintenanceCompany);
                    }
                    if (dataBean.getMaintenanceCompanyInfoList().size() == 0) {
                        MaintenanceCompany maintenanceCompany = new MaintenanceCompany();
                        maintenanceCompany.setName("无");
                        maintenanceCompanyList.add(maintenanceCompany);
                    }
                }
                company.setDeviceList(deviceList);
                company.setMaintenanceCompanyList(maintenanceCompanyList);
                companyList.add(company);

            }
        }
        /*adapter.notifyDataSetChanged();*/
        recyclerView.completeLoad(0);
    }
}
