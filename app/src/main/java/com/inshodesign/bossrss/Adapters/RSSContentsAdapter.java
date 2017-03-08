
package com.inshodesign.bossrss.Adapters;

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.widget.RecyclerView;
        import android.text.SpannableString;
        import android.text.method.LinkMovementMethod;
        import android.text.style.ForegroundColorSpan;
        import android.text.style.URLSpan;
        import android.text.util.Linkify;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.inshodesign.bossrss.R;
        import com.inshodesign.bossrss.XMLModel.ParcebleItem;
        import com.squareup.picasso.Picasso;
        import java.util.ArrayList;

public class RSSContentsAdapter extends RecyclerView.Adapter<RSSContentsAdapter.ViewHolder> {

    private ArrayList<ParcebleItem> mDataset;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private ImageView image;
        private TextView txtDescription;
        private TextView txtAuthor;
        private TextView txtDate;
        private TextView txtDataLink;

        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.title);
            txtDescription = (TextView) v.findViewById(R.id.description);
            txtAuthor = (TextView) v.findViewById(R.id.author);
            txtDate = (TextView) v.findViewById(R.id.date);
            image = (ImageView) v.findViewById(R.id.image);
            txtDataLink = (TextView) v.findViewById(R.id.datalink);
        }
    }

    public RSSContentsAdapter(ArrayList<ParcebleItem> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RSSContentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rssitems_recycler_row, parent, false);


        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        /** If there is an image icon, show it**/


        String url =  mDataset.get(holder.getAdapterPosition()).getLink();
        String title = mDataset.get(holder.getAdapterPosition()).getTitle();
        if(url != null) {
//            Linkify.addLinks(holder.txtTitle, Linkify.ALL);

            SpannableString text = new SpannableString(title);
            text.setSpan(new URLSpan(url), 0, title.length(), 0);
            holder.txtTitle.setMovementMethod(LinkMovementMethod.getInstance());

            holder.txtTitle.setText(text, TextView.BufferType.SPANNABLE);
        } else {
            holder.txtTitle.setText(title);
        }

        if(mDataset.get(holder.getAdapterPosition()).getEnclosureLink() != null) {
            holder.txtDataLink.setVisibility(View.VISIBLE);
            holder.txtDataLink.setText(mDataset.get(holder.getAdapterPosition()).getEnclosureLink());
        }

        if(mDataset.get(holder.getAdapterPosition()).getPubDate() != null) {
            holder.txtDate.setText(mDataset.get(holder.getAdapterPosition()).getPubDate());

        }

        if(mDataset.get(holder.getAdapterPosition()).getContentURL() != null) {
            Picasso.with(mContext).load(mDataset.get(position).getContentURL())
                    .into(holder.image);
        }

        if(mDataset.get(holder.getAdapterPosition()).getMediaDescription() != null) {
            holder.txtDescription.setText(mDataset.get(position).getMediaDescription());
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
