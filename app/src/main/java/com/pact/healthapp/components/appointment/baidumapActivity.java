package com.pact.healthapp.components.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.view.CommonToast;

/**
 * 进行地理编码搜索（用地址检索坐标）、反地理编码搜索（用坐标检索地址）
 * 结合定位SDK实现定位
 */
public class baidumapActivity extends BaseFragmentActivity implements
        OnGetGeoCoderResultListener {

    private TextView org_center_name;
    private TextView org_center_address;
    private TextView org_center_time;

    String orgName;
    String address;
    String openTime;

    String longitude;
    String latitude;

    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BaiduMap mBaiduMap = null;
    MapView mMapView = null;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    boolean isFirstLoc = true; // 是否首次定位

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
//        setContentView(R.layout.baidumap_geocoder_layout);
        setContenierView(1);
        setTitle("百度地图");
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.GONE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.login_fl_continer);
        View view = LayoutInflater.from(context).inflate(R.layout.baidumap_geocoder_layout, null);
        frameLayout.addView(view);
        // 地图初始化
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);


        Intent intent = getIntent();
        orgName = intent.getStringExtra("orgName");
        address = intent.getStringExtra("address");
        openTime = intent.getStringExtra("openTime");

        org_center_name = (TextView) view.findViewById(R.id.org_center_name);
        org_center_address = (TextView) view.findViewById(R.id.org_center_address);
        org_center_time = (TextView) view.findViewById(R.id.org_center_time);

        org_center_name.setText(orgName);
        org_center_address.setText(address);
        org_center_time.setText(openTime);

        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");

        //后台没有配置体检机构的地址经纬度，默认定位到天安门
        if (longitude.equals("") || latitude.equals("")) {
            latitude = "39.9151120000";
            longitude = "116.4039630000";
        }

        //设定中心点坐标
        LatLng cenpt = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        //检索位置
        SearchButtonProcess();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();
    }

    /**
     * 发起搜索
     */
    public void SearchButtonProcess() {
        if (longitude.equals("") || latitude.equals("")) {
            return;
        }
        LatLng ptCenter = new LatLng(Float.valueOf(latitude), Float.valueOf(longitude));
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));

        //Geo搜索
//        mSearch.geocode(new
//                GeoCodeOption().city("北京").address("海淀区上地十街10号"));
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
//        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            CommonToast.makeText(baidumapActivity.this, "抱歉，未能找到结果");
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
//        CommonToast.makeText(baidumapActivity.this, strInfo);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            CommonToast.makeText(baidumapActivity.this, "抱歉，未能找到结果");
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
//        CommonToast.makeText(baidumapActivity.this, result.getAddress());
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}
