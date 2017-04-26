package com.weather.lb.com.entity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-04-24.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("fl")
    public String bodyTemp;

    public class Cond {
        @SerializedName("txt")
        public String info;
    }

    public Cond cond ;

    public class Wind{
        @SerializedName("sc")
        public String windSc;

        @SerializedName("dir")
        public String windDir;
    }

    public  Wind wind;

}
