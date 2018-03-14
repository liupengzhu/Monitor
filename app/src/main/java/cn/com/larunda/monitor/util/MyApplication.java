package cn.com.larunda.monitor.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;


public class MyApplication extends Application {

    public static final String VERSION = "V1.0.1";
    public static final String URL = "http://kunshan.dsmcase.com:90/api/";
    public static final String TOKEN = "?_token=";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        LitePal.initialize(context);

    }

    public static Context getContext() {
        return context;
    }


}
