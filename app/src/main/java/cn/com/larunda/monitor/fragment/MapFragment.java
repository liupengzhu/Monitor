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
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
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
import cn.com.larunda.monitor.adapter.MapCompanyAdapter;
import cn.com.larunda.monitor.bean.MapCompanyBean;
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
    private final String MAP_URL = MyApplication.URL + "side/heat_map" + MyApplication.TOKEN;
    private final int color = 0xAA00FF00;
    private SharedPreferences preferences;
    public static String token;

    private Button leftButton;
    public LocationClient mLocationClient = null;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;
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
    private List<MapInfo.DataBeanX> dadas = new ArrayList<>();
    private InfoWindow mInfoWindow;

    private TextView textView;
    private View markerView;
    private RecyclerView markerRecycler;
    private MapCompanyAdapter markerAdapter;
    private List<MapCompanyBean> mapCompanyBeanList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private View view;
    private LatLng pNW;
    private LatLng pNE;
    private LatLng pSE;
    private LatLng pSW;
    private LatLng pNW2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //保存view布局
        if (container.getTag(R.id.tag_second) == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
            mLocationClient = new LocationClient(getContext().getApplicationContext());
            //注册监听函数
            initView(view);
            initEvent();
            container.setTag(R.id.tag_second, view);
        } else {
            view = (View) container.getTag(R.id.tag_second);
        }
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
        markerView = LayoutInflater.from(getContext()).inflate(R.layout.map_layout, null);
        markerView.setMinimumWidth(1000);
        textView = markerView.findViewById(R.id.map_marker_text);
        markerRecycler = markerView.findViewById(R.id.map_marker_recycler);
        markerAdapter = new MapCompanyAdapter(getContext(), mapCompanyBeanList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        markerRecycler.setAdapter(markerAdapter);
        markerRecycler.setLayoutManager(linearLayoutManager);

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
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setOverlookingGesturesEnabled(false);

        radioGroup = view.findViewById(R.id.map_radio_group);
        radioButton1 = view.findViewById(R.id.map_radio1);
        radioButton2 = view.findViewById(R.id.map_radio2);
        radioButton3 = view.findViewById(R.id.map_radio3);
        radioButton4 = view.findViewById(R.id.map_radio4);
        radioButton5 = view.findViewById(R.id.map_radio5);
        radioGroup.check(radioButton1.getId());

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
        pNW = new LatLng(59.0, 73.0);
        pNE = new LatLng(59.0, 136.0);
        pSE = new LatLng(3.0, 136.0);
        pSW = new LatLng(3.0, 73.0);
        pNW2 = new LatLng(59.0, 73.0);
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

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                int point = Integer.parseInt(marker.getTitle());
                LatLng ll = marker.getPosition();
                showPopWindow(point, ll);
                return false;
            }
        });

        adapter.setMapOnClickListener(new MapAdapter.MapOnClickListener() {
            @Override
            public void onClick(View view, int position, LatLng latLng) {
                showPopWindow(position, latLng);
            }
        });


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
                List<LatLng> points = new ArrayList<>();
                points.addAll(polyline);
                points.add(pNW);
                points.add(pNE);
                points.add(pSE);
                points.add(pSW);
                points.add(pNW2);
                OverlayOptions ooPolygon = new PolygonOptions().points(points)
                        .fillColor(Color.argb(78, 0, 0, 0));
                mBaiduMap.addOverlay(ooPolygon);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));

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
        dadas = info.getData();
        if (info.getData() != null) {
            for (int i = 0; i < info.getData().size(); i++) {
                LatLng latLng = new LatLng(Double.valueOf(info.getData().get(i).getLat()),
                        Double.valueOf(info.getData().get(i).getLng()));
                if (i < 10) {
                    PointBean pointBean = new PointBean();
                    pointBean.setName(info.getData().get(i).getName() + "");
                    pointBean.setRank(info.getData().get(i).getTop_ten() + "");
                    pointBean.setLatLng(latLng);
                    pointBeanList.add(pointBean);
                }
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
                        .icon(list.get(i))
                        .title(i + "");
            } else {
                option = new MarkerOptions()
                        .position(latLngList.get(i))
                        .icon(bitmap1)
                        .title(i + "");
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

    private void getMapCompanyBeanList(int point) {
        mapCompanyBeanList.clear();
        MapCompanyBean bean1 = new MapCompanyBean();
        bean1.setData(Util.formatNum(dadas.get(point).getData().getPower()) + "tce");
        bean1.setOriginal(Util.formatNum(dadas.get(point).getOriginal_data().getPower()) + "k"
                + preferences.getString("power_unit", null));
        bean1.setType("电");
        MapCompanyBean bean2 = new MapCompanyBean();
        bean2.setData(Util.formatNum(dadas.get(point).getData().getWater()) + "tce");
        bean2.setOriginal(Util.formatNum(dadas.get(point).getOriginal_data().getWater()) + ""
                + preferences.getString("water_unit", null));
        bean2.setType("水");
        MapCompanyBean bean3 = new MapCompanyBean();
        bean3.setData(Util.formatNum(dadas.get(point).getData().getSteam()) + "tce");
        bean3.setOriginal(Util.formatNum(dadas.get(point).getOriginal_data().getSteam()) + ""
                + preferences.getString("steam_unit", null));
        bean3.setType("蒸汽");
        MapCompanyBean bean4 = new MapCompanyBean();
        bean4.setData(Util.formatNum(dadas.get(point).getData().getGas()) + "tce");
        bean4.setOriginal(Util.formatNum(dadas.get(point).getOriginal_data().getGas()) + ""
                + preferences.getString("gas_unit", null));
        bean4.setType("天然气");
        MapCompanyBean bean5 = new MapCompanyBean();
        bean5.setData(Util.formatNum(dadas.get(point).getData().getTotal()) + "tce");
        bean5.setType("总量");
        mapCompanyBeanList.add(bean1);
        mapCompanyBeanList.add(bean2);
        mapCompanyBeanList.add(bean3);
        mapCompanyBeanList.add(bean4);
        mapCompanyBeanList.add(bean5);

    }

    /**
     * 显示弹窗
     *
     * @param position
     * @param ll
     */
    private void showPopWindow(int position, LatLng ll) {
        if (position < dadas.size()) {
            textView.setText(dadas.get(position).getName() + "");
            getMapCompanyBeanList(position);
            markerAdapter.notifyDataSetChanged();
        }
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(ll)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
        InfoWindow.OnInfoWindowClickListener listener = null;
        listener = new InfoWindow.OnInfoWindowClickListener() {
            public void onInfoWindowClick() {
                mBaiduMap.hideInfoWindow();
            }
        };

        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(markerView), ll, -47, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
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

}
