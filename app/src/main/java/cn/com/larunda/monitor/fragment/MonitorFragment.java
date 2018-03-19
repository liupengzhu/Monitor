package cn.com.larunda.monitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.com.larunda.monitor.ElectricActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.WaterActivity;

/**
 * Created by sddt on 18-3-13.
 */

public class MonitorFragment extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    LinearLayout electricButton;
    LinearLayout waterButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        initView(view);
        initEvent();
        return view;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        toolbar = view.findViewById(R.id.monitor_toolbar);
        electricButton = view.findViewById(R.id.monitor_button1);
        waterButton = view.findViewById(R.id.monitor_button2);

    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        electricButton.setOnClickListener(this);
        waterButton.setOnClickListener(this);
    }

    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monitor_button1:
                Intent electricIntent = new Intent(getContext(), ElectricActivity.class);
                startActivity(electricIntent);
                break;
            case R.id.monitor_button2:
                Intent waterIntent = new Intent(getContext(), WaterActivity.class);
                startActivity(waterIntent);
                break;
            default:
                break;
        }

    }
}
