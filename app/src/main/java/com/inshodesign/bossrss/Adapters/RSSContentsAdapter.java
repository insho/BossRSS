
package com.inshodesign.bossrss.Adapters;

        import android.content.Context;
        import android.database.Cursor;
        import android.net.Uri;
        import android.provider.OpenableColumns;
        import android.support.annotation.Nullable;
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
        import android.widget.SeekBar;
        import android.widget.TextView;
        import com.inshodesign.bossrss.R;
        import com.inshodesign.bossrss.XMLModel.AudioStream;
        import com.inshodesign.bossrss.XMLModel.ParcebleItem;
        import com.squareup.picasso.Picasso;

        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLConnection;
        import java.util.ArrayList;
        import java.util.Locale;

public class RSSContentsAdapter extends RecyclerView.Adapter<RSSContentsAdapter.ViewHolder> {

    private ArrayList<ParcebleItem> mDataset;
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

    public RSSContentsAdapter(ArrayList<ParcebleItem> myDataset, RxBus rxBus, Context context) {
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

        /** If there is an image icon, show it**/
        String url =  mDataset.get(holder.getAdapterPosition()).getLink();
        String title = mDataset.get(holder.getAdapterPosition()).getTitle();
        if(url != null) {
            SpannableString text = new SpannableString(title);
            text.setSpan(new URLSpan(url), 0, title.length(), 0);
            holder.txtTitle.setMovementMethod(LinkMovementMethod.getInstance());
            holder.txtTitle.setText(text, TextView.BufferType.SPANNABLE);
        } else {
            holder.txtTitle.setText(title);
        }

        /** If the item has music or video, show the link **/
        if(mDataset.get(holder.getAdapterPosition()).getEnclosureLink() != null) {
            holder.btnPlay.setVisibility(View.GONE);
            holder.btnPlay.setVisibility(View.GONE);

            /** If the item is music, show the music player **/
            if(mDataset.get(holder.getAdapterPosition()).getEnclosureType() != null
            && mDataset.get(holder.getAdapterPosition()).getEnclosureType().contains("audio")) {
                holder.btnPlay.setVisibility(View.VISIBLE);

                /** Click play to activate media controller in main activity */
                holder.btnPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRxBus.send(new AudioStream(holder.getAdapterPosition(),mDataset.get(holder.getAdapterPosition()).getEnclosureLink(),true,mDataset.get(holder.getAdapterPosition()).getEnclosureLength()));
                    }
                });

//                /** Get file information */
//                Uri uri =  Uri.parse(mDataset.get(holder.getAdapterPosition()).getEnclosureLink());
//                Cursor c = mContext.getContentResolver().query(uri, null, null, null, null);

            }


        }

        if(mDataset.get(holder.getAdapterPosition()).getPubDate() != null) {
            holder.txtDate.setText(mDataset.get(holder.getAdapterPosition()).getPubDate());

        }

        /** Add image if applicable **/
        if(mDataset.get(holder.getAdapterPosition()).getContentURL() != null) {
            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(mDataset.get(position).getContentURL())
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        if(mDataset.get(holder.getAdapterPosition()).getMediaDescription() != null) {
            holder.txtDescription.setVisibility(View.VISIBLE);
            holder.txtDescription.setText(mDataset.get(position).getMediaDescription());
        } else if(mDataset.get(holder.getAdapterPosition()).getDescription() != null) {
            holder.txtDescription.setVisibility(View.VISIBLE);
            holder.txtDescription.setText(mDataset.get(position).getDescription());
        } else {
            holder.txtDescription.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
