package com.lb.test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lb.test.com.lb.test.untils.HttpUntil;
import com.lb.test.com.lb.test.untils.Utility;
import com.xiongmai.lb.test.R;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  ,AdapterView.OnItemClickListener{

    private ListView listView;
    private Button mQuery;
    private Button mBack;
    private WeartherAdapter adapter;
    private List<Provice> proviceList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private List<County> countyList = new ArrayList<>();
    private List<String> dataList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int currentLevel = 0;
    private Provice selectProvince;
    private City selectCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.wearther);
        listView.setOnItemClickListener(this);
        mQuery = (Button) findViewById(R.id.query);
        mQuery.setOnClickListener(this);
        mBack = (Button) findViewById(R.id.back);
        mBack.setOnClickListener(this);
        adapter = new WeartherAdapter(this , dataList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mQuery.getId()){
            if(currentLevel == 0){
                queryProvinces();
            }
        } else if(v.getId() == mBack.getId()){
            if(currentLevel == 3){
                queryCity();
            } else if(currentLevel == 2){
                queryProvinces();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(currentLevel == 3){
            queryCity();
        } else if(currentLevel == 2){
            queryProvinces();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if( currentLevel == 1){
            selectProvince = proviceList.get(position);
            queryCity();
        } else if(currentLevel == 2){
            selectCity = cityList.get(position);
            queryCounty();
        } else if(currentLevel == 3){
            Toast.makeText(this , "选择了 ： " +countyList.get(position).getCountyName() , Toast.LENGTH_SHORT).show();
        }
    }


    private void queryWeartherData(String adress , final String type){
        showWaitDialog();
        HttpUntil.sendOkhttpRequest(adress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this , "失败" ,Toast.LENGTH_SHORT).show();
                hideWaitDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resposeText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result =Utility.handlerProvinceRespose(resposeText);
                } else if("city".equals(type)){
                    result =Utility.handlerCityRespose(resposeText , selectProvince.getProviceCode());
                } else if ("county".equals(type)){
                    result = Utility.handlerCountyRespose(resposeText , selectCity.getCityCode());
                }

                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideWaitDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            } else if("city".equals(type)){
                               queryCity();
                            } else if ("county".equals(type)){
                                queryCounty();
                            }
                        }
                    });
                }
            }
        });
    }

    private void queryProvinces(){
        proviceList = DataSupport.findAll(Provice.class);
        if(proviceList != null && proviceList.size() > 0){
            dataList.clear();
            for (Provice provice : proviceList){
                dataList.add(provice.getProviceName());
            }
            adapter.updateLisy(dataList);
            listView.setSelection(0);
            currentLevel = 1;
        } else {
            String address = "http://guolin.tech/api/china";
            queryWeartherData(address , "province");
        }
    }

    private void queryCity(){
        cityList = DataSupport.where("proviceId = ?" , String.valueOf(selectProvince.getId())).find(City.class);
        if(cityList != null && cityList.size() > 0){
            dataList.clear();
            for (City city : cityList ){
                dataList.add(city.getCityName());
            }
            adapter.updateLisy(dataList);
            listView.setSelection(0);
            currentLevel = 2;
        } else {
            String address = "http://guolin.tech/api/china/" + selectProvince.getId();
            queryWeartherData(address , "city");
        }
    }

    private void queryCounty(){
        countyList = DataSupport.where("cityId = ?" , String.valueOf(selectCity.getCityCode())).find(County.class);
        if(countyList != null && countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.updateLisy(dataList);
            listView.setSelection(0);
            currentLevel = 3;
        } else {
            String address = "http://guolin.tech/api/china/" + selectProvince.getProviceCode() + "/" + selectCity.getCityCode();
            queryWeartherData(address , "county");
        }
    }

    private void showWaitDialog(){
        if (progressDialog == null ){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void hideWaitDialog(){
        if(progressDialog != null ){
            progressDialog.dismiss();
        }
    }
}
