package cn.com.larunda.monitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CompanyListActivity extends AppCompatActivity implements View.OnClickListener {

    private int id;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        id = getIntent().getIntExtra("id", 0);
        Log.d("main", id + "");
        initView();
        initEvent();
    }

    /**
     * 初始化view
     */
    private void initView() {
        backButton = findViewById(R.id.company_list_back);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.company_list_back:
                finish();
                break;
        }
    }
}
