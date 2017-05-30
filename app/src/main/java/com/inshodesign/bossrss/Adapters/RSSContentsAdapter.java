
package com.inshodesign.bossrss.Adapters;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.text.SpannableString;
        import android.text.method.LinkMovementMethod;
        import android.text.style.URLSpan;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.inshodesign.bossrss.R;
        import com.inshodesign.bossrss.Models.AudioStream;
//        import com.inshodesign.bossrss.Models.ParcebleItem;
//        import com.inshodesign.bossrss.XML_Models.Item;
        import com.inshodesign.bossrss.XML_Models.Channel;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

public class RSSContentsAdapter extends RecyclerView.Adapter<RSSContentsAdapter.ViewHolder> {

    private ArrayList<Channel.Item> mDataset;
    private Context mContext;
    private RxBus mRxBus;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private ImageView image;
        private TextView txtDescription;
        private TextView txtAuthor;
        private TextView txtDate;
        private TextView txtDataLink;
        private TextView txtMediaFileName;
        private TextView txtMediaFileSize;
        private TextView txtMediaFileType;
        private ImageButton btnPlay;

        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.title);
            txtDescription = (TextView) v.findViewById(R.id.description);
            txtAuthor = (TextView) v.findViewById(R.id.author);
            txtDate = (TextView) v.findViewById(R.id.date);
            image = (ImageView) v.findViewById(R.id.image);
            txtDataLink = (TextView) v.findViewById(R.id.datalink);
            btnPlay = (ImageButton) v.findViewById(R.id.playbutton);
            txtMediaFileName = (TextView) v.findViewById(R.id.mediafilename);
            txtMediaFileType = (TextView) v.findViewById(R.id.mediafiletype);
            txtMediaFileSize = (TextView) v.findViewById(R.id.mediafilesize);
        }
    }

    public RSSContentsAdapter(ArrayList<Channel.Item> myDataset, RxBus rxBus, Context context) {
        mDataset = myDataset;
        mRxBus = rxBus;
        mContext = context;
    }

    @Override
    public RSSContentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rssitems_recycler_row, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Log.i("TEST","item null: " + (mDataset == null));
        Log.i("TEST","item pos null: " + (mDataset.get(holder.getAdapterPosition()) == null));
        Log.i("TEST","item pos enclosure null: " + (mDataset.get(holder.getAdapterPosition()).getEnclosure() == null));
        Log.i("TEST","item pos getDescription null: " + (mDataset.get(holder.getAdapterPosition()).getDescription() == null));
        Log.i("TEST","item pos getContent null: " + (mDataset.get(holder.getAdapterPosition()).getContent() == null));
        Log.i("TEST","item pos getThumbnailList null: " + (mDataset.get(holder.getAdapterPosition()).getThumbnailList() == null));




        holder.btnPlay.setVisibility(View.GONE);


        Channel.Item rssItem = mDataset.get(holder.getAdapterPosition());
        String title = rssItem.getTitle();

        /** If there is an image icon, show it**/
        String url;
        if(rssItem.getEnclosure() != null &&
                rssItem.getEnclosure().getUrl() != null ) {
            url = rssItem.getEnclosure().getUrl();

            SpannableString text = new SpannableString(title);
            text.setSpan(new URLSpan(url), 0, title.length(), 0);
            holder.txtTitle.setMovementMethod(LinkMovementMethod.getInstance());
            holder.txtTitle.setText(text, TextView.BufferType.SPANNABLE);



            /** If the item is music, show the music player **/
            if(mDataset.get(holder.getAdapterPosition()).getEnclosure().getType() != null
                    && mDataset.get(holder.getAdapterPosition()).getEnclosure().getType().contains("audio")) {
                holder.btnPlay.setVisibility(View.VISIBLE);

                /** Click play to activate media controller in main activity */
                holder.btnPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRxBus.send(new AudioStream(holder.getAdapterPosition(),mDataset.get(holder.getAdapterPosition()).getEnclosure().getUrl(),true,mDataset.get(holder.getAdapterPosition()).getEnclosure().getLength()));
                    }
                });

            }



        } else {
            holder.txtTitle.setText(title);
        }



        if(rssItem.getPubDate() != null) {
            holder.txtDate.setText(rssItem.getPubDate());
        }

        //Add image if possible
        if(rssItem.getContent()!=null && rssItem.getContent().size()>0) {
            if(rssItem.getContent().get(0).getUrl() != null) {
                holder.image.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(rssItem.getContent().get(0).getUrl())
                        .into(holder.image);
            } else if(rssItem.getContent().get(0).getThumbnail() != null)  {
                holder.image.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(rssItem.getContent().get(0).getThumbnail().getUrl())
                        .into(holder.image);
            } else {
                holder.image.setVisibility(View.GONE);
            }


            if(rssItem.getContent().get(0).getDescription() != null) {
                holder.txtDescription.setVisibility(View.VISIBLE);
                holder.txtDescription.setText(rssItem.getContent().get(0).getDescription());
            } else if(rssItem.getDescription() != null) {
                holder.txtDescription.setVisibility(View.VISIBLE);
                holder.txtDescription.setText(rssItem.getDescription());
            } else {
                holder.txtDescription.setVisibility(View.GONE);
            }


        }




    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
