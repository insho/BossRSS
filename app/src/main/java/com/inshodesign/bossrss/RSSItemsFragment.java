
package com.inshodesign.bossrss;

        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import com.inshodesign.bossrss.Adapters.RSSContentsAdapter;
        import com.inshodesign.bossrss.XMLModel.ParcebleItem;
        import com.inshodesign.bossrss.XMLModel.RSSList;
        import java.util.ArrayList;


/**
 * Created by JClassic on 3/5/2017.
 */

public class RSSItemsFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    RSSContentsAdapter mAdapter;
    private ArrayList<ParcebleItem> mDataset;

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
        mAdapter = new RSSContentsAdapter(items, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }



//    private void determinePattern(){
        //TODO

        /**
         * Determine pattern of the adapter? Would that work?
         *
         * spit out a the type of container to fill with the stream...
         * */
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
