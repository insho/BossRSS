package com.inshodesign.bossrss.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.inshodesign.bossrss.Adapters.RSSListAdapter;
import com.inshodesign.bossrss.Adapters.RxBus;
import com.inshodesign.bossrss.Database.InternalDB;
import com.inshodesign.bossrss.Interfaces.OnFragmentInteractionListener;
import com.inshodesign.bossrss.Models.RSSList;
import com.inshodesign.bossrss.R;

import java.util.List;

import rx.functions.Action1;

import static com.inshodesign.bossrss.MainActivity.isUniqueClick;

/**
 * Displays saved RSS Lists. They can be clicked on to download current items for the list
 * and show them in {@link RSSItemsFragment}
 */
public class RSSListFragment extends Fragment {
    OnFragmentInteractionListener mCallback;
    private long mLastClickTime = 0;


    private RxBus _rxBus = new RxBus();
    private RecyclerView mRecyclerView;
    private RSSListAdapter mAdapter;
    private TextView mNoLists;

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

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerMain);
        mNoLists = (TextView) view.findViewById(R.id.nolists);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
            updateAdapter();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        updateAdapter();
    }

    /**
     * Refreshes RSS lists, pulling saved RSS lists from the db and setting up adapter
     *
     * @see com.inshodesign.bossrss.MainActivity#onRemoveRSSDialogPositiveClick(String)
     * @see com.inshodesign.bossrss.MainActivity#saveRSSFeed(String, boolean)
     */
    public void updateAdapter() {

        //Initialize the rsslist;
        List<RSSList> rssLists = InternalDB.getInstance(getContext()).getRSSLists();

        if(rssLists != null && rssLists.size()>0) {
            Log.d("InternalDB","Filladapterlist TITLE: " + rssLists.get(0).getImageURI());
        }

        if(rssLists != null && rssLists.size() > 0) {
            mAdapter = new RSSListAdapter(rssLists, _rxBus, getContext());
                Log.d("InternalDB", "count: " + mAdapter.getItemCount());

            showRecyclerView(true);

            _rxBus.toClickObserverable()
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object event) {

                            if(isUniqueClick(750,mLastClickTime)) {
                                if(event instanceof RSSList) {
                                    RSSList rssList = (RSSList) event;
                                    mCallback.getRSSFeed(rssList.getURL());
                                }
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();

                        }

                    });

            _rxBus.toLongClickObserverable()
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object event) {
                            if(isUniqueClick(750,mLastClickTime)) {
                                if(event instanceof RSSList) {
                                    RSSList rssList = (RSSList) event;
                                    mCallback.showRemoveDialog(rssList.getURL());

                                }
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();


                        }

                    });

            mRecyclerView.setAdapter(mAdapter);

        } else {
            showRecyclerView(false);
        }


    }

    /**
     * Toggles between showing recycler view and showing "No lists saved" message
     * @param show bool true to show recyler with saved lists, false to hide it and show no lists message
     */
    private void showRecyclerView(boolean show) {
        if(show) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoLists.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoLists.setVisibility(View.VISIBLE);
        }
    }

}

