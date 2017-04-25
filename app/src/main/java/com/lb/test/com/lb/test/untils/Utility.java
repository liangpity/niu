package com.lb.test.com.lb.test.untils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lb.test.City;
import com.lb.test.County;
import com.lb.test.Provice;
import com.weather.lb.com.entity.Weather;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017-04-20.
 */

public class Utility {

    /**
     * 解析省级Json 数据
     */
    public static boolean handlerProvinceRespose(String respose){
        if(!TextUtils.isEmpty(respose)){
            try {
                JSONArray jsonArray =new JSONArray(respose);
                final int jsonArray_length = jsonArray.length();// Moved  jsonArray.length() call out of the loop to local variable jsonArray_length
                for(int i = 0; i < jsonArray_length; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Provice provice = new Provice();
                    provice.setProviceCode(jsonObject.getInt("id"));
                    provice.setProviceName(jsonObject.getString("name"));
                    provice.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 解析市级Json 数据
     */
    public static boolean handlerCityRespose(String respose , int provinceId){
        if(!TextUtils.isEmpty(respose)){
            try {
                JSONArray jsonArray =new JSONArray(respose);
                final int jsonArray_length = jsonArray.length();// Moved  jsonArray.length() call out of the loop to local variable jsonArray_length
                for(int i = 0; i < jsonArray_length; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProviceId(provinceId);
                    city.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 解析省级Json 数据
     */
    public static boolean handlerCountyRespose(String respose , int cityId){
        if(!TextUtils.isEmpty(respose)){
            try {
                JSONArray jsonArray =new JSONArray(respose);
                final int jsonArray_length = jsonArray.length();// Moved  jsonArray.length() call out of the loop to local variable jsonArray_length
                for(int i = 0; i < jsonArray_length; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setCityId(cityId);
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWearthId(jsonObject.getString("weather_id"));
                    county.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 解析天气信息
     */
    public static Weather handlerWeatherRespose(String respose){
        try {
            JSONObject  jsonObject = new JSONObject(respose);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherInfo = jsonArray.getJSONObject(0).toString();
            Weather weather = new Gson().fromJson(weatherInfo , new TypeToken<Weather>(){}.getType());
            if(weather != null)
                return weather;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public static  String getImgUrl(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            String url = jsonObject.getString("url");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
