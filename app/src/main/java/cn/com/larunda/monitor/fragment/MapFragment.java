package cn.com.larunda.monitor.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.WeightedLatLng;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.larunda.monitor.LoginActivity;
import cn.com.larunda.monitor.MainActivity;
import cn.com.larunda.monitor.R;
import cn.com.larunda.monitor.adapter.MapAdapter;
import cn.com.larunda.monitor.bean.PointBean;
import cn.com.larunda.monitor.gson.MapInfo;
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

public class MapFragment extends Fragment implements View.OnClickListener, OnGetDistricSearchResultListener {
    private static final String MAP_URL = MyApplication.URL + "side/heat_map" + MyApplication.TOKEN;
    private final int color = 0xAA00FF00;
    private SharedPreferences preferences;
    public static String token;

    private Button leftButton;
    public LocationClient mLocationClient = null;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private DistrictSearch mDistrictSearch;


    private ImageView img;
    private LinearLayout mapUpLayout;
    private boolean isUp = false;

    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private String type;

    private RecyclerView recyclerView;
    private MapAdapter adapter;
    private GridLayoutManager manager;
    private List<PointBean> pointBeanList = new ArrayList<>();
    private List<LatLng> latLngList = new ArrayList<>();
    private List<WeightedLatLng> weightedLatLngList = new ArrayList<>();
    private BitmapDescriptor bitmap1;
    private BitmapDescriptor bitmap2;
    private BitmapDescriptor bitmap3;
    private BitmapDescriptor bitmap4;
    private BitmapDescriptor bitmap5;
    private BitmapDescriptor bitmap6;
    private BitmapDescriptor bitmap7;
    private BitmapDescriptor bitmap8;
    private BitmapDescriptor bitmap9;
    private BitmapDescriptor bitmap10;
    private BitmapDescriptor bitmap11;
    private List<BitmapDescriptor> list = new ArrayList<>();
    private HeatMap heatmap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //注册监听函数
        initView(view);
        initEvent();
        //drawBackground();
        sendRequest();
        return view;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        isUp = false;
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = preferences.getString("token", null);

        leftButton = view.findViewById(R.id.map_left_button);

        img = view.findViewById(R.id.map_up_view);
        mapUpLayout = view.findViewById(R.id.map_up_layout);

        mDistrictSearch = DistrictSearch.newInstance();
        mMapView = view.findViewById(R.id.map_mapView);
        mBaiduMap = mMapView.getMap();
        Point point1 = new Point(0, 0);
        mMapView.setScaleControlPosition(point1);
        mMapView.showZoomControls(false);

        radioGroup = view.findViewById(R.id.map_radio_group);
        radioButton1 = view.findViewById(R.id.map_radio1);
        radioButton2 = view.findViewById(R.id.map_radio2);
        radioButton3 = view.findViewById(R.id.map_radio3);
        radioButton4 = view.findViewById(R.id.map_radio4);
        radioButton5 = view.findViewById(R.id.map_radio5);

