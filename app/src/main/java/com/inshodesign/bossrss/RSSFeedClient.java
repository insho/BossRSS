//package com.inshodesign.bossrss;
//
//import com.inshodesign.bossrss.XMLModel.RSS;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
//import rx.Observable;
//
//
//public class RSSFeedClient {
//
//    //TODO -- MAKE DYNAMIC
////    private static String TESTURL = "https://www.espn.com/espnradio/feeds/rss/podcast.xml/_/id/2406595";
//    private static RSSFeedClient instance;
//    private RSSService rssService;
//
//
//
//    private RSSFeedClient(String endpoint) {
//
//
//        endpoint = "http://www.thestar.com/";
//
//        OkHttpClient.Builder client = new OkHttpClient.Builder();
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        client.addInterceptor(loggingInterceptor);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(endpoint)
//                .client(new OkHttpClient()) // Use OkHttp3 client
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // RxJava adapter
//                .addConverterFactory(SimpleXmlConverterFactory.create()) // Simple XML converter
//                .build();
//
//        rssService = retrofit.create(RSSService.class);
//
//    }
//
//    public static RSSFeedClient getInstance(String endpoint) {
//        if (instance == null) {
//            instance = new RSSFeedClient(endpoint);
//        }
//        return instance;
//    }
//
//    public Observable<RSS> getRSSFeed(String feedUrl) {
//        return rssService.getFeed(feedUrl);
//    }
//
//
//
//
//}
//
