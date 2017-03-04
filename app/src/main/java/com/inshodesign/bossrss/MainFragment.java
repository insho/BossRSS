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

import rx.functions.Action1;

import static android.view.View.GONE;

/**
 * Created by JClassic on 2/21/2017.
 */

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {
    OnMainOptionSelectedListener mCallback;

    public interface OnMainOptionSelectedListener {
        void onMainOptionSelected(int position);
    }
    private RxBus _rxBus = new RxBus();
    private RecyclerView mRecyclerView;
    RSSListAdapter mAdapter;
    private TextView mNoLists;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) container.findViewById(R.id.recycler);
        mNoLists = (TextView) container.findViewById(R.id.nolists);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        filltheAdapter();


    }

    public void filltheAdapter() {
        List<RSSList> rssLists = InternalDB.getInstance(getContext()).getRSSLists();
        if(rssLists == null || rssLists.size() == 0) {

            mAdapter = new RSSListAdapter(rssLists, _rxBus, getContext());
            showRecyclerView(true);

            _rxBus.toObserverable()
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object event) {

                            if(event instanceof Integer) {
//                            getBeerData(beerRecTMP,article );
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
            mRecyclerView.setVisibility(View.GONE);
            mNoLists.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoLists.setVisibility(View.GONE);
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
        mCallback.onMainOptionSelected(position);
    }


//    @Override
//    public void onDialogPositiveClick(String rssURI) {
//        Toast.makeText(getActivity(), "POS CLICK", Toast.LENGTH_SHORT).show();
//    }




//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            mStackLevel = savedInstanceState.getInt("level");
//        }
//    }


}

