
package com.inshodesign.bossrss;

        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.TextView;

        import com.facebook.FacebookSdk;
        import com.facebook.share.model.ShareLinkContent;
        import com.facebook.share.widget.ShareDialog;
        import com.inshodesign.bossrss.Adapters.RSSContentsAdapter;
        import com.inshodesign.bossrss.XMLModel.Channel;
        import com.inshodesign.bossrss.XMLModel.ParcebleItem;
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

public class RSSItemsFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private SmoothProgressBar progressbar;
    private String TAG = "TEST--RSSFRAG";
    RSSContentsAdapter mAdapter;
    private ArrayList<ParcebleItem> mDataset;
//    ShareDialog shareDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);

//        mNoLists = (TextView) view.findViewById(R.id.nolists);
        progressbar = (SmoothProgressBar) view.findViewById(R.id.progressbar);
//        FacebookSdk.sdkInitialize(getContext());
//        shareDialog = new ShareDialog(this);

        return view;
    }

    public static RSSItemsFragment newInstance(@Nullable final String listTitle, @Nullable String feedURL, @Nullable String imageURL,@NonNull final ArrayList<ParcebleItem> items) {
        final RSSItemsFragment fragment = new RSSItemsFragment();
        final Bundle args = new Bundle();
        args.putString("title",listTitle);
        args.putString("feedURL",feedURL);
        args.putString("imageURL",imageURL);
        args.putParcelableArrayList("items",items);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        //SET TITLE
        getArguments().getString("title");

        mDataset = getArguments().getParcelableArrayList("items");
        updateAdapter(mDataset);
//        filltheAdapter();
    }

//    public void shareURL() {
//
////            FacebookSdk.setApplicationId("1842775815961608");
////            FacebookSdk.sdkInitialize(getActivity());
////
////            ShareDialog shareDialog = new ShareDialog(this);
////            ShareLinkContent linkContent = new ShareLinkContent.Builder()
////                    .setContentTitle("Hello Facebook")
////                    .setContentDescription(
////                            "The 'Hello Facebook' sample  showcases simple Facebook integration")
////                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
////                    .build();
////
////            ShareDialog.show(this,linkContent);
////
//        if (ShareDialog.canShow(ShareLinkContent.class)) {
//
//            try {
//
//
////            if(getArguments().getString("feedURL") != null) {
//                String url = getArguments().getString("feedURL");
//                String imageURL = getArguments().getString("imageURL");
//
//                Log.d("TEST","SHARING... -- " + url);
//                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
//                        .setContentTitle("Your Title")
//                        .setContentDescription("Your Description")
//                        .setContentUrl(Uri.parse(url))
//                        .setImageUrl(Uri.parse(imageURL))
//                        .build();
//                ShareDialog.show(getActivity(),shareLinkContent);
//            } catch(Exception e ) {
//
//            }
//        }
//
//
//    }


    private void updateAdapter(ArrayList<ParcebleItem> items) {
        mAdapter = new RSSContentsAdapter(items, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

//    public void updateItemListAdapter(List<Channel.Item> items) {
//
//        mAdapter = new RSSContentsAdapter(items, getContext());
//        mRecyclerView.setAdapter(mAdapter);
//    }


    private void determinePattern(){
        //TODO

        /**
         * Determine pattern of the adapter
         *
         * spit out a the type of container we'll fill
         * */
    }

//    private void filltheAdapter() {
//
//        progressbar.setVisibility(View.VISIBLE);
//       final  Channel rssChannel = new Channel();
//
//        List<Channel.Item> listItems ;
//
//
//        RSSService xmlAdapterFor = APIService.createXmlAdapterFor(RSSService.class, "http://www.google.com");
//        Observable<RSS> rssObservable = xmlAdapterFor.getFeed(getArguments().getString("link"));
//
////        fooContainerObservable
////                .map(container -> container.getFooList())
////                .flatMap(foo -> transformFooToFooBar(foo))
////                .toList()
////                .subscribe(fooBarList -> /* Display the list */);
//
//        rssObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<RSS>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted() called");
//
//                        progressbar.setVisibility(View.GONE);
//
//                    }
//
//
//                    @Override
//                    public void onError(final Throwable e) {
//                        Log.d(TAG, "onError() called with: e = [" + e + "]");
//
//
//
//
//                    }
//
//                    @Override
//                    public void onNext(final RSS rss) {
//                        Log.d(TAG, "onNext() called with: rss = [" + rss + "]");
//                        if (rss.getChannel() != null) {
//
//
//                            mAdapter = new RSSContentsAdapter(rss.getChannel().getItemList(), getContext());
//                            mRecyclerView.setAdapter(mAdapter);
//
//
//                        }
//                        ;
//                    }
//                });
//    }



    public RSSList getCurrentList() {
        RSSList rssList = new RSSList();
        if(getArguments() != null) {
            rssList.setURL(getArguments().getString("feedURL"));
            rssList.setImageURL(getArguments().getString("imageURL"));
            rssList.setTitle(getArguments().getString("title"));
        }
        return rssList;
    }
}
