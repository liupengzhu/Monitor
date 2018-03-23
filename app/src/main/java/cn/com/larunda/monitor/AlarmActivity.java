package cn.com.larunda.monitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.com.larunda.monitor.util.BaseActivity;

public class AlarmActivity extends BaseActivity implements View.OnClickListener {

    public static int id;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        id = getIntent().getIntExtra("id", 0);
        initView();
        initEvent();
    }

    private void initView() {
        backButton = findViewById(R.id.alarm_back_button);
    }

    private void initEvent() {
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_back_button:
                finish();
                break;
        }
    }
}
