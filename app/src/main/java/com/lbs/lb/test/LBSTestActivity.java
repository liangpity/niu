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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.aactivity_lbs);
        posInfo = (TextView) findViewById(R.id.position_info);
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(LBSTestActivity.this , android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(LBSTestActivity.this , Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(LBSTestActivity.this , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(! permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LBSTestActivity.this , permissions , 1);
        } else {
            requsetLocation();
        }

    }
    private void requsetLocation(){
        locationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for (int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this , "开启权限才能开启位置" , Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requsetLocation();
                } else {
                   Toast.makeText(this , "发生错误" ,Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public  class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuilder currentPos =  new StringBuilder();
            currentPos.append("纬度：").append(location.getLatitude()).append("\n");
            currentPos.append("经度：").append(location.getLongitude()).append("\n");
            currentPos.append("定位方式:");
            if(location.getLocType() == BDLocation.TypeGpsLocation){
                currentPos.append("GPS");
            } else if(location.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPos.append("网络");
            }
            posInfo.setText(currentPos);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Toast.makeText(LBSTestActivity.this , s , Toast.LENGTH_SHORT).show();
        }
    }
}
