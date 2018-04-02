package cn.com.larunda.monitor.util;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import cn.com.larunda.monitor.gson.CarbonInfo;
import cn.com.larunda.monitor.gson.CompanyInfo;
import cn.com.larunda.monitor.gson.CompanyRankInfo;
import cn.com.larunda.monitor.gson.DayElectricInfo;
import cn.com.larunda.monitor.gson.DayRenewableInfo;
import cn.com.larunda.monitor.gson.ElectricInfo;
import cn.com.larunda.monitor.gson.FilterCompanyInfo;
import cn.com.larunda.monitor.gson.FilterCompanyWorksheetInfo;
import cn.com.larunda.monitor.gson.GasInfo;
import cn.com.larunda.monitor.gson.HomeInfo;
import cn.com.larunda.monitor.gson.MaintenanceCompanyInfo;
import cn.com.larunda.monitor.gson.MapInfo;
import cn.com.larunda.monitor.gson.MessageInfo;
import cn.com.larunda.monitor.gson.PowerUsageInfo;
import cn.com.larunda.monitor.gson.RankCompanyInfo;
import cn.com.larunda.monitor.gson.RenewableInfo;
import cn.com.larunda.monitor.gson.RenewableRankInfo;
import cn.com.larunda.monitor.gson.SteamInfo;
import cn.com.larunda.monitor.gson.UnitInfo;
import cn.com.larunda.monitor.gson.UserToken;
import cn.com.larunda.monitor.gson.WarningInfo;
import cn.com.larunda.monitor.gson.WaterInfo;
import cn.com.larunda.monitor.gson.WorksheetInfo;

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

    public static String formatNum(Object num) {
        DecimalFormat format = new DecimalFormat("0.00");
        String newNum = format.format(num);
        return newNum;
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

    public static ElectricInfo handleElectricInfo(String response) {
        Gson gson = new Gson();
        ElectricInfo electricInfo = gson.fromJson(response, ElectricInfo.class);
        return electricInfo;
    }

    public static DayElectricInfo handleDayElectricInfo(String response) {
        Gson gson = new Gson();
        DayElectricInfo dayelectricInfo = gson.fromJson(response, DayElectricInfo.class);
        return dayelectricInfo;
    }

    public static WaterInfo handleWaterInfo(String response) {
        Gson gson = new Gson();
        WaterInfo waterInfo = gson.fromJson(response, WaterInfo.class);
        return waterInfo;
    }

    public static GasInfo handleGasInfo(String response) {
        Gson gson = new Gson();
        GasInfo gasInfo = gson.fromJson(response, GasInfo.class);
        return gasInfo;
    }

    public static SteamInfo handleSteamInfo(String response) {
        Gson gson = new Gson();
        SteamInfo steamInfo = gson.fromJson(response, SteamInfo.class);
        return steamInfo;
    }

    public static RankCompanyInfo handleRankCompanyInfo(String response) {
        Gson gson = new Gson();
        RankCompanyInfo rankCompanyInfo = gson.fromJson(response, RankCompanyInfo.class);
        return rankCompanyInfo;
    }

    public static CarbonInfo handleCarbonInfo(String response) {
        Gson gson = new Gson();
        CarbonInfo carbonInfo = gson.fromJson(response, CarbonInfo.class);
        return carbonInfo;
    }

    public static RenewableInfo handleRenewableInfo(String response) {
        Gson gson = new Gson();
        RenewableInfo renewableInfo = gson.fromJson(response, RenewableInfo.class);
        return renewableInfo;
    }

    public static DayRenewableInfo handleDayRenewableInfo(String response) {
        Gson gson = new Gson();
        DayRenewableInfo renewableInfo = gson.fromJson(response, DayRenewableInfo.class);
        return renewableInfo;
    }

    public static RenewableRankInfo handleRenewableRankInfo(String response) {
        Gson gson = new Gson();
        RenewableRankInfo renewableRankInfo = gson.fromJson(response, RenewableRankInfo.class);
        return renewableRankInfo;
    }

    public static MaintenanceCompanyInfo handleMaintenanceCompanyInfo(String response) {
        Gson gson = new Gson();
        MaintenanceCompanyInfo info = gson.fromJson(response, MaintenanceCompanyInfo.class);
        return info;
    }

    public static CompanyInfo handleCompanyInfo(String response) {
        Gson gson = new Gson();
        CompanyInfo info = gson.fromJson(response, CompanyInfo.class);
        return info;
    }

    public static WarningInfo handleWarningInfo(String response) {
        Gson gson = new Gson();
        WarningInfo warningInfo = gson.fromJson(response, WarningInfo.class);
        return warningInfo;
    }

    public static FilterCompanyWorksheetInfo handleFilterCompanyWorksheetInfo(String response) {
        Gson gson = new Gson();
        FilterCompanyWorksheetInfo info = gson.fromJson(response, FilterCompanyWorksheetInfo.class);
        return info;
    }

    public static FilterCompanyInfo handleFilterCompanyInfo(String response) {
        Gson gson = new Gson();
        FilterCompanyInfo info = gson.fromJson(response, FilterCompanyInfo.class);
        return info;
    }

    public static WorksheetInfo handleWorksheetInfo(String response) {
        Gson gson = new Gson();
        WorksheetInfo worksheetInfo = gson.fromJson(response, WorksheetInfo.class);
        return worksheetInfo;
    }

    public static MessageInfo handleMessageInfo(String response) {
        Gson gson = new Gson();
        MessageInfo messageInfo = gson.fromJson(response, MessageInfo.class);
        return messageInfo;
    }

    public static PowerUsageInfo handlePowerUsageInfo(String response) {
        Gson gson = new Gson();
        PowerUsageInfo info = gson.fromJson(response, PowerUsageInfo.class);
        return info;
    }

    public static CompanyRankInfo handleCompanyRankInfo(String response) {
        Gson gson = new Gson();
        CompanyRankInfo info = gson.fromJson(response, CompanyRankInfo.class);
        return info;
    }

    public static MapInfo handleMapInfo(String response) {
        Gson gson = new Gson();
        MapInfo info = gson.fromJson(response, MapInfo.class);
        return info;
    }
}
