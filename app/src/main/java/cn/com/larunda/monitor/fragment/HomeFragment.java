package cn.com.larunda.monitor.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.MainActivity;
import cn.com.larunda.monitor.PowerActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.gson.HomeInfo;
import cn.com.larunda.monitor.gson.UnitInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sddt on 18-3-13.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final String HOME_URL = MyApplication.URL + "home/data_screen" + MyApplication.TOKEN;
    private SharedPreferences preferences;
    private String token;
    private TextView powerText;
    private TextView waterText;
    private TextView steamText;
    private TextView gasText;
    private TextView energyText;
    private TextView carbonText;
    private TextView powerGeneratedText;
    private TextView safeDayText;
    private TextView powerUnitText;
    private TextView waterUnitText;
    private TextView steamUnitText;
    private TextView gasUnitText;
    private TextView energyUnitText;
    private TextView carbonUnitText;
    private TextView powerGeneratedUnitText;
    private TextView safeDayUnitText;
    private String unit;

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout layout;
    private LinearLayout errorLayout;

    private Button leftButton;

    private RelativeLayout button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);
        unit = preferences.getString("unit", null);
        powerText = view.findViewById(R.id.home_item_text_1);
        waterText = view.findViewById(R.id.home_item_text_2);
        steamText = view.findViewById(R.id.home_item_text_3);
        gasText = view.findViewById(R.id.home_item_text_4);
        energyText = view.findViewById(R.id.home_item_text_5);
        carbonText = view.findViewById(R.id.home_item_text_6);
        powerGeneratedText = view.findViewById(R.id.home_item_text_7);
        safeDayText = view.findViewById(R.id.home_item_text_8);
        powerUnitText = view.findViewById(R.id.home_item_basis_text_1);
        waterUnitText = view.findViewById(R.id.home_item_basis_text_2);
        steamUnitText = view.findViewById(R.id.home_item_basis_text_3);
        gasUnitText = view.findViewById(R.id.home_item_basis_text_4);
        energyUnitText = view.findViewById(R.id.home_item_basis_text_5);
        carbonUnitText = view.findViewById(R.id.home_item_basis_text_6);
        powerGeneratedUnitText = view.findViewById(R.id.home_item_basis_text_7);
        safeDayUnitText = view.findViewById(R.id.home_item_basis_text_8);

        layout = view.findViewById(R.id.home_layout);
        errorLayout = view.findViewById(R.id.home_error_layout);

        refreshLayout = view.findViewById(R.id.home_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });

        leftButton = view.findViewById(R.id.home_left_button);

        button = view.findViewById(R.id.home_button);
    }

    @Override
    public void onStart() {
        super.onStart();
        sendRequest();
        layout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    /**
     * 发送数据请求
     */
    private void sendRequest() {
        refreshLayout.setRefreshing(true);
        HttpUtil.sendGetRequestWithHttp(HOME_URL + token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                            layout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    final HomeInfo homeInfo = Util.handleHomeInfo(content);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (homeInfo != null && homeInfo.getError() == null) {
                                    parseHomeInfo(homeInfo);
                                    refreshLayout.setRefreshing(false);
                                    layout.setVisibility(View.VISIBLE);
                                    errorLayout.setVisibility(View.GONE);
                                } else {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.putExtra("token_timeout", "登录超时");
                                    preferences.edit().putString("token", null).commit();
                                    startActivity(intent);
                                    ActivityCollector.finishAllActivity();
                                }
                            }
                        });
                    }

                }
            }
        });
    }

    /**
     * 解析首页数据
     *
     * @param homeInfo
     */
    private void parseHomeInfo(HomeInfo homeInfo) {
        powerText.setText(homeInfo.getPower().getData() + "");
        waterText.setText(homeInfo.getWater() + "");
        steamText.setText(homeInfo.getSteam() + "");
        gasText.setText(homeInfo.getGas() + "");
        energyText.setText(homeInfo.getEnergy() + "");
        carbonText.setText(homeInfo.getCarbon() + "");
        powerGeneratedText.setText(homeInfo.getPower_generated().getData() + "");
        safeDayText.setText(homeInfo.getSafe_day() + "");

        if (unit != null && Util.isGoodJson(unit)) {
            UnitInfo info = Util.handleUnitInfo(unit);
            powerUnitText.setText("单位:" + homeInfo.getPower().getRatio() + info.getPower());
            waterUnitText.setText("单位:" + info.getWater_usage());
            steamUnitText.setText("单位:" + info.getSteam_usage());
            gasUnitText.setText("单位:" + info.getGas_usage());
            energyUnitText.setText("单位:" + info.getEnergy_usage());
            carbonUnitText.setText("单位:" + info.getCarbon_emissions());
            powerGeneratedUnitText.setText("单位:" + homeInfo.getPower_generated().getRatio() + info.getPower());
            safeDayUnitText.setText("单位:天");

        }

    }

    private void initEvent() {
        leftButton.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_left_button:
                MainActivity.drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.home_button:
                Intent intent = new Intent(getContext(), PowerActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
