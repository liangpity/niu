package com.lb.test;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017-04-20.
 */

public class County extends DataSupport {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWearthId() {
        return wearthId;
    }

    public void setWearthId(String wearthId) {
        this.wearthId = wearthId;
    }

    private int id;
    private String countyName;
    private int cityId;
    private String wearthId;

}
