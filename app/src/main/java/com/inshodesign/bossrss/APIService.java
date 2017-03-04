package com.inshodesign.bossrss;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by JClassic on 3/4/2017.
 */

public class APIService {

    public static <T> T createXmlAdapterFor(final Class<T> api, final String endpoint) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .client(new OkHttpClient()) // Use OkHttp3 client
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava adapter
                .addConverterFactory(SimpleXmlConverterFactory.create()) // Simple XML converter
                .build();
        return retrofit.create(api);
    }
}
