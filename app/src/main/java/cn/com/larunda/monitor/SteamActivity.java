package cn.com.larunda.monitor;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.adapter.GasFragmentAdapter;
import cn.com.larunda.monitor.adapter.SteamFragmentAdapter;
import cn.com.larunda.monitor.fragment.GasFragment;
import cn.com.larunda.monitor.fragment.GasRankingFragment;
import cn.com.larunda.monitor.fragment.SteamFragment;
import cn.com.larunda.monitor.fragment.SteamRankingFragment;
import cn.com.larunda.monitor.util.CustomViewPager;
import cn.com.larunda.monitor.util.Util;

public class SteamActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private SteamFragmentAdapter adapter;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam);
        init();
        initView();
        initEvent();
    }

    /**
     * 初始化信息
     */
    private void init() {
        fragmentList.add(new SteamFragment());
        fragmentList.add(new SteamRankingFragment());
        titleList.add("区间用汽量");
        titleList.add("耗汽排行");
    }

    /**
     * 初始化view
     */
    private void initView() {

        backButton = findViewById(R.id.steam_back_button);

        tabLayout = findViewById(R.id.steam_tabLayout);
        viewPager = findViewById(R.id.steam_viewPager);
        adapter = new SteamFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //设置下划线长度
        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                Util.setIndicator(tabLayout, 50, 50);
            }
        });
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
            case R.id.steam_back_button:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
