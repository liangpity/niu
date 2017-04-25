package com.weather.lb.com.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lb.test.City;
import com.lb.test.County;
import com.lb.test.Provice;
import com.lb.test.WeartherAdapter;
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

import static android.R.attr.type;

/**
 * Created by Administrator on 2017-04-25.
 */

public class CitySeleteFragment  extends Fragment implements AdapterView.OnItemClickListener{

    private Activity mActivity;
    private ListView listView;
    private WeartherAdapter adapter;
    private List<Provice> proviceList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private List<County> countyList = new ArrayList<>();
    private List<String> dataList = new ArrayList<>();
    private int currentLevel = 0;
    private Provice selectProvince;
    private City selectCity;

    @Nullable
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (WeatherActivity)context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_select_city , container , false);
        listView = (ListView) view.findViewById(R.id.select_city);
        listView.setOnItemClickListener(this);
        adapter = new WeartherAdapter(mActivity , dataList);
        queryProvinces();
        return view;
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
            String cityName = countyList.get(position).getCountyName();
            if(mActivity instanceof WeatherActivity){
                ((WeatherActivity)mActivity).setDrawerLayout(cityName);
            }
        }
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

    private void queryWeartherData(String adress , final String type){
        HttpUntil.sendOkhttpRequest(adress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resposeText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handlerProvinceRespose(resposeText);
                } else if("city".equals(type)){
                    result =Utility.handlerCityRespose(resposeText , selectProvince.getProviceCode());
                } else if ("county".equals(type)){
                    result = Utility.handlerCountyRespose(resposeText , selectCity.getCityCode());
                }

                if(result){
                    mHandler.sendEmptyMessage(111);
                }
            }
        });
    }

    Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            if("province".equals(type)){
                queryProvinces();
            } else if("city".equals(type)){
                queryCity();
            } else if ("county".equals(type)){
                queryCounty();
            }
        }
    };
}
