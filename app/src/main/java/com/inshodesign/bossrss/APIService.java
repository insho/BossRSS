package com.inshodesign.bossrss;

import com.inshodesign.bossrss.XMLModel.Channel;
//import com.inshodesign.bossrss.XMLModel.CustomConverterFactory;
//import com.inshodesign.bossrss.XMLModel.CustomItemFactory;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by JClassic on 3/4/2017.
 */

public class APIService {

    public static <T> T createXmlAdapterFor(final Class<T> api, final String endpoint) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Strategy strategy = new AnnotationStrategy();

        Serializer serializer = new Persister(strategy);
//        serializer.read(Channel.ChannelConverter,)
//        serializer.read(Channel.ChannelConverter,);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.google.com/")
                .client(okHttpClient) // Use OkHttp3 client
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava adapter
                .addConverterFactory(SimpleXmlConverterFactory.create()) // Simple XML converter
//                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
//                .addConverterFactory(CustomConverterFactory.create())
//                .addConverterFactory(CustomItemFactory.create())

                .build();
        return retrofit.create(api);
    }
}
