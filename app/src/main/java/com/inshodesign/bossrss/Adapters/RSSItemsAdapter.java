
package com.inshodesign.bossrss.Adapters;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.text.SpannableString;
        import android.text.method.LinkMovementMethod;
        import android.text.style.URLSpan;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.inshodesign.bossrss.R;
        import com.inshodesign.bossrss.Models.AudioStream;
        import com.inshodesign.bossrss.XML_Models.Item;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

public class RSSItemsAdapter extends RecyclerView.Adapter<RSSItemsAdapter.ViewHolder> {

    private ArrayList<Item> mDataset;
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

    public RSSItemsAdapter(ArrayList<Item> myDataset, RxBus rxBus, Context context) {
        mDataset = myDataset;
        mRxBus = rxBus;
        mContext = context;
    }

    @Override
    public RSSItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rssitems_recycler_row, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.btnPlay.setVisibility(View.GONE);

        final Item rssItem = mDataset.get(holder.getAdapterPosition());
        String title = rssItem.getTitle();

//        String url;
        if(rssItem.getLink() != null) {
            linkifyTextView(holder.txtTitle,title,rssItem.getLink());
        } else if(rssItem.getEnclosure() != null &&
                rssItem.getEnclosure().getUrl() != null ) {

            linkifyTextView(holder.txtTitle,title,rssItem.getEnclosure().getUrl());

//            SpannableString text = new SpannableString(title);
//            text.setSpan(new URLSpan(rssItem.getEnclosure().getUrl()), 0, title.length(), 0);
//            holder.txtTitle.setMovementMethod(LinkMovementMethod.getInstance());
//            holder.txtTitle.setText(text, TextView.BufferType.SPANNABLE);

        } else {
            holder.txtTitle.setText(title);
        }



        if(rssItem.getPubDate() != null) {
            holder.txtDate.setText(rssItem.getPubDate());
        }

        /** If the item is music, show the music player **/
        if(rssItem.getEnclosure() != null
                && rssItem.getEnclosure().getType() != null
                && rssItem.getEnclosure().getType().contains("audio")) {
            holder.btnPlay.setVisibility(View.VISIBLE);

            /** Click play to activate media controller in main activity */
            holder.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRxBus.send(new AudioStream(holder.getAdapterPosition()
                            ,rssItem.getEnclosure().getUrl()
                            ,true
                            ,rssItem.getEnclosure().getLength()));
                }
            });

        }

        //Add image if possible
        if(rssItem.getContent()!=null && rssItem.getContent().size()>0) {
            if(rssItem.getContent().get(0).getUrl() != null) {
//                holder.image.setVisibility(View.VISIBLE);
//                Picasso.with(mContext).load(rssItem.getContent().get(0).getUrl())
//                        .into(holder.image);

                loadAndLinkPicture(holder.image,rssItem.getContent().get(0).getUrl());

            } else if(rssItem.getContent().get(0).getThumbnail() != null &&
                    rssItem.getContent().get(0).getThumbnail().getUrl() != null )  {
//                holder.image.setVisibility(View.VISIBLE);
//                Picasso.with(mContext).load(rssItem.getContent().get(0).getThumbnail().getUrl())
//                        .into(holder.image);
                loadAndLinkPicture(holder.image,rssItem.getContent().get(0).getThumbnail().getUrl());

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


    private void loadAndLinkPicture(ImageView image, final String imageUrl){
        image.setVisibility(View.VISIBLE);
        Picasso.with(mContext).load(imageUrl)
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxBus.send(imageUrl);
            }
        });
    }

    private void linkifyTextView(TextView textView, String text, String linkUrl) {
        SpannableString linkedText = new SpannableString(text);
        linkedText.setSpan(new URLSpan(linkUrl), 0, text.length(), 0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(linkedText, TextView.BufferType.SPANNABLE);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
