package com.inshodesign.bossrss.Interfaces;

import com.inshodesign.bossrss.XML_Models.RSS;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;


public interface RSSService {
@GET
Observable<RSS> getFeed(@Url String feedUrl);
}
