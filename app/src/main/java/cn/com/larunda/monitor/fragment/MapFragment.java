package cn.com.larunda.monitor.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import cn.com.larunda.monitor.MainActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.util.MyApplication;

/**
 * Created by sddt on 18-3-13.
 */

public class MapFragment extends Fragment implements View.OnClickListener {
    private static final String MAP_URL = MyApplication.URL + "side/heat_map" + MyApplication.TOKEN;
    private Button leftButton;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ImageView img;
    private LinearLayout mapUpLayout;
    private boolean isUp = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initView(view);
        initEvent();
        sendRequest();
        return view;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        leftButton = view.findViewById(R.id.map_left_button);

        img = view.findViewById(R.id.map_up_view);
        mapUpLayout = view.findViewById(R.id.map_up_layout);

        mMapView = view.findViewById(R.id.map_mapView);
        mBaiduMap = mMapView.getMap();
        Point point1 = new Point(0, 0);
        mMapView.setScaleControlPosition(point1);
        mMapView.showZoomControls(false);
    }

    /*
    初始化点击事件
     */
    private void initEvent() {
        leftButton.setOnClickListener(this);
        mapUpLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_left_button:
                MainActivity.drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.map_up_layout:
                if (!isUp) {
                    isUp = true;
                    changeMarginUp();
                    img.setRotation(180);
                } else {
                    isUp = false;
                    changeMarginDown();
                    img.setRotation(0);
                }
                break;
            default:
                break;
        }
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    private void changeMarginUp() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapUpLayout.getLayoutParams();
        params.bottomMargin = 0;
        mapUpLayout.setLayoutParams(params);
    }

    private void changeMarginDown() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapUpLayout.getLayoutParams();
        params.bottomMargin = -(int) (mapUpLayout.getHeight() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                70, getContext().getResources().getDisplayMetrics()));

        mapUpLayout.setLayoutParams(params);
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
    }

    /**
     * 获取按钮组状态
     */
    private void getType() {

    }
}
