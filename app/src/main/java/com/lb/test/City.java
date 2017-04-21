package com.lb.test;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017-04-20.
 */

public class City extends DataSupport {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public int getCityCide() {
        return cityCode;
    }

    public void setCityCode(int cityCide) {
        this.cityCode = cityCide;
    }

    public int getProviceId() {
        return proviceId;
    }

    public void setProviceId(int proviceId) {
        this.proviceId = proviceId;
    }

    private int id;
    private String CityName;
    private int cityCode;
    private int proviceId;



}
