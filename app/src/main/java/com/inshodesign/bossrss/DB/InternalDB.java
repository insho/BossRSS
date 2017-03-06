package com.inshodesign.bossrss.DB;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import com.inshodesign.bossrss.XMLModel.RSSList;

import java.util.ArrayList;
import java.util.List;


public class InternalDB extends SQLiteOpenHelper {

    private static boolean debug = true;
    private static String TAG = "TEST -- Internal";
    private static InternalDB sInstance;

    public static  String DB_NAME =  "JQuiz";
    public static String DATABASE_NAME = DB_NAME + ".db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_MAIN = "EndpointURL";
    public static final String COL_ID = "_id";

    public static final String COL0 = "URL";
    public static final String COL1 = "Title";
    public static final String COL2 = "ImageURL";
    public static final String COL3 = "ImageURI";



    public static synchronized InternalDB getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new InternalDB(context.getApplicationContext());
        }
        return sInstance;
    }


    public InternalDB(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQueryMain =
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT, " +
                                "%s TEXT, " +
                                "%s TEXT, " +
                                "%s TEXT)", TABLE_MAIN,
                        COL_ID,
                        COL0,
                        COL1,
                        COL2,
                        COL3);

        sqlDB.execSQL(sqlQueryMain);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        onCreate(sqlDB);
    }



    public boolean duplicateFeed(String feedURL) {

        /** Before inserting record, check to see if feed already exists */
        SQLiteDatabase db = this.getWritableDatabase();
        String queryRecordExists = "Select _id From " + TABLE_MAIN + " where " + COL0 + " = ?" ;
        Cursor c = db.rawQuery(queryRecordExists, new String[]{feedURL});
        if (c.moveToFirst()) {
            //A record already exists, so return -2
            return true;
        }

        c.close();

        return false;
    }

    public int saveFeedURL(@NonNull String url) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(debug) {
            Log.d(TAG,"saving initial url: " + url );
        }

        values.put(COL0, url.trim());
        long x=db.insert(TABLE_MAIN, null, values);
        db.close();
        if(debug) {
            Log.d(TAG,"save inital URL return value:" + x);
        }
        return (int)x;
    }


    public void addTitleandImageURLtoDB(String URL, String title, String imageURL) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL1, title);
        values.put(COL2, imageURL);
        db.update(TABLE_MAIN, values, COL0 + "= ?", new String[] {URL});
        db.close();
//        Log.d(TAG,"SUCESSFUL INSERT TITLE AND IMAGE URL FOR: " + title + ", " + imageURL);

    }


    public Boolean existingRSSListValues() {

        String querySelectAll = "Select _id, URL, Title, ImageURL, ImageURI From " + TABLE_MAIN  + " WHERE URL not NULL and Title not NULL and ImageURL not null";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querySelectAll, null);

        try {
            if (c.moveToFirst()) {
                c.close();
                return true;
            }
            c.close();

        } finally {
            db.close();
        }
        return false;
    }



    public int getRowIDforURL(String URL) {
        SQLiteDatabase db = this.getWritableDatabase();

        /** Before inserting record, check to see if feed already exists */
        String queryRecordExists = "Select _id From " + TABLE_MAIN + " where url = ?" ;
        Cursor c = db.rawQuery(queryRecordExists, new String[]{URL.trim()});
        int result;
        if (c.moveToFirst()) {
            //A record already exists, so return -2
            result = c.getInt(0);
        } else {
            result = -1;
        }

        c.close();
        return result;
    }

    public void addMediaURItoDB(String URI, int rowID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL3, URI);
        db.update(TABLE_MAIN, values, COL_ID + "= ?", new String[] {String.valueOf(rowID)});
        db.close();
        Log.d(TAG,"SUCESSFUL INSERT URI value at row: " + rowID);

    }

    /** This pulls the RSSList dataset for the mainfragment recycler **/
    /** IT is also chained to the attachImagestoRSSLists which matches downloaded thumbnails to the RSSList rows **/
    public List<RSSList> getRSSLists(Context context) {
        List<RSSList> rssLists = new ArrayList<RSSList>();
        String querySelectAll = "Select _id, URL, Title, ImageURL, ImageURI From " + TABLE_MAIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querySelectAll, null);


        try {
            if (c.moveToFirst()) {
                do {
                    RSSList itemData = new RSSList();
                    if(debug) {
                        Log.d(TAG, "putting url: " + c.getInt(0));
                        Log.d(TAG, "putting title: " + c.getString(1));
                        Log.d(TAG, "putting imageURL: " + c.getString(2));
                        Log.d(TAG, "putting imageURI: " + c.getString(3));
                    }
                    itemData.setId(c.getInt(0));
                    itemData.setURL(c.getString(1));
                    itemData.setTitle(c.getString(2));
                    itemData.setImageURL(c.getString(3));
                    itemData.setImageURI(c.getString(4));
                    rssLists.add(itemData);

                } while (c.moveToNext());
            }

            c.close();
        } finally {
            db.close();
        }

        //Now look for and attach images to the RSS LIST
        return attachImagestoRSSLists(rssLists,context);
    }

    private List<RSSList> attachImagestoRSSLists(List<RSSList> rssLists, Context context) {

        for (RSSList rssList : rssLists) {
            if(rssList.getImageURI() != null) {
                long selectedImageUri = ContentUris.parseId(Uri.parse(rssList.getImageURI()));
                Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(
                        context.getContentResolver(), selectedImageUri,MediaStore.Images.Thumbnails.MICRO_KIND,
                        null );
                rssList.setBitmap(bm);
            }
        }

        return rssLists;
    }


    public boolean deletedRSSFeed(int removeid) {
        if(removeid >= 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_MAIN, COL_ID + "= ?", new String[]{String.valueOf(removeid)});
            db.close();
            return true;
        }
        return false;
    };
}