        recyclerView = view.findViewById(R.id.map_recycler);
        adapter = new MapAdapter(getContext(), pointBeanList);
        manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        //构建Marker图标
        bitmap1 = BitmapDescriptorFactory.fromResource(R.mipmap.point1);
        bitmap2 = BitmapDescriptorFactory.fromResource(R.mipmap.point2);
        bitmap3 = BitmapDescriptorFactory.fromResource(R.mipmap.point3);
        bitmap4 = BitmapDescriptorFactory.fromResource(R.mipmap.point4);
        bitmap5 = BitmapDescriptorFactory.fromResource(R.mipmap.point5);
        bitmap6 = BitmapDescriptorFactory.fromResource(R.mipmap.point6);
        bitmap7 = BitmapDescriptorFactory.fromResource(R.mipmap.point7);
        bitmap8 = BitmapDescriptorFactory.fromResource(R.mipmap.point8);
        bitmap9 = BitmapDescriptorFactory.fromResource(R.mipmap.point9);
        bitmap10 = BitmapDescriptorFactory.fromResource(R.mipmap.point10);
        bitmap11 = BitmapDescriptorFactory.fromResource(R.mipmap.point11);
        list.add(bitmap1);
        list.add(bitmap2);
        list.add(bitmap3);
        list.add(bitmap4);
        list.add(bitmap5);
        list.add(bitmap6);
        list.add(bitmap7);
        list.add(bitmap8);
        list.add(bitmap9);
        list.add(bitmap10);
        list.add(bitmap11);
    }

    /*
    初始化点击事件
     */
    private void initEvent() {
        leftButton.setOnClickListener(this);
        mapUpLayout.setOnClickListener(this);
        mDistrictSearch.setOnDistrictSearchListener(this);

        radioButton1.setOnClickListener(this);
        radioButton2.setOnClickListener(this);
        radioButton3.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton5.setOnClickListener(this);
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
            case R.id.map_radio1:
                sendRequest();
                break;
            case R.id.map_radio2:
                sendRequest();
                break;
            case R.id.map_radio3:
                sendRequest();
                break;
            case R.id.map_radio4:
                sendRequest();
                break;
            case R.id.map_radio5:
                sendRequest();
                break;
            default:
                break;
        }
    }

    /**
     * 行政区域检索回调
     *
     * @param districtResult
     */
    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        if (districtResult == null) {
            return;
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<List<LatLng>> polyLines = districtResult.getPolylines();
            if (polyLines == null) {
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {
                OverlayOptions ooPolygon = new PolygonOptions().points(polyline)
                        .fillColor(Color.argb(30, 0, 0, 0));
                mBaiduMap.addOverlay(ooPolygon);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));

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
        for (BitmapDescriptor descriptor : list) {
            if (descriptor != null) {
                descriptor.recycle();
                descriptor = null;
            }
        }
        list.clear();


    }

    private void changeMarginUp() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapUpLayout.getLayoutParams();
        params.bottomMargin = 0;
        mapUpLayout.setLayoutParams(params);
    }

    private void changeMarginDown() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapUpLayout.getLayoutParams();
        params.bottomMargin = -(int) (mapUpLayout.getHeight() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                55, getContext().getResources().getDisplayMetrics()));

        mapUpLayout.setLayoutParams(params);
    }

    /**
     * 发送网络请求
     */
    private void sendRequest() {
        getType();
        HttpUtil.sendGetRequestWithHttp(MAP_URL + token + "&type=" + type, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String content = response.body().string();
                if (Util.isGoodJson(content)) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MapInfo info = Util.handleMapInfo(content);
                                if (info != null && info.getError() == null) {
                                    parseInfo(info);
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
     * 解析服务器返回信息
     *
     * @param info
     */
    private void parseInfo(MapInfo info) {
        mBaiduMap.clear();
        mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName("苏州").districtName("昆山"));
        pointBeanList.clear();
        latLngList.clear();
        weightedLatLngList.clear();
        if (info.getData() != null) {
            for (int i = 0; i < info.getData().size(); i++) {
                if (i < 10) {
                    PointBean pointBean = new PointBean();
                    pointBean.setName(info.getData().get(i).getName() + "");
                    pointBean.setRank(info.getData().get(i).getTop_ten() + "");
                    pointBeanList.add(pointBean);
                }
                LatLng latLng = new LatLng(Double.valueOf(info.getData().get(i).getLat()),
                        Double.valueOf(info.getData().get(i).getLng()));
                latLngList.add(latLng);
                WeightedLatLng weightedLatLng = new WeightedLatLng(latLng, info.getData().get(i).getCount());
                weightedLatLngList.add(weightedLatLng);
            }
        }
        adapter.notifyDataSetChanged();
        for (int i = 0; i < latLngList.size(); i++) {
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option;
            if (i < 11) {
                option = new MarkerOptions()
                        .position(latLngList.get(i))
                        .icon(list.get(i));
            } else {
                option = new MarkerOptions()
                        .position(latLngList.get(i))
                        .icon(bitmap1);
            }
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
        heatmap = new HeatMap.Builder().weightedData(weightedLatLngList).radius(30).build();
        mBaiduMap.addHeatMap(heatmap);
    }

    /**
     * 获取按钮组状态
     */
    private void getType() {
        if (radioGroup.getCheckedRadioButtonId() == R.id.map_radio5) {
            type = "gas";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.map_radio2) {
            type = "power";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.map_radio3) {
            type = "water";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.map_radio4) {
            type = "steam";
        } else {
            type = "total";
        }
    }

    private void drawBackground() {
        mBaiduMap.clear();
        // 添加多边形
        LatLng pt1 = new LatLng(59.0, 73.0);
        LatLng pt2 = new LatLng(59.0, 136.0);
        LatLng pt3 = new LatLng(3.0, 136.0);
        LatLng pt4 = new LatLng(3.0, 73.0);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        OverlayOptions ooPolygon = new PolygonOptions().points(pts)
                .fillColor(Color.argb(20, 0, 0, 0));
        mBaiduMap.addOverlay(ooPolygon);
    }
}
