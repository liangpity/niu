package com.lb.test.com.lb.test.untils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017-04-20.
 */

public class HttpUntil {

    public static void sebdOkhttpRequest(String address , okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();

        client.newCall(request).enqueue(callback);
    }
}