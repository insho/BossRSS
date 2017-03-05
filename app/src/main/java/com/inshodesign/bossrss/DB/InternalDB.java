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
import android.util.Log;

import com.inshodesign.bossrss.TargetPhoneGallery;
import com.inshodesign.bossrss.XMLModel.RSSList;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Pack200;

//import com.jukuproject.juku.CharacterLists;


public class InternalDB extends SQLiteOpenHelper {

//    private static boolean debug = false;
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
        long x=db.insert(TABLE_MAIN, null, values);
        db.close();
        Log.d(TAG,"insert URI value:" + x);

    }

    public int  saveEndPointURL(RSSList rssList) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

        /** Before inserting record, check to see if feed already exists */
        String queryRecordExists = "Select _id From " + TABLE_MAIN + " where url = ?" ;
        Cursor c = db.rawQuery(queryRecordExists, new String[]{rssList.getURL().trim()});
            if (c.moveToFirst()) {
                //A record already exists, so return -2
                return -2;
            }

            c.close();



        Log.d(TAG,"putting url: " + rssList.getURL() );
        Log.d(TAG,"putting title: " +rssList.getTitle() );

            values.put(COL0, rssList.getURL());

//        /** If the device is offline, or otherwise fails to pull data, just save the URL only */
            values.put(COL1, rssList.getTitle());

        long x=db.insert(TABLE_MAIN, null, values);
            db.close();
            Log.d(TAG,"insert x value:" + x);
            return (int)x;
    }


    public List<RSSList> getRSSLists(Context context) {
        List<RSSList> rssLists = new ArrayList<RSSList>();
        String querySelectAll = "Select _id, URL, Title, ImageURL, ImageURI From " + TABLE_MAIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querySelectAll, null);


        try {
            if (c.moveToFirst()) {
                do {
                    RSSList itemData = new RSSList();

                    Log.d(TAG,"putting url: " + c.getInt(0) );
                    Log.d(TAG,"putting title: " + c.getString(1) );
                    Log.d(TAG,"putting imageURL: " + c.getString(2) );
                    Log.d(TAG,"putting imageURI: " + c.getString(3) );

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

        Log.d(TAG,"HEEEEERE");

        return attachImagestoRSSLists(rssLists,context);
    }

    private List<RSSList> attachImagestoRSSLists(List<RSSList> rssLists, Context context) {

        Log.d("TEST -- INTERNAL", "RSSLIST SIZE: " + rssLists.size());
        for (RSSList rssList : rssLists) {
            if(rssList.getImageURI() != null) {
                long selectedImageUri = ContentUris.parseId(Uri.parse(rssList.getImageURI()));
                Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(
                        context.getContentResolver(), selectedImageUri,MediaStore.Images.Thumbnails.MICRO_KIND,
                        null );
                rssList.setImage(bm);
            }

        }

        Log.d("TEST -- INTERNAL", "RSSLIST SIZE2: " + rssLists.size());

        return rssLists;
    }


    public boolean deletedRSSFeed(int removeid) {
        if(removeid >= 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_MAIN, removeid + "=" + COL_ID, null);
            db.close();
            return true;
        }
        return false;
    };
}