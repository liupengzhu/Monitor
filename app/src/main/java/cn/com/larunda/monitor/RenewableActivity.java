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
import cn.com.larunda.monitor.adapter.RenewableFragmentAdapter;
import cn.com.larunda.monitor.fragment.GasFragment;
import cn.com.larunda.monitor.fragment.GasRankingFragment;
import cn.com.larunda.monitor.fragment.RenewableFragment;
import cn.com.larunda.monitor.fragment.RenewableRankingFragment;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.CustomViewPager;
import cn.com.larunda.monitor.util.Util;

public class RenewableActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private RenewableFragmentAdapter adapter;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renewable);
        init();
        initView();
        initEvent();
    }

    /**
     * 初始化信息
     */
    private void init() {
        fragmentList.add(new RenewableFragment());
        fragmentList.add(new RenewableRankingFragment());
        titleList.add("区间发电量");
        titleList.add("发电排行");
    }

    /**
     * 初始化view
     */
    private void initView() {

        backButton = findViewById(R.id.renewable_back_button);

        tabLayout = findViewById(R.id.renewable_tabLayout);
        viewPager = findViewById(R.id.renewable_viewPager);
        adapter = new RenewableFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
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
            case R.id.renewable_back_button:
                finish();
                break;
            default:
                break;
        }
    }
}
