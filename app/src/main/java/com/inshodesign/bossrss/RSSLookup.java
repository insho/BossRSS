//package com.inshodesign.bossrss;
//
//import android.util.Log;
//
//import com.inshodesign.bossrss.XMLModel.RSS;
//import com.inshodesign.bossrss.XMLModel.RSSList;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * Created by JClassic on 3/5/2017.
// */
//
//public class RSSLookup extends Observable {
//
//    private String TAG = "TEST -- RSSLOOK";
//    private String mEndpointURL;
//
//    public RSSLookup(final String endpointURL, )
//    {
//        this.mEndpointURL = endpointURL;
//        this.rowID = rowID;
//        this.title = title;
//        this.context = context;
////            this.addMediaURIListener = callback;
//    }
//
//
//    private void getRSS(final String endpoint) {
//
//        final RSSList rssList = new RSSList();
//
//        //TODO -- SWITCH FOR REAL URL!
////        String endpoint = "http://www.thestar.com/feeds.topstories.rss";
//        RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class, "http://www.thestar.com/");
//        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(endpoint);
//        rssObservable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<RSS>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted() called");
//
//                    }
//
//
//                    @Override
//                    public void onError(final Throwable e) {
//                        Log.d(TAG, "onError() called with: e = [" + e + "]");
//                    }
//
//                    @Override
//                    public void onNext(final RSS rss) {
//                        Log.d(TAG, "onNext() called with: rss = [" + rss + "]");
//                        if (rss.getChannel() != null) {
//
//
//                            /** Assign a title for the feed*/
//                            if (rss.getChannel() != null && !rssList.hasTitle()) {
//                                rssList.setTitle(rss.getChannel().getTitle());
//                            }
//
//                            /** Get imageURL*/
//                            if (rss.getChannel() != null && rss.getChannel().getImage() != null && rss.getChannel().getImage().getUrl() != null) {
//                                rssList.setImageURL(rss.getChannel().getImage().getUrl());
//                            }
//
//                        }
//                    }
//                });
//
//
//    }
//}
