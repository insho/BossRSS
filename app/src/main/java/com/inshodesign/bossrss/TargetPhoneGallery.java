package com.inshodesign.bossrss;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

import com.inshodesign.bossrss.DB.InternalDB;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

/**
 * Taken straight from maros136 from StackOverflow's answer...
 * http://stackoverflow.com/questions/27729976/download-and-save-images-using-picasso
 */

public class TargetPhoneGallery implements Target {
    addMediaURIListener mCallback;

        private final WeakReference<ContentResolver> resolver;
        private final String title;
        private final int rowID;

        public TargetPhoneGallery(ContentResolver r, int rowID, String title)
        {
            this.resolver = new WeakReference<ContentResolver>(r);
            this.rowID = rowID;
            this.title = title;
        }

    public interface addMediaURIListener {
        void addMediaURItoDB(String URI, int rowID);
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
               String uri = MediaStore.Images.Media.insertImage(r, bitmap, "img-" + rowID, title);
                //TODO == put this in a listener callback thing?
                mCallback.addMediaURItoDB(uri, rowID);
            }
        }

        @Override
        public void onBitmapFailed (Drawable arg0)
        {
        }
    }
