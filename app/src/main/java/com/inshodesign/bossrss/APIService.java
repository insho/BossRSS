package com.inshodesign.bossrss;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Service making Retrofit calls to download the contents of an RSS feed
 *
 * @see MainActivity#getRSSFeed(String)
 */
public class APIService {

    public static <T> T createXmlAdapterFor(final Class<T> api) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.google.com/") //it's a dummy url
                .client(okHttpClient) // Use OkHttp3 client
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava adapter
                .addConverterFactory(SimpleXmlConverterFactory.create()) // Simple XML converter

                .build();
        return retrofit.create(api);
    }
}
