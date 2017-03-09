
package com.inshodesign.bossrss;

        import android.content.Context;
        import android.media.AudioManager;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.SystemClock;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.MediaController;
        import android.widget.Toast;

//        import com.github.piasy.rxandroidaudio.StreamAudioPlayer;
        import com.inshodesign.bossrss.Adapters.RSSContentsAdapter;
        import com.inshodesign.bossrss.Adapters.RxBus;
        import com.inshodesign.bossrss.XMLModel.AudioStream;
        import com.inshodesign.bossrss.XMLModel.ParcebleItem;
        import com.inshodesign.bossrss.XMLModel.RSSList;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.IOException;
        import java.nio.Buffer;
        import java.util.ArrayList;

        import rx.Observable;
        import rx.functions.Action1;
        import rx.schedulers.Schedulers;
        import rx.subscriptions.Subscriptions;

public class RSSItemsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    RSSContentsAdapter mAdapter;
    private ArrayList<ParcebleItem> mDataset;
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

        getArguments().getString("title");
        mDataset = getArguments().getParcelableArrayList("items");
        updateAdapter(mDataset);
    }



    private void updateAdapter(ArrayList<ParcebleItem> items) {
        mAdapter = new RSSContentsAdapter(items, _rxBus, getContext());

        _rxBus.toClickObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {

                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        /** Stupid way of differentiating between short and long clicks... **/
                        if(event instanceof AudioStream) {
                            AudioStream audioStream = (AudioStream) event;
                            mCallback.playAudio(audioStream);

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
