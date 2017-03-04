package com.inshodesign.bossrss;

import com.inshodesign.bossrss.XMLModel.RSS;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by JClassic on 3/3/2017.
 */


public interface RSSService {
@GET
Observable<RSS> getFeed(@Url String feedUrl);
}
