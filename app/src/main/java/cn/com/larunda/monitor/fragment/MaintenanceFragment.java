package cn.com.larunda.monitor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.GasFragmentAdapter;
import cn.com.larunda.monitor.adapter.MaintenanceFragmentAdapter;
import cn.com.larunda.monitor.util.CustomViewPager;
import cn.com.larunda.monitor.util.Util;

/**
 * Created by sddt on 18-3-13.
 */

public class MaintenanceFragment extends Fragment {
    Toolbar toolbar;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private MaintenanceFragmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //保存view布局
        View view;
        if (container.getTag(R.id.tag_first) == null) {
            view = inflater.inflate(R.layout.fragment_maintenance, container, false);
            container.setTag(R.id.tag_first, view);
        } else {
            view = (View) container.getTag(R.id.tag_first);
        }
        init();
        initView(view);
        initEvent();
        return view;
    }

    private void init() {
        fragmentList.add(new MaintenanceSurveyFragment());
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
        adapter = new MaintenanceFragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //设置下划线长度
        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                Util.setIndicator(tabLayout, 15, 15);
            }
        });
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {

    }
}
