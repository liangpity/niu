package com.weather.lb.com.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-04-24.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String cityID;

    @SerializedName("update")
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String upadteTime;
    }
}
