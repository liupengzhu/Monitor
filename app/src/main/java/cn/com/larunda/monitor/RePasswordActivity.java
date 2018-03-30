package cn.com.larunda.monitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.com.larunda.monitor.gson.MessageInfo;
import cn.com.larunda.monitor.util.ActivityCollector;
import cn.com.larunda.monitor.util.BaseActivity;
import cn.com.larunda.monitor.util.HttpUtil;
import cn.com.larunda.monitor.util.MyApplication;
import cn.com.larunda.monitor.util.Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RePasswordActivity extends BaseActivity implements View.OnClickListener {
    private static final String PWD_URL = MyApplication.URL + "change_pwd" + MyApplication.TOKEN;
    private SharedPreferences preferences;
    private String token;

    private Button backButton;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_password);
        initView();
        initEvent();
    }

    /**
     * 初始化
     */
    private void initView() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);

        backButton = findViewById(R.id.re_password_back);
        editText1 = findViewById(R.id.re_password_edit1);
        editText2 = findViewById(R.id.re_password_edit2);
        editText3 = findViewById(R.id.re_password_edit3);
        button = findViewById(R.id.re_password_button);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        backButton.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_password_back:
                finish();
                break;
            case R.id.re_password_button:
                String password1 = editText1.getText().toString().trim();
                String password2 = editText2.getText().toString().trim();
                String password3 = editText3.getText().toString().trim();
                if (!isEmpty(password1, password2, password3)) {
                    sendPostRequest(password1, password2, password3);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送网络请求
     *
     * @param password1
     * @param password2
     * @param password3
     */
    private void sendPostRequest(String password1, String password2, String password3) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password1);
            jsonObject.put("new_password", password2);
            jsonObject.put("rep_password", password3);
            HttpUtil.sendPostRequestWithHttp(PWD_URL + token, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RePasswordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String content = response.body().string();
                    if (Util.isGoodJson(content)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                parseContent(content);
                            }
                        });
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseContent(String content) {
        if (content.equals("true")) {
            finish();
        } else if (content.equals("false")) {

        } else {
            MessageInfo info = Util.handleMessageInfo(content);
            if (info != null && info.getError() == null) {
                Toast.makeText(this, info.getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("token_timeout", "登录超时");
                preferences.edit().putString("token", null).commit();
                startActivity(intent);
                ActivityCollector.finishAllActivity();
            }
        }
    }

    /**
     * 判断密码是否为空
     *
     * @param password1
     * @param password2
     * @param password3
     * @return
     */
    private boolean isEmpty(String password1, String password2, String password3) {
        if (password1.equals("")) {
            Toast.makeText(this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return true;
        } else if (password2.equals("")) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return true;
        } else if (password3.equals("")) {
            Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
            return true;
        } else if (password1.equals(password2)) {
            Toast.makeText(this, "新密码与旧密码相同", Toast.LENGTH_SHORT).show();
            return true;
        } else if (!password2.equals(password3)) {
            Toast.makeText(this, "新密码与确认密码不一致", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
