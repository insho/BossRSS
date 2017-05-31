
package com.inshodesign.bossrss.Fragments;

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.SystemClock;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

//        import com.github.piasy.rxandroidaudio.StreamAudioPlayer;
        import com.inshodesign.bossrss.Adapters.RSSContentsAdapter;
        import com.inshodesign.bossrss.Adapters.RxBus;
        import com.inshodesign.bossrss.Interfaces.OnFragmentInteractionListener;
        import com.inshodesign.bossrss.Models.AudioStream;
//        import com.inshodesign.bossrss.Models.ParcebleItem;
        import com.inshodesign.bossrss.Models.RSSList;
        import com.inshodesign.bossrss.R;
        import com.inshodesign.bossrss.XML_Models.Channel;
//        import com.inshodesign.bossrss.XML_Models.Item;

        import java.util.ArrayList;

        import rx.functions.Action1;

        import static com.inshodesign.bossrss.MainActivity.isUniqueClick;

public class RSSItemsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    RSSContentsAdapter mAdapter;
    private ArrayList<Channel.Item> mDataset;
    private RxBus _rxBus = new RxBus();
    private long mLastClickTime = 0;

    OnFragmentInteractionListener mCallback;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        return view;
    }

    public static RSSItemsFragment newInstance(@Nullable final String listTitle
            , @Nullable String feedURL
            , @Nullable String imageURL
            ,@NonNull final ArrayList<Channel.Item> items) {
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

        getArguments().getString("title");
        mDataset = getArguments().getParcelableArrayList("items");
        updateAdapter(mDataset);
    }



    private void updateAdapter(ArrayList<Channel.Item> items) {
        mAdapter = new RSSContentsAdapter(items, _rxBus, getContext());

        _rxBus.toClickObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {

                       if(isUniqueClick(750,mLastClickTime)) {
                           if(event instanceof AudioStream) {
                               AudioStream audioStream = (AudioStream) event;
                               mCallback.playAudio(audioStream);

                           } else if(event instanceof String) {
                               // Follow the link, to image url or the story
                               Intent intent = new Intent();
                               intent.setAction(Intent.ACTION_VIEW);
                               intent.addCategory(Intent.CATEGORY_BROWSABLE);
                               intent.setData(Uri.parse((String) event));
                               startActivity(intent);
                           }

                       }

                    }

                });
        mRecyclerView.setAdapter(mAdapter);
    }

    public RSSList getCurrentList() {
        RSSList rssList = new RSSList();
        if(getArguments() != null) {
            rssList.setURL(getArguments().getString("feedURL"));
            rssList.setImageURL(getArguments().getString("imageURL"));
            rssList.setTitle(getArguments().getString("title"));
        }
        return rssList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


}
