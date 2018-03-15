package cn.com.larunda.monitor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.adapter.ElectricFragmentAdapter;
import cn.com.larunda.monitor.fragment.ElectricFragment;
import cn.com.larunda.monitor.fragment.ElectricRankingFragment;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.CustomViewPager;
import cn.com.larunda.monitor.util.Util;

public class ElectricActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private ElectricFragmentAdapter adapter;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric);
        init();
        initView();
        initEvent();
    }

    /**
     * 初始化信息
     */
    private void init() {
        fragmentList.add(new ElectricFragment());
        fragmentList.add(new ElectricRankingFragment());
        titleList.add("区间用电量");
        titleList.add("耗电排行");
    }

    /**
     * 初始化view
     */
    private void initView() {

        backButton = findViewById(R.id.electric_back_button);

        tabLayout = findViewById(R.id.electric_tabLayout);
        viewPager = findViewById(R.id.electric_viewPager);
        adapter = new ElectricFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
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
            case R.id.electric_back_button:
                finish();
                break;
            default:
                break;
        }
    }
}
