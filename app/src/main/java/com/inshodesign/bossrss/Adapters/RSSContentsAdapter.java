
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
//        private ImageButton btnStop;
//        private SeekBar seekBar;



        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.title);
            txtDescription = (TextView) v.findViewById(R.id.description);
            txtAuthor = (TextView) v.findViewById(R.id.author);
            txtDate = (TextView) v.findViewById(R.id.date);
            image = (ImageView) v.findViewById(R.id.image);
            txtDataLink = (TextView) v.findViewById(R.id.datalink);
            btnPlay = (ImageButton) v.findViewById(R.id.playbutton);
//            btnStop = (ImageButton) v.findViewById(R.id.cancel_button);
            txtMediaFileName = (TextView) v.findViewById(R.id.mediafilename);
            txtMediaFileType = (TextView) v.findViewById(R.id.mediafiletype);
            txtMediaFileSize = (TextView) v.findViewById(R.id.mediafilesize);

//            seekBar = (SeekBar) v.findViewById(R.id.seekbar);


        }
    }

    public RSSContentsAdapter(ArrayList<ParcebleItem> myDataset, RxBus rxBus, Context context) {
        mDataset = myDataset;
        mRxBus = rxBus;
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

        /** If the item has music or video, show the link **/
        if(mDataset.get(holder.getAdapterPosition()).getEnclosureLink() != null) {
//            holder.txtDataLink.setVisibility(View.VISIBLE);
//            holder.txtDataLink.setText(mDataset.get(holder.getAdapterPosition()).getEnclosureLink());


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

                /** Get file information */
                Uri uri =  Uri.parse(mDataset.get(holder.getAdapterPosition()).getEnclosureLink());
                Cursor c = mContext.getContentResolver().query(uri, null, null, null, null);

                try {
                    holder.txtMediaFileName.setText(mContext.getFileStreamPath(url).getName());

//                    System.out.println(FilenameUtils.getBaseName(url.getPath())); // -> file
//                    System.out.println(FilenameUtils.getExtension(url.getPath())); // -> xml
//                    System.out.println(FilenameUtils.getName(url.getPath())); // -> file.xml
//
//                    int nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                    int sizeIndex = c.getColumnIndex(OpenableColumns.SIZE);
//                    c.moveToFirst();
//
//                    holder.txtMediaFileName.setText(c.getString(nameIndex));
//                    holder.txtMediaFileSize.setText(Long.toString(c.getLong(sizeIndex)));

//                    c.close();
                } catch(Exception e) {
                    // Log exception thrown
                    Log.d("TEST", "Error getting filename and size data: " + e.getMessage());

                }



//
//                if(mDataset.get(holder.getAdapterPosition()).getEnclosureLength() != null) {
//                   holder.seekBar.setVisibility(View.VISIBLE);
//                    holder.seekBar.setAlpha(.5f);
//                    holder.seekBar.setMax(mDataset.get(holder.getAdapterPosition()).getEnclosureLength());
//                }
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
            holder.txtDescription.setText(mDataset.get(position).getMediaDescription());
        }



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
