
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
        import com.inshodesign.bossrss.XMLModel.Channel;
        import com.inshodesign.bossrss.XMLModel.RSS;
        import com.inshodesign.bossrss.XMLModel.RSSList;
        import com.squareup.picasso.Picasso;

        import java.util.List;

/**
 * Created by JClassic on 3/4/2017.
 */

public class RSSContentsAdapter extends RecyclerView.Adapter<RSSContentsAdapter.ViewHolder> {

//    private RxBus _rxbus;
    private List<Channel.Item> mDataset;
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

    public RSSContentsAdapter(List<Channel.Item> myDataset, Context context) {
        mDataset = myDataset;
//        _rxbus = rxBus;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RSSContentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rsslist_recycler_row, parent, false);


        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        /** If there is an image icon, show it**/
//        if(mDataset.get(position).hasImage()) {
//            Drawable image = new BitmapDrawable(mContext.getResources(), mDataset.get(position).getImage());
//
//            holder.image.setVisibility(View.VISIBLE);
//            holder.image.setImageDrawable(image);
//            holder.image.setAdjustViewBounds(true);
//
//        }  else {
//            holder.image.setVisibility(View.GONE);
//
//        }


//        Log.d("InternalDB","Holdertest: " + mDataset.get(position).;
        holder.txtTitle.setText(mDataset.get(position).getTitle());




        if(mDataset.get(position).getMediaThumbnail() != null && mDataset.get(position).getMediaThumbnail().getUrl() != null) {
            Picasso.with(mContext).load(mDataset.get(position).getMediaThumbnail().getUrl())
//                    .error(R.drawable.placeholder)
//                    .placeholder(R.drawable.placeholder)
                    .into(holder.image);

        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //If it's a short click, send the object (with title, image etc)
//                _rxbus.send(mDataset.get(holder.getAdapterPosition()));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


//    public RSS getRSSItem(int position) {
//
//        return mDataset.get(position);
//
//    }



}
