package com.inshodesign.bossrss;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;


public class RSSFeedClient {

    //TODO -- MAKE DYNAMIC
//    private static String TESTURL = "https://www.espn.com/espnradio/feeds/rss/podcast.xml/_/id/2406595";
    private static RSSFeedClient instance;
//    private NYTimesService nytimesService;



    private RSSFeedClient(String URL) {

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

//        final Retrofit retrofit = new Retrofit.Builder().baseUrl(NYTIMES_BASE_URL)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(client.build())
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        nytimesService = retrofit.create(NYTimesService.class);
    }

    public static RSSFeedClient getInstance(String URL) {
        if (instance == null) {
            instance = new RSSFeedClient(URL);
        }
        return instance;
    }

//    public Observable<NYTimesArticleWrapper> getArticles(@NonNull String requesttype, @NonNull String section, @NonNull String time, @NonNull String apikey) {
//        return nytimesService.getArticles(requesttype, section, time,apikey);
//    }


}

