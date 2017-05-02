package com.lbs.lb.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.xiongmai.lb.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-04-28.
 */

public class LBSTestActivity extends AppCompatActivity {

    //位置信息
    private TextView posInfo;
    public LocationClient locationClient;
    private MapView mapView;
    private boolean isFirstLoc = true;
    private BaiduMap baiduMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.aactivity_lbs);
        posInfo = (TextView) findViewById(R.id.position_info);
        mapView = (MapView) findViewById(R.id.baidu_map);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(LBSTestActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LBSTestActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(LBSTestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(LBSTestActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LBSTestActivity.this, permissions, 1);
        } else {
            requsetLocation();
        }
        baiduMap = mapView.getMap();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    private void requsetLocation() {
        initLocation();
        locationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        locationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "开启权限才能开启位置", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requsetLocation();
                } else {
                    Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location.getLocType() == BDLocation.TypeNetWorkLocation
                    || location.getLocType() == BDLocation.TypeGpsLocation){
                moveToLoc(location);
            }
//            StringBuilder currentPos = new StringBuilder();
//            currentPos.append("纬度：").append(location.getLatitude()).append("\n");
//            currentPos.append("经度：").append(location.getLongitude()).append("\n");
//            currentPos.append("国家:").append(location.getCountry()).append("\n");
//            currentPos.append("省份:").append(location.getProvince()).append("\n");
//            currentPos.append("市:").append(location.getCity()).append("\n");
//            currentPos.append("区:").append(location.getDistrict()).append("\n");
//            currentPos.append("街道:").append(location.getStreet()).append("\n");
//            currentPos.append("定位方式:");
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                currentPos.append("GPS");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                currentPos.append("网络");
//            }
//            posInfo.setText(currentPos);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Toast.makeText(LBSTestActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToLoc(BDLocation location){
        if(isFirstLoc){
            LatLng ll = new LatLng(location.getLatitude() , location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLoc = false;
        }

    }
}
