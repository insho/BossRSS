package com.inshodesign.bossrss;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.inshodesign.bossrss.Adapters.RSSListAdapter;
import com.inshodesign.bossrss.Adapters.RxBus;
import com.inshodesign.bossrss.DB.InternalDB;
import com.inshodesign.bossrss.XMLModel.Channel;
import com.inshodesign.bossrss.XMLModel.RSS;
import com.inshodesign.bossrss.XMLModel.RSSList;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import rx.functions.Action1;

import static android.view.View.GONE;

/**
 * Created by JClassic on 2/21/2017.
 */

public class MainFragment extends Fragment {
    OnMainOptionSelectedListener mCallback;

    public interface OnMainOptionSelectedListener {
        void getRSSFeed(final String feedURL);
        void showRemoveDialog(Integer removeRowID);
    }

    private RxBus _rxBus = new RxBus();
    private RecyclerView mRecyclerView;
    RSSListAdapter mAdapter;
    private TextView mNoLists;
    private SmoothProgressBar progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerMain);
        mNoLists = (TextView) view.findViewById(R.id.nolists);
        progressbar = (SmoothProgressBar) view.findViewById(R.id.progressbar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
//        if(mAdapter == null) {
            updateAdapter();
//        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        updateAdapter();
    }

    public void showProgressBar(Boolean show) {
        if(show) {
            progressbar.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.GONE);

        }
    }

    public void updateAdapter() {

        //Initialize the rsslist;
        List<RSSList> rssLists = InternalDB.getInstance(getContext()).getRSSLists(getContext());

        if(rssLists != null && rssLists.size()>0) {
            Log.d("InternalDB","Filladapterlist TITLE: " + rssLists.get(0).getTitle());
        }

        if(rssLists != null && rssLists.size() > 0) {
            mAdapter = new RSSListAdapter(rssLists, _rxBus, getContext());
                Log.d("InternalDB", "count: " + mAdapter.getItemCount());

            showRecyclerView(true);

            _rxBus.toClickObserverable()
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object event) {

                            /** Stupid way of differentiating between short and long clicks... **/
                            if(event instanceof RSSList) {
                                RSSList rssList = (RSSList) event;
                                Log.d("TEST -- FRAGMAIN","callback to showRSS");
                                mCallback.getRSSFeed(rssList.getURL());

                            } else if (event instanceof Integer){
                                /** On long click show remove list dialog **/
                                mCallback.showRemoveDialog((Integer)event);
                            }
                        }

                    });



            mRecyclerView.setAdapter(mAdapter);

        } else {
            showRecyclerView(false);
        }


    }


    /** If there are no saved lists, hide recycler and show No Lists message **/
    private void showRecyclerView(boolean show) {
        if(show) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoLists.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoLists.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnMainOptionSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//
//    }

}

