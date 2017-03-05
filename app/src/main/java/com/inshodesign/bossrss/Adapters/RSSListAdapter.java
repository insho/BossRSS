package com.inshodesign.bossrss.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inshodesign.bossrss.R;
import com.inshodesign.bossrss.XMLModel.RSS;
import com.inshodesign.bossrss.XMLModel.RSSList;
import com.squareup.picasso.Picasso;

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
//        public TextView txtDescription;

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

        /** If there is an image icon, show it**/
        if(mDataset.get(position).hasImage()) {
            Drawable image = new BitmapDrawable(mContext.getResources(), mDataset.get(position).getImage());

                Picasso.with(mContext).load(mDataset.get(position).getImageURI())
//                    .error(R.drawable.placeholder)
//                    .placeholder(R.drawable.placeholder)
                        .into(holder.image);

            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageDrawable(image);
            holder.image.setAdjustViewBounds(true);

        }  else {
            holder.image.setVisibility(View.GONE);

        }

        Log.d("InternalDB","Holdertest: " + mDataset.get(position).getTitle());


        /** If there is no icon or title, show url and grey out the row, because it is considered incomplete **/
        if( !mDataset.get(position).hasTitle()) {
            holder.txtTitle.setText(mDataset.get(position).getURL());
            holder.txtTitle.setAlpha(.5f);
        } else {

            holder.txtTitle.setText(mDataset.get(position).getTitle());
            holder.txtTitle.setAlpha(1);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If it's a short click, send the object (with title, image etc)
                _rxbus.send(mDataset.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //If it's a long click, send the row id only, for deletion
                _rxbus.send(mDataset.get(holder.getAdapterPosition()).getId());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public RSSList getList(int position) {

        return mDataset.get(position);

    }



}