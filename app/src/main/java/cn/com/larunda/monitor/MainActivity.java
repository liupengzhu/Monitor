package cn.com.larunda.monitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.adapter.HomeAdapter;
import cn.com.larunda.monitor.fragment.HomeFragment;
import cn.com.larunda.monitor.fragment.MaintenanceFragment;
import cn.com.larunda.monitor.fragment.MapFragment;
import cn.com.larunda.monitor.fragment.MonitorFragment;
import cn.com.larunda.monitor.gson.UnitInfo;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.CustomViewPager;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private String[] titles = {"数据总览", "综合监控", "用能地图", "事件总览"};
    private int[] icons = {R.drawable.home_icon2, R.drawable.monitor_icon2, R.drawable.map_icon2,
            R.drawable.maintenance_icon2};
    private HomeAdapter homeAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private final String UNIT_URL = MyApplication.URL + "config/unit";
    public static String token;
    private String unit;
    public static DrawerLayout drawerLayout;

    private Button cancelButton;
    private Button rePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTabs();
        unit = preferences.getString("unit", null);
        initEvent();
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        cancelButton.setOnClickListener(this);
        rePasswordButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (unit == null) {
            sendRequest();
        } else {
            if (Util.isGoodJson(unit)) {
                UnitInfo info = Util.handleUnitInfo(unit);
                editor.putString("power_unit", info.getPower()).commit();
                editor.putString("water_unit", info.getWater_usage()).commit();
                editor.putString("steam_unit", info.getSteam_usage()).commit();
                editor.putString("gas_unit", info.getGas_usage()).commit();
                editor.putString("energy_unit", info.getEnergy_usage()).commit();
                editor.putString("carbon_unit", info.getCarbon_emissions()).commit();
                editor.putString("installed_capacity_unit", info.getInstalled_capacity()).commit();
            }
        }
    }

    private void sendRequest() {
        HttpUtil.sendGetRequestWithHttp(UNIT_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    editor.putString("unit", content).commit();
                    UnitInfo info = Util.handleUnitInfo(content);
                    editor.putString("power_unit", info.getPower()).commit();
                    editor.putString("water_unit", info.getWater_usage()).commit();
                    editor.putString("steam_unit", info.getSteam_usage()).commit();
                    editor.putString("gas_unit", info.getGas_usage()).commit();
                    editor.putString("energy_unit", info.getEnergy_usage()).commit();
                    editor.putString("carbon_unit", info.getCarbon_emissions()).commit();
                    editor.putString("installed_capacity_unit", info.getInstalled_capacity()).commit();
                }
            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        fragments.add(new HomeFragment());
        fragments.add(new MonitorFragment());
        fragments.add(new MapFragment());
        fragments.add(new MaintenanceFragment());
        homeAdapter = new HomeAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(homeAdapter);
        tabLayout.setupWithViewPager(viewPager);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        token = preferences.getString("token", null);

        cancelButton = findViewById(R.id.user_menu_cancel);
        rePasswordButton = findViewById(R.id.user_menu_re_password);
    }

    /**
     * 初始化tab
     */
    private void initTabs() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(getView(i));
        }
        //tablayout切换监听事件
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        tab.getCustomView().findViewById(R.id.tab_image_view).setBackgroundResource(R.drawable.home_icon1);
                        break;
                    case 1:
                        tab.getCustomView().findViewById(R.id.tab_image_view).setBackgroundResource(R.drawable.monitor_icon1);
                        break;
                    case 2:
                        tab.getCustomView().findViewById(R.id.tab_image_view).setBackgroundResource(R.drawable.map_icon1);
                        break;
                    case 3:
                        tab.getCustomView().findViewById(R.id.tab_image_view).setBackgroundResource(R.drawable.maintenance_icon1);
                        break;
                    default:
                        break;
                }
                TextView textView = tab.getCustomView().findViewById(R.id.tab_text_view);
                textView.setTextColor(getResources().getColor(R.color.tab_text_color1));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


                switch (tab.getPosition()) {
                    case 0:
                        tab.getCustomView().findViewById(R.id.tab_image_view).setBackgroundResource(R.drawable.home_icon2);
                        break;
                    case 1:
                        tab.getCustomView().findViewById(R.id.tab_image_view).setBackgroundResource(R.drawable.monitor_icon2);
                        break;
                    case 2:
                        tab.getCustomView().findViewById(R.id.tab_image_view).setBackgroundResource(R.drawable.map_icon2);
                        break;
                    case 3:
                        tab.getCustomView().findViewById(R.id.tab_image_view).setBackgroundResource(R.drawable.maintenance_icon2);
                        break;
                    default:
                        break;
                }
                TextView textView = tab.getCustomView().findViewById(R.id.tab_text_view);
                textView.setTextColor(getResources().getColor(R.color.tab_text_color2));

            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

    }

    //获取position位置的tab的子控件
    public View getView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_list, null);
        ImageView imageView = view.findViewById(R.id.tab_image_view);
        TextView textView = view.findViewById(R.id.tab_text_view);
        imageView.setBackgroundResource(icons[position]);
        textView.setText(titles[position]);
        if (position == 0) {
            imageView.setBackgroundResource(R.drawable.home_icon1);
            textView.setTextColor(getResources().getColor(R.color.tab_text_color1));

        }
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_menu_cancel:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                editor.putString("token", null).commit();
                finish();
                break;
            case R.id.user_menu_re_password:
                Intent intent1 = new Intent(MainActivity.this, RePasswordActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
