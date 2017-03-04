package com.inshodesign.bossrss;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by JClassic on 3/3/2017.
 */


public interface RSSInterface {
//    @GET("rss/karlsruhe.xml")
//    Observable<RSS> getRSSFeed();
@GET
Observable<RSS> getFeed(@Url String feedUrl);
}
