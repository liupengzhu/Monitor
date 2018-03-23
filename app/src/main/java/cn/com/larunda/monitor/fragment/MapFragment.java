package cn.com.larunda.monitor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.com.larunda.monitor.MainActivity;
import cn.com.larunda.monitor.R;

/**
 * Created by sddt on 18-3-13.
 */

public class MapFragment extends Fragment implements View.OnClickListener {
    private Button leftButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
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
        leftButton = view.findViewById(R.id.map_left_button);
    }

    /*
    初始化点击事件
     */
    private void initEvent() {
        leftButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_left_button:
                MainActivity.drawerLayout.openDrawer(Gravity.START);
                break;
            default:
                break;
        }
    }
}
