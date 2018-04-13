package cn.com.larunda.monitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.larunda.monitor.gson.UnitInfo;
import cn.com.larunda.monitor.gson.UserToken;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOGIN_NAME = "login_name";
    public static final String LOGIN_PASSWORD = "login_password";
    private EditText loginName;
    private EditText loginPassword;
    private CheckBox checkBox;
    private Button loginButton;
    private Button loginButtonDemo;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private String LOGIN_URL;
    private String UNIT_URL;
    private boolean isLogin = false;
    private String person = "李俊";
    private String tel = "18762870876";
    private LinearLayout dialog;
    private TextView dialogText;
    private Timer timer;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        //若果token不为空 则直接进入主界面
        if (preferences.getString("token", null) != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //从数据库获取登录名和密码并设置到界面
        String login_Name = preferences.getString(LOGIN_NAME, null);
        String login_Password = preferences.getString(LOGIN_PASSWORD, null);

        String token_time = getIntent().getStringExtra("token_timeout");


        if (login_Name != null && login_Password != null) {
            checkBox.setChecked(true);
            loginName.setText(login_Name);
            loginPassword.setText(login_Password);
            loginName.setSelection(login_Name.length());
            loginPassword.setSelection(login_Password.length());

        } else if (login_Name != null) {
            loginName.setText(login_Name);
            loginName.setSelection(login_Name.length());
        }
        if (token_time != null) {
            Toast.makeText(this, "登录超时,请重新登录", Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if (preferences.getString("unit", null) == null) {
            sendRequest();
        }
    }*/

    /*
     * 加载单位接口

    private void sendRequest() {
        HttpUtil.sendGetRequestWithHttp(UNIT_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    editor.putString("unit", content).commit();
                }
            }
        });
    }*/

    /**
     * 初始化view
     */
    private void initView() {
        loginName = findViewById(R.id.login_name);
        loginPassword = findViewById(R.id.login_password);
        checkBox = findViewById(R.id.check_button);
        loginButton = findViewById(R.id.login_button);
        loginButtonDemo = findViewById(R.id.login_button2);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        dialog = findViewById(R.id.login_dialog);
        dialogText = findViewById(R.id.login_dialog_text);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        loginButton.setOnClickListener(this);
        loginButtonDemo.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                if (!isLogin) {
                    MyApplication.URL = "http://ksdy_db.dsmcase.com:90/api/";
                    MyApplication.IMG_URL = "http://ksdy_db.dsmcase.com:90";
                    LOGIN_URL = "http://ksdy_db.dsmcase.com:90/api/login";
                    UNIT_URL = "http://ksdy_db.dsmcase.com:90/api/config/unit";
                    isLogin = true;
                    showDialog();
                    login();
                }
                break;
            case R.id.login_button2:
                if (!isLogin) {
                    MyApplication.URL = "http://ksdy_demo.dsmcase.com:90/api/";
                    MyApplication.IMG_URL = "http://ksdy_demo.dsmcase.com:90";
                    LOGIN_URL = "http://ksdy_demo.dsmcase.com:90/api/login";
                    UNIT_URL = "http://ksdy_demo.dsmcase.com:90/config/unit";
                    isLogin = true;
                    showDialog();
                    login();
                }
                break;
            default:
                break;
        }
    }

    private void login() {
        String username = loginName.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(password)) {

            if (checkBox.isChecked()) {
                editor.putString(LOGIN_NAME, username);
                editor.putString(LOGIN_PASSWORD, password);
                editor.apply();
                loginServer(username, password);

            } else {
                editor.clear().commit();
                editor.putString(LOGIN_NAME, username);
                editor.apply();
                loginServer(username, password);

            }

        } else {
            isLogin = false;
            cancelDialog();
            Toast.makeText(this, "账号或者密码不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 向服务器提交数据
     *
     * @param username
     * @param password
     */
    private void loginServer(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", username);
            jsonObject.put("pwd", password);

            HttpUtil.sendPostRequestWithHttp(LOGIN_URL, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isLogin = false;
                            cancelDialog();
                            Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isLogin = false;
                            cancelDialog();
                        }
                    });
                    String content = response.body().string();
                    if (Util.isGoodJson(content)) {
                        UserToken userToken = Util.handleLoginInfo(content);
                        if (userToken != null && userToken.message == null) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            UnitInfo info = userToken.unit;
                            editor.putString("token", userToken.token).commit();
                            editor.putString("power_unit", info.getPower()).commit();
                            editor.putString("water_unit", info.getWater_usage()).commit();
                            editor.putString("steam_unit", info.getSteam_usage()).commit();
                            editor.putString("gas_unit", info.getGas_usage()).commit();
                            editor.putString("energy_unit", info.getEnergy_usage()).commit();
                            editor.putString("carbon_unit", info.getCarbon_emissions()).commit();
                            editor.putString("installed_capacity_unit", info.getInstalled_capacity()).commit();
                            startActivity(intent);
                            finish();
                        } else {
                            if (userToken.connect_info.user == null) {
                                person = "";
                            } else {
                                person = userToken.connect_info.user.trim();
                            }
                            if (userToken.connect_info.phone == null) {
                                tel = "";
                            } else {
                                tel = userToken.connect_info.phone.trim();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!person.equals("") && !tel.equals("")) {
                                        Toast.makeText(LoginActivity.this,
                                                "账号或者密码无效，\n请联系负责人：" + person + "，\n电话：" + tel,
                                                Toast.LENGTH_SHORT).show();
                                    } else if (!person.equals("")) {
                                        Toast.makeText(LoginActivity.this,
                                                "账号或者密码无效，\n请联系负责人：" + person,
                                                Toast.LENGTH_SHORT).show();
                                    } else if (!tel.equals("")) {
                                        Toast.makeText(LoginActivity.this,
                                                "账号或者密码无效，\n" + "请联系电话：" + tel,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this,
                                                "账号或者密码无效",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        dialog.setVisibility(View.VISIBLE);
        loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_color2));
        loginButtonDemo.setBackground(getResources().getDrawable(R.drawable.login_button_color2));
        loginButton.setTextColor(getResources().getColor(R.color.white));
        count = 20;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (count > 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogText.setText("请稍等(" + count + ")");
                        }
                    });
                    count--;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isLogin = false;
                            cancelDialog();
                        }
                    });
                }
            }
        }, 1000, 1000);
    }

    private void cancelDialog() {
        dialog.setVisibility(View.GONE);
        loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_color));
        loginButtonDemo.setBackground(getResources().getDrawable(R.drawable.demo_button_color));
        loginButton.setTextColor(getResources().getColor(R.color.tool_bar_color1));
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
