package cn.com.larunda.monitor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.MainActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.MaintenanceFragmentAdapter;
import cn.com.larunda.monitor.util.CustomViewPager;
import cn.com.larunda.monitor.util.Util;

/**
 * Created by sddt on 18-3-13.
 */

public class MaintenanceFragment extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private MaintenanceFragmentAdapter adapter;

    private Button leftButton;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //保存view布局
        if (container.getTag(R.id.tag_first) == null) {
            view = inflater.inflate(R.layout.fragment_maintenance, container, false);
            init();
            initView(view);
            initEvent();
            container.setTag(R.id.tag_first, view);
        } else {
            view = (View) container.getTag(R.id.tag_first);
        }

        return view;
    }

    private void init() {
        fragmentList.add(new CompanyFragment());
        fragmentList.add(new MaintenanceWarningFragment());
        fragmentList.add(new MaintenanceWorksheetFragment());
        titleList.add("维保概况");
        titleList.add("维保警报");
        titleList.add("维保工单");
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        toolbar = view.findViewById(R.id.maintenance_toolbar);
        tabLayout = view.findViewById(R.id.maintenance_tabLayout);
        viewPager = view.findViewById(R.id.maintenance_viewPager);
        adapter = new MaintenanceFragmentAdapter(getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //设置下划线长度
        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                Util.setIndicator(tabLayout, 15, 15);
            }
        });

        leftButton = view.findViewById(R.id.maintenance_left_button);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        leftButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maintenance_left_button:
                MainActivity.drawerLayout.openDrawer(Gravity.START);
                break;
        }
    }
}
