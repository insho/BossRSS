
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

        import com.inshodesign.bossrss.XMLModel.RSS;
        import com.inshodesign.bossrss.XMLModel.RSSList;

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

    private void filltheADapter() {

        RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class, getArguments().getString("link"));
        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(getArguments().getString("link"));

        rssObservable.subscribeOn(Schedulers.io())
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


                            /** Assign a title for the feed*/
                            if (rss.getChannel() != null && !rssList.hasTitle()) {
                                rssList.setTitle(rss.getChannel().getTitle());
                            }

                            /** Get imageURL*/
                            if (rss.getChannel() != null && rss.getChannel().getImage() != null && rss.getChannel().getImage().getUrl() != null) {
                                rssList.setImageURL(rss.getChannel().getImage().getUrl());
                            }

                        }
                        ;
                    }
                });
    }


}
