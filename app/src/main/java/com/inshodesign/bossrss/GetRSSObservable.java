//package com.inshodesign.bossrss;
//
//import com.inshodesign.bossrss.XMLModel.RSS;
//
//import java.util.List;
//
//import rx.Observable;
//import rx.Subscriber;
//
///**
// * Created by JClassic on 3/5/2017.
// */
//
//public final class GetRSSObservable extends Observable<RSS> {
//
////    private AtomicLong counter;
//    private List<RSS> rss;
//
//
//    public static GetRSSObservable create(String endpoint) {
//        List<RSS> rss;
//
//        RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class, endpoint);
//        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(endpoint);
//
//        OnSubscribe<RSS> onSubscribe = new OnSubscribe<RSS>() {
//            @Override
//            public void call(Subscriber<? super RSS> t1) {
//                t1.onNext(rss..incrementAndGet());
//                t1.onCompleted();
//            }
//        };
//        return new GetRSSObservable(onSubscribe, counter);
//    }
//
//    private GetRSSObservable(OnSubscribe<RSS> onSubscribe, List<RSS> rss) {
//        super(onSubscribe);
//        this.rss = rss;
////        this.endpoint = endpoint;
//    }
//
//
//    public List<RSS> getRSS() {
//        return rss;
//    }
//}