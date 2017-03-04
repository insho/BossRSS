package com.inshodesign.bossrss.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inshodesign.bossrss.R;
import com.inshodesign.bossrss.XMLModel.RSSList;

import java.util.List;

/**
 * Created by JClassic on 3/4/2017.
 */

public class RSSListAdapter extends RecyclerView.Adapter<RSSListAdapter.ViewHolder> {

    private RxBus _rxbus;
    private List<RSSList> mDataset;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public ImageView image;
        public TextView txtDescription;

        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.title);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }

    public RSSListAdapter(List<RSSList> myDataset, RxBus rxBus, Context context) {
        mDataset = myDataset;
        _rxbus = rxBus;
         mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RSSListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rsslist_recycler_row, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(mDataset.get(position).getImage() != null) {
            Drawable image = new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeByteArray(mDataset.get(position).getImage(), 0, mDataset.get(position).getImage().length));
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageDrawable(image);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.txtTitle.setText(mDataset.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _rxbus.send(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}