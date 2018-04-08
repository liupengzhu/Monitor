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

import cn.com.larunda.monitor.CarbonActivity;
import cn.com.larunda.monitor.ElectricActivity;
import cn.com.larunda.monitor.GasActivity;
import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.MainActivity;
import cn.com.larunda.monitor.PowerActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.RenewableActivity;
import cn.com.larunda.monitor.SteamActivity;
import cn.com.larunda.monitor.WaterActivity;
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

    private SwipeRefreshLayout refreshLayout;
    private LinearLayout layout;
    private LinearLayout errorLayout;

    private Button leftButton;

    private RelativeLayout button;

    LinearLayout electricButton;
    LinearLayout waterButton;
    LinearLayout gasButton;
    LinearLayout steamButton;
    LinearLayout carbonButton;
    LinearLayout renewableButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        initEvent();
        String content = preferences.getString("home_info", null);
        if (content != null) {
            HomeInfo homeInfo = Util.handleHomeInfo(content);
            parseHomeInfo(homeInfo);
        } else {
            sendRequest();
            layout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        }
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

        electricButton = view.findViewById(R.id.home_button1);
        waterButton = view.findViewById(R.id.home_button2);
        gasButton = view.findViewById(R.id.home_button3);
        steamButton = view.findViewById(R.id.home_button4);
        renewableButton = view.findViewById(R.id.home_button5);
        carbonButton = view.findViewById(R.id.home_button6);
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
                final String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    final HomeInfo homeInfo = Util.handleHomeInfo(content);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (homeInfo != null && homeInfo.getError() == null) {
                                    parseHomeInfo(homeInfo);
                                    preferences.edit().putString("home_info", content).commit();
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
        powerText.setText(Util.formatNum(homeInfo.getPower().getData()) + "");
        waterText.setText(Util.formatNum(homeInfo.getWater()) + "");
        steamText.setText(Util.formatNum(homeInfo.getSteam()) + "");
        gasText.setText(Util.formatNum(homeInfo.getGas()) + "");
        energyText.setText(Util.formatNum(homeInfo.getEnergy()) + "");
        carbonText.setText(Util.formatNum(homeInfo.getCarbon()) + "");
        powerGeneratedText.setText(Util.formatNum(homeInfo.getPower_generated().getData()) + "");
        safeDayText.setText(homeInfo.getSafe_day() + "");


        powerUnitText.setText("单位:" + homeInfo.getPower().getRatio()
                + preferences.getString("power_unit", null));
        waterUnitText.setText("单位:" + preferences.getString("water_unit", null));
        steamUnitText.setText("单位:" + preferences.getString("steam_unit", null));
        gasUnitText.setText("单位:" + preferences.getString("gas_unit", null));
        energyUnitText.setText("单位:" + preferences.getString("energy_unit", null));
        carbonUnitText.setText("单位:" + preferences.getString("carbon_unit", null));
        powerGeneratedUnitText.setText("单位:" + homeInfo.getPower_generated().getRatio()
                + preferences.getString("power_unit", null));
        safeDayUnitText.setText("单位:天");

    }

    private void initEvent() {
        leftButton.setOnClickListener(this);
        button.setOnClickListener(this);
        errorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        electricButton.setOnClickListener(this);
        waterButton.setOnClickListener(this);
        gasButton.setOnClickListener(this);
        steamButton.setOnClickListener(this);
        renewableButton.setOnClickListener(this);
        carbonButton.setOnClickListener(this);
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
            case R.id.home_button1:
                Intent electricIntent = new Intent(getContext(), ElectricActivity.class);
                startActivity(electricIntent);
                break;
            case R.id.home_button2:
                Intent waterIntent = new Intent(getContext(), WaterActivity.class);
                startActivity(waterIntent);
                break;
            case R.id.home_button3:
                Intent gasIntent = new Intent(getContext(), GasActivity.class);
                startActivity(gasIntent);
                break;
            case R.id.home_button4:
                Intent steamIntent = new Intent(getContext(), SteamActivity.class);
                startActivity(steamIntent);
                break;
            case R.id.home_button5:
                Intent renewableIntent = new Intent(getContext(), RenewableActivity.class);
                startActivity(renewableIntent);
                break;
            case R.id.home_button6:
                Intent carbonIntent = new Intent(getContext(), CarbonActivity.class);
                startActivity(carbonIntent);
                break;
            default:
                break;
        }
    }
}
