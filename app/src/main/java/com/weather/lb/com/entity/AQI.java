package com.weather.lb.com.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-04-24.
 */

public class AQI {
    public class AQICity{
        @SerializedName("aqi")
        public String aqi;
        @SerializedName("pm25")
        public String pm25;
        @SerializedName("qlty")
        public String qlty;

    }
    @SerializedName("city")
    public AQICity aqiCity;
}
