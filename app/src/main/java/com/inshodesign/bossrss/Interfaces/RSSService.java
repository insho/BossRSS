package com.inshodesign.bossrss.Interfaces;

import com.inshodesign.bossrss.XML_Models.RSS;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Interface between {@link com.inshodesign.bossrss.MainActivity#getRSSFeed(String)} and API Service
 */
public interface RSSService {
@GET
Observable<RSS> getFeed(@Url String feedUrl);
}
