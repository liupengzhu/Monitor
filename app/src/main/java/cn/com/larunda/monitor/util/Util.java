package cn.com.larunda.monitor.util;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import cn.com.larunda.monitor.gson.DayElectricInfo;
import cn.com.larunda.monitor.gson.ElectricInfo;
import cn.com.larunda.monitor.gson.HomeInfo;
import cn.com.larunda.monitor.gson.UnitInfo;
import cn.com.larunda.monitor.gson.UserToken;

/**
 * Created by sddt on 18-3-14.
 */

public class Util {

    /**
     * 判断是否是json对象
     *
     * @param json
     * @return
     */
    public static boolean isGoodJson(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    /**
     * 设置tablayout下划线宽度的方法
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }

    public static String parseTime(long time, int type) {
        String date = "";
        switch (type) {
            case 1:
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy");
                date = formatter1.format(time);
                break;
            case 2:
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM");
                date = formatter2.format(time);
                break;
            case 3:
                SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd");
                date = formatter3.format(time);
                break;
        }
        return date;
    }

    public static UserToken handleLoginInfo(String response) {
        Gson gson = new Gson();
        UserToken userToken = gson.fromJson(response, UserToken.class);
        return userToken;
    }

    public static HomeInfo handleHomeInfo(String response) {
        Gson gson = new Gson();
        HomeInfo homeInfo = gson.fromJson(response, HomeInfo.class);
        return homeInfo;
    }

    public static UnitInfo handleUnitInfo(String response) {
        Gson gson = new Gson();
        UnitInfo unitInfo = gson.fromJson(response, UnitInfo.class);
        return unitInfo;
    }

    public static ElectricInfo handleElectricInfo(String response){
        Gson gson = new Gson();
        ElectricInfo electricInfo = gson.fromJson(response,ElectricInfo.class);
        return electricInfo;
    }

    public static DayElectricInfo handleDayElectricInfo(String response){
        Gson gson = new Gson();
        DayElectricInfo dayelectricInfo = gson.fromJson(response,DayElectricInfo.class);
        return dayelectricInfo;
    }
}
