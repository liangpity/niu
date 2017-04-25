package com.weather.lb.com.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lb.test.com.lb.test.untils.HttpUntil;
import com.lb.test.com.lb.test.untils.Utility;
import com.weather.lb.com.entity.Forecast;
import com.weather.lb.com.entity.Weather;
import com.xiongmai.lb.test.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-04-24.
 */

public class WeatherActivity extends Activity {

    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView cityName;  //城市名
    private TextView updateTime; //更新时间
    private TextView currentWeather; //当前天气
    private TextView degree; //当前温度
    private TextView bodyTemp; //体感温度
    private TextView wind; //风向
    private TextView aqi; //污染
    private TextView comfort; // 舒适度
    private TextView carWash; //洗车指数
    private TextView sport;//运动指数
    private LinearLayout forecastLayout;//未来天气预报
    private ImageView background;
    private String weatherCity = null ;
    private Button navBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21 ){
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        cityName = (TextView) findViewById(R.id.wearther_city);
        updateTime = (TextView) findViewById(R.id.update_time);
        currentWeather = (TextView) findViewById(R.id.current_weather);
        degree = (TextView) findViewById(R.id.degree_weather);
        bodyTemp = (TextView) findViewById(R.id.body_temp);
        wind = (TextView) findViewById(R.id.wind);
        aqi = (TextView) findViewById(R.id.aqi);
        comfort = (TextView) findViewById(R.id.comfort);
        carWash = (TextView) findViewById(R.id.car_wash);
        sport = (TextView) findViewById(R.id.sport);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_futrure);
        background = (ImageView) findViewById(R.id.back_ground);
        navBtn = (Button) findViewById(R.id.nav_button);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void setDrawerLayout(String cityName){
        drawerLayout.closeDrawers();
        swipeRefreshLayout.setRefreshing(true);
        weatherCity = cityName;
        requestWeather(cityName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handlerWeatherRespose(weatherString);
            weatherCity = weather.basic.cityName;
            showWeatherInfo(weather);
        } else {
            String cityName = getIntent().getStringExtra("WEATHER_ID");
            weatherCity = cityName;
            requestWeather(cityName);
        }
        setBackground();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestWeather(weatherCity);
            }
        });

    }


    private void setBackground(){
        String imgUrl = "http://guolin.tech/api/bing_pic";
        HttpUntil.sendOkhttpRequest(imgUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String url =response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(url).into(background);
                    }
                });
            }
        });

    }

    private void requestWeather(final String cityName){
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+ cityName +"&key=bc0418b57b2d4918819d3974ac1285d9";

        HttpUntil.sendOkhttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(WeatherActivity.this , "加载天气失败" , Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String respose = response.body().string();
                final Weather weather = Utility.handlerWeatherRespose(respose);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather" , respose);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this , "加载天气失败" , Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });


    }

    private void showWeatherInfo(Weather weather) {
        try {
            cityName.setText(weather.basic.cityName);
            updateTime.setText(weather.basic.update.upadteTime);
            currentWeather.setText(weather.now.cond.info);
            degree.setText("当前温度："+weather.now.temperature + "℃");
            bodyTemp.setText("体感温度："+weather.now.bodyTemp + "℃");
            wind.setText(weather.now.wind.windDir +  weather.now.wind.windSc + "级");
            aqi.setText("污染指数：  " + weather.aqi.aqiCity.aqi + " \nPM2.5 :     " + weather.aqi.aqiCity.pm25
                    + " \n类  别：    " + weather.aqi.aqiCity.qlty);
            comfort.setText("舒适度：" + weather.suggestion.comfort.info);
            carWash.setText("洗车指数：" + weather.suggestion.carWash.info);
            sport.setText("运动建议：" + weather.suggestion.sport.info);
            forecastLayout.removeAllViews();
            for (Forecast forecast : weather.forecasts) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecst_item, null);
                TextView forecastDate = (TextView) view.findViewById(R.id.date_text);//预报日期
                TextView forecastInfo = (TextView) view.findViewById(R.id.info); //天气信息
                TextView forecastMax = (TextView) view.findViewById(R.id.max_temp);//最高温度
                TextView forecastMin = (TextView) view.findViewById(R.id.min_temp);//最低温度
                forecastDate.setText(forecast.date);
                forecastInfo.setText(forecast.more.info);
                forecastMax.setText(forecast.temperature.max + "℃");
                forecastMin.setText(forecast.temperature.min + "℃");
                forecastLayout.addView(view);
            }
        }catch ( Exception e){
            e.printStackTrace();
        }


    }
}
