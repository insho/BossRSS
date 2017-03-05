package com.inshodesign.bossrss;

import android.content.Context;
import android.os.Bundle;
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
import com.inshodesign.bossrss.XMLModel.RSSList;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import rx.functions.Action1;

import static android.view.View.GONE;

/**
 * Created by JClassic on 2/21/2017.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {
    OnMainOptionSelectedListener mCallback;

    public interface OnMainOptionSelectedListener {
        void showRSSListFragment(RSSList rssList);
        void showRemoveDialog(Integer removeRowID);

    }
    private RxBus _rxBus = new RxBus();
    private RecyclerView mRecyclerView;
    RSSListAdapter mAdapter;
    private TextView mNoLists;
    private RecyclerView.LayoutManager mLayoutManager;
    private SmoothProgressBar progressbar;
//    @Override
//    public View onCreateView(LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        mRecyclerView = (RecyclerView) container.findViewById(R.id.recycler);
//        mNoLists = (TextView) container.findViewById(R.id.nolists);
//        return inflater.inflate(R.layout.fragment_main, container, false);
//    }

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

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        filltheAdapter();


    }

    public void showProgressBar(Boolean show) {
        if(show) {
            progressbar.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.GONE);

        }
    }

    public void filltheAdapter() {
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

                            //If it's a short click, send user to RSSListFragment for that row
                            if(event instanceof RSSList) {
                                RSSList rssList = (RSSList) event;

                                mCallback.showRSSListFragment(rssList);

                            } else if (event instanceof Integer){
                                mCallback.showRemoveDialog((Integer)event);
                            }
                        }

                    });



            mRecyclerView.setAdapter(mAdapter);

        } else {
            showRecyclerView(false);
        }


    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//        mCallback.onMainOptionSelected(position);
//        Toast.makeText(getActivity(), "ITS CLICKED: " + position, Toast.LENGTH_SHORT).show();
    }






}

