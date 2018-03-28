package cn.com.larunda.monitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

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

import cn.com.larunda.monitor.adapter.WaterFragmentAdapter;
import cn.com.larunda.monitor.fragment.WaterFragment;
import cn.com.larunda.monitor.fragment.WaterRankingFragment;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.CustomViewPager;
import cn.com.larunda.monitor.util.Util;

public class WaterActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private WaterFragmentAdapter adapter;

    private Button backButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        init();
        initView();
        initEvent();
        setSupportActionBar(toolbar);
    }

    /**
     * 初始化信息
     */
    private void init() {
        fragmentList.add(new WaterFragment());
        fragmentList.add(new WaterRankingFragment());
        titleList.add("区间用水量");
        titleList.add("耗水排行");
    }

    /**
     * 初始化view
     */
    private void initView() {

        backButton = findViewById(R.id.water_back_button);

        tabLayout = findViewById(R.id.water_tabLayout);
        viewPager = findViewById(R.id.water_viewPager);
        adapter = new WaterFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //设置下划线长度
        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                Util.setIndicator(tabLayout, 50, 50);
            }
        });

        toolbar = findViewById(R.id.water_toolbar);
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
            case R.id.water_back_button:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.water_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.water_menu_electric:
                Intent electricIntent = new Intent(this, ElectricActivity.class);
                startActivity(electricIntent);
                finish();
                break;
            case R.id.water_menu_steam:
                Intent steamIntent = new Intent(this, SteamActivity.class);
                startActivity(steamIntent);
                finish();
                break;
            case R.id.water_menu_gas:
                Intent gasIntent = new Intent(this, GasActivity.class);
                startActivity(gasIntent);
                finish();
                break;
            case R.id.water_menu_renewable:
                Intent renewableIntent = new Intent(this, RenewableActivity.class);
                startActivity(renewableIntent);
                finish();
                break;
            case R.id.water_menu_carbon:
                Intent carbonIntent = new Intent(this, CarbonActivity.class);
                startActivity(carbonIntent);
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
