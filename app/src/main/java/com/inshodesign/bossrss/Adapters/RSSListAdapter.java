package com.inshodesign.bossrss.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.inshodesign.bossrss.R;
import com.inshodesign.bossrss.Models.RSSList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RSSListAdapter extends RecyclerView.Adapter<RSSListAdapter.ViewHolder> {

    private RxBus _rxbus;
    private List<RSSList> mDataset;
    private Context mContext;
    private final String TAG = "TEST-ListAdap";

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public ImageView image;

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
    public RSSListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rsslist_recycler_row, parent, false);
        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RSSList rssList = mDataset.get(holder.getAdapterPosition());
        // If there is an image icon, show it
        holder.image.setVisibility(View.VISIBLE);
        if(rssList.getImageURI() !=null) {
        Log.d(TAG,"hasbitmap URI: " + rssList.getImageURI()) ;
//                Picasso.with(mContext).load(rssList.getImageURI())
//                        .into(holder.image);
//
//            holder.image.setVisibility(View.VISIBLE);
//            holder.image.setAdjustViewBounds(true);

            Picasso picasso = new Picasso.Builder(mContext)
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.e(TAG,"SAVED URI Image load failed " + rssList.getTitle());
                            holder.image.setVisibility(View.GONE);
                        }
                    }).build();

            picasso.load(rssList.getImageURI())
                    .fit()
                    .into(holder.image);
            ImageView testView = new ImageView(mContext);
//            holder.image.setAdjustViewBounds(true);

        } else if(rssList.getImageURL() != null) {
                    Log.d(TAG,"hasbitmap URL: " + rssList.getImageURL()) ;
            holder.image.setVisibility(View.VISIBLE);
//            Picasso.with(mContext).load(rssList.getImageURL()).fit().into(holder.image);

            Picasso picasso = new Picasso.Builder(mContext)
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.e(TAG,"URL Image load failed " + rssList.getTitle());
                            holder.image.setVisibility(View.GONE);
                        }
                    }).build();
            picasso.load(rssList.getImageURL())
                    .fit()
                    .into(holder.image);
            holder.image.setAdjustViewBounds(true);

        } else {
            holder.image.setVisibility(View.GONE);
        }

        Log.i(TAG,"TITLE: " + rssList.getTitle());
        // If there is no icon or title, show url and grey out the row, because it is considered incomplete
        if( !mDataset.get(position).hasTitle()) {
            holder.txtTitle.setText(rssList.getURL());
            holder.txtTitle.setAlpha(.5f);
        } else {
            holder.txtTitle.setText(rssList.getTitle());
            holder.txtTitle.setAlpha(1);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If it's a short click, send the object (with title, image etc)
                _rxbus.send(rssList);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //If it's a long click, send the row id only, for deletion
                _rxbus.sendLongClick(rssList);
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