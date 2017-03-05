package com.inshodesign.bossrss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by JClassic on 3/5/2017.
 */

public class ListFragment extends Fragment  {

    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
//        mNoLists = (TextView) view.findViewById(R.id.nolists);
//        progressbar = (SmoothProgressBar) view.findViewById(R.id.progressbar);
        return view;
    }

}
