package cn.com.larunda.monitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.adapter.CarbonFragmentAdapter;
import cn.com.larunda.monitor.adapter.GasFragmentAdapter;
import cn.com.larunda.monitor.fragment.CarbonFragment;
import cn.com.larunda.monitor.fragment.CarbonRankingFragment;
import cn.com.larunda.monitor.fragment.GasFragment;
import cn.com.larunda.monitor.fragment.GasRankingFragment;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.CustomViewPager;
import cn.com.larunda.monitor.util.Util;

public class CarbonActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private CarbonFragmentAdapter adapter;

    private Button backButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon);
        init();
        initView();
        initEvent();
        setSupportActionBar(toolbar);
    }

    /**
     * 初始化信息
     */
    private void init() {
        fragmentList.add(new CarbonFragment());
        fragmentList.add(new CarbonRankingFragment());
        titleList.add("实际排放量");
        titleList.add("排放量排行");
    }

    /**
     * 初始化view
     */
    private void initView() {

        backButton = findViewById(R.id.carbon_back_button);

        tabLayout = findViewById(R.id.carbon_tabLayout);
        viewPager = findViewById(R.id.carbon_viewPager);
        adapter = new CarbonFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //设置下划线长度
        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                Util.setIndicator(tabLayout, 50, 50);
            }
        });
        toolbar = findViewById(R.id.carbon_toolbar);
        toolbar.setTitle("");
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carbon_back_button:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.carbon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.carbon_menu_electric:
                Intent electricIntent = new Intent(this, ElectricActivity.class);
                startActivity(electricIntent);
                finish();
                break;
            case R.id.carbon_menu_water:
                Intent waterIntent = new Intent(this, WaterActivity.class);
                startActivity(waterIntent);
                finish();
                break;
            case R.id.carbon_menu_steam:
                Intent steamIntent = new Intent(this, SteamActivity.class);
                startActivity(steamIntent);
                finish();
                break;
            case R.id.carbon_menu_gas:
                Intent gasIntent = new Intent(this, GasActivity.class);
                startActivity(gasIntent);
                finish();
                break;
            case R.id.carbon_menu_renewable:
                Intent renewableIntent = new Intent(this, RenewableActivity.class);
                startActivity(renewableIntent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 反射修改弹出菜单显示图标
     *
     * @param view
     * @param menu
     * @return
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
}
