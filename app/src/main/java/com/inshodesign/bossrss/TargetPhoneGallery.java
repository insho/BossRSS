package com.inshodesign.bossrss;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.inshodesign.bossrss.DB.InternalDB;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

/**
 * Taken straight from maros136 from StackOverflow's answer...
 * http://stackoverflow.com/questions/27729976/download-and-save-images-using-picasso
 */

public class TargetPhoneGallery implements Target {
//    private AddMediaURIListener mCallback;

    private final WeakReference<ContentResolver> resolver;
    private String title;
    private final int rowID;
    private Context context;


    public TargetPhoneGallery(ContentResolver r, int rowID, @Nullable String title, Context context)
    {
        this.resolver = new WeakReference<ContentResolver>(r);
        this.rowID = rowID;
        this.title = title;
        this.context = context;
//            this.addMediaURIListener = callback;
    }



    @Override
    public void onPrepareLoad (Drawable arg0)
    {
    }

    @Override
    public void onBitmapLoaded (Bitmap bitmap, Picasso.LoadedFrom arg1)
    {
        ContentResolver r = resolver.get();
        if (r != null)
        {
            if(title == null) {
                title = "BossRSSimage";
            }
            String uri = MediaStore.Images.Media.insertImage(r, bitmap, "img-" + rowID, title);

            InternalDB.getInstance(context).addMediaURItoDB(uri, rowID);
//                mCallback.addMediaURItoDB(uri, rowID);
        }
    }

    @Override
    public void onBitmapFailed (Drawable arg0)
    {
    }


}
