package com.inshodesign.bossrss;

import com.inshodesign.bossrss.XMLModel.RSS;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;


public interface RSSService {
@GET
Observable<RSS> getFeed(@Url String feedUrl);
}
