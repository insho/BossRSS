
package com.inshodesign.bossrss;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.TextView;

        import com.inshodesign.bossrss.Adapters.RSSContentsAdapter;
        import com.inshodesign.bossrss.XMLModel.Channel;
        import com.inshodesign.bossrss.XMLModel.RSS;
        import com.inshodesign.bossrss.XMLModel.RSSList;

        import java.util.ArrayList;
        import java.util.List;

        import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
        import rx.Observable;
        import rx.Subscriber;
        import rx.android.schedulers.AndroidSchedulers;
        import rx.schedulers.Schedulers;

/**
 * Created by JClassic on 3/5/2017.
 */

public class DisplayRSSFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private SmoothProgressBar progressbar;
    private String TAG = "TEST--RSSFRAG";
    RSSContentsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);

//        mNoLists = (TextView) view.findViewById(R.id.nolists);
        progressbar = (SmoothProgressBar) view.findViewById(R.id.progressbar);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        filltheAdapter();
    }

    private void determinePattern(){
        //TODO

        /**
         * Determine pattern of the adapter
         *
         * spit out a the type of container we'll fill
         * */
    }

    private void filltheAdapter() {

        progressbar.setVisibility(View.VISIBLE);
       final  Channel rssChannel = new Channel();

        List<Channel.Item> listItems ;


        RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class, "http://www.google.com");
        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(getArguments().getString("link"));

//        fooContainerObservable
//                .map(container -> container.getFooList())
//                .flatMap(foo -> transformFooToFooBar(foo))
//                .toList()
//                .subscribe(fooBarList -> /* Display the list */);

        rssObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RSS>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called");

                        progressbar.setVisibility(View.GONE);

                    }


                    @Override
                    public void onError(final Throwable e) {
                        Log.d(TAG, "onError() called with: e = [" + e + "]");




                    }

                    @Override
                    public void onNext(final RSS rss) {
                        Log.d(TAG, "onNext() called with: rss = [" + rss + "]");
                        if (rss.getChannel() != null) {


                            mAdapter = new RSSContentsAdapter(rss.getChannel().getItemList(), getContext());
                            mRecyclerView.setAdapter(mAdapter);


                        }
                        ;
                    }
                });
    }


}
