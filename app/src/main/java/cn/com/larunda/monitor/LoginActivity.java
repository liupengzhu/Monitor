package cn.com.larunda.monitor;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private final String LOGIN_URL = MyApplication.URL + "login";
    private final String UNIT_URL = MyApplication.URL + "config/unit";
    private boolean isLogin = false;
    private String person = "李俊";
    private String tel = "18762870876";

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

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getString("unit", null) == null) {
            sendRequest();
        }
    }

    /**
     * 加载单位接口
     */
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
    }

    /**
     * 初始化view
     */
    private void initView() {
        loginName = findViewById(R.id.login_name);
        loginPassword = findViewById(R.id.login_password);
        checkBox = findViewById(R.id.check_button);
        loginButton = findViewById(R.id.login_button);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        loginButton.setOnClickListener(this);
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
                    isLogin = true;
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
                            Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    isLogin = false;
                    String content = response.body().string();
                    if (Util.isGoodJson(content)) {
                        UserToken userToken = Util.handleLoginInfo(content);
                        if (userToken != null && userToken.message == null) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            editor.putString("token", userToken.token);
                            editor.apply();
                            startActivity(intent);
                            finish();
                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "账号或者密码无效,请联系负责人:"+person+",电话:"+tel, Toast.LENGTH_SHORT).show();
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
}
