package cn.com.larunda.monitor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import cn.com.larunda.monitor.R;

/**
 * Created by sddt on 18-3-21.
 */

public class MaintenanceWarningFragment extends Fragment {
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_warning, container, false);
        initView(view);
        return view;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        spinner = view.findViewById(R.id.maintenance_warning_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner_showed) {

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        adapter.setDropDownViewResource(R.layout.item_spinner_option);
        adapter.add("处理中");
        adapter.add("未开始");
        adapter.add("警报状态");
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
    }
}
