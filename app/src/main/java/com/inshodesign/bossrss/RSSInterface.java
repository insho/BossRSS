package com.inshodesign.bossrss;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JClassic on 3/3/2017.
 */


public interface RSSInterface {
    @GET("/xml/simple.xml")
    Observable<RSSObject> getRSSFeed();

}
