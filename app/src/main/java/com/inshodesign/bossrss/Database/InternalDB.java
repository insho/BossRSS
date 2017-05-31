package com.inshodesign.bossrss.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.inshodesign.bossrss.BuildConfig;
import com.inshodesign.bossrss.Fragments.RSSListFragment;
import com.inshodesign.bossrss.Models.RSSList;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SQlite helper singleton. Handles all db related calls as well
 * as saving/deleting saved RSS List icons
 */
public class InternalDB extends SQLiteOpenHelper {

    private static String TAG = "TEST -- Internal";
    private static InternalDB sInstance;

    public static  String DB_NAME =  "JQuiz";
    public static String DATABASE_NAME = DB_NAME + ".db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_MAIN = "RSSFeeds";
    public static final String COL_ID = "_id";
    public static final String COL0_URL = "URL";
    public static final String COL1_TITLE = "Title";
    public static final String COL2_IMAGE_URL = "ImageURL";
    public static final String COL3_IMAGE_FILE_PATH = "ImageURI";



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
                        COL0_URL,
                        COL1_TITLE,
                        COL2_IMAGE_URL,
                        COL3_IMAGE_FILE_PATH);

        sqlDB.execSQL(sqlQueryMain);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        onCreate(sqlDB);
    }


    /**
     * Saves a feed url to the db
     * @param url RSS feed url
     * @return bool true if save was successful, false if not
     */
    public boolean saveFeedURL(@NonNull String url) {

        if(BuildConfig.DEBUG) {Log.d(TAG,"saving initial url: " + url );}
        try {
            ContentValues values = new ContentValues();
            values.put(COL0_URL, url.trim());
            sInstance.getWritableDatabase().insertWithOnConflict(TABLE_MAIN, null, values,SQLiteDatabase.CONFLICT_REPLACE);
            return true;
        } catch (SQLiteException e) {
            Log.e(TAG,"InternalDB saveFeedURL sqlite exception: " + e.getCause());
            return false;
        }
    }


    /**
     * Pulls a list of saved RSS feeds from the RSSFeeds table in the database
     * @return array of RSSList objects for the saved feeds in the db
     *
     * @see RSSListFragment
     */
    public List<RSSList> getRSSLists() {

        List<RSSList> rssLists = new ArrayList<RSSList>();
        String querySelectAll = "Select URL, Title, ImageURL, ImageURI From " + TABLE_MAIN;
        Cursor c = sInstance.getReadableDatabase().rawQuery(querySelectAll, null);

        try {
            if (c.moveToFirst()) {
                do {
                    RSSList itemData = new RSSList();
                    if(BuildConfig.DEBUG) {
                        Log.d(TAG, "putting url: " + c.getString(0));
                        Log.d(TAG, "putting title: " + c.getString(1));
                        Log.d(TAG, "putting imageURL: " + c.getString(2));
                        Log.d(TAG, "putting imageURI: " + c.getString(3));
                    }
                    itemData.setURL(c.getString(0));
                    itemData.setTitle(c.getString(1));
                    itemData.setImageURL(c.getString(2));
                    itemData.setImageURI(c.getString(3));
                    rssLists.add(itemData);

                } while (c.moveToNext());
            }

            c.close();
        }  catch (SQLiteException e) {
            Log.e(TAG,"Internal DB getRSSLists sqlite exception: " + e.getCause());
        }
        return rssLists;
    }

    /**
     * Removes an RSS feed from the database, and deletes its icon from the icons folder
     * @param removeUrl url of row to delete (pkey in db)
     * @return bool true if delete was successful, false if error occurred
     */
    public boolean deletedRSSFeed(Context context, String removeUrl) {
        try {
            //Firstly delete icon (icon file name is the row id # in the db, so icon delete must occur before row is removed)
            final int savedImgTitle = getRowIDforURL(removeUrl);
            if(savedImgTitle>=0) {
                ContextWrapper cw = new ContextWrapper(context);
                File directory = cw.getDir("icons", Context.MODE_PRIVATE);

                for (File file : directory.listFiles()) {
                    Uri uri = Uri.fromFile(file);
                    if(BuildConfig.DEBUG){Log.i(TAG,"FILE: " + uri);}
                    if(String.valueOf(savedImgTitle).equals(uri.toString())) {
                        if(BuildConfig.DEBUG){Log.i(TAG,"FILE DELETE!");}
                        file.delete();
                    }
                }
            }
            sInstance.getWritableDatabase().delete(TABLE_MAIN, COL0_URL + "= ?", new String[]{removeUrl});
            return true;
        } catch (SQLiteException e) {
            Log.e(TAG,"InternalDB deleteRSSFeed sqlite error " + e.getCause());
        }
        return false;
    };


    /**
     * Simple check to see if an RSS feeds URL is saved into the database. Necessary because
     * the RSS URL is saved to the db first when a new feed is added, and other rows for that feed
     * are added later in an update statement, so it is necessary to know if the URL actually exists in the db
     * @param URL url string for the RSS Feed. Essentially the primary key
     * @param onlyCheckForURL bool true if only checking to see if the url (pkey) exists. False if
     *                        checking to see that all items for that row have been inserted
     *
     * @return bool true if url exists in db, false if not
     *
     * @see #addTitleandImageURLtoDB(String, String, String)
     * @see #addIconImageFilePathURItoDB(String, String)
     */
    public boolean rssDataExistsInDB(String URL, Boolean onlyCheckForURL) {
        String queryRecordExists;

        if(onlyCheckForURL) {
            queryRecordExists = "Select URL From " + TABLE_MAIN + " WHERE " +  COL0_URL + " = ?" ;
        } else {
            queryRecordExists = "Select URL From " + TABLE_MAIN  + " WHERE " + COL0_URL + " = ? and Title not NULL and ImageURL not null and ImageURI not null";
        }
        Cursor c = sInstance.getReadableDatabase().rawQuery(queryRecordExists, new String[]{URL.trim()});
        boolean recordExists = (c.getCount()>0);
        c.close();
        return recordExists;
    }


    /**
     * Updates database row for a saved RSS feed URL with additional info
     * for feed Title and feed icon image url.
     *
     * @param url RSS feed url -- essentially the primary key for an RSS feed in the db
     * @param title title to add to the db
     * @param imageURL rss icon image url to add to the db
     *
     * @see com.inshodesign.bossrss.MainActivity#getRSSFeed(String)
     */
    public void addTitleandImageURLtoDB(String url, String title, String imageURL) {

        if(rssDataExistsInDB(url,true)) {
            ContentValues values = new ContentValues();
            values.put(COL1_TITLE, title);
            values.put(COL2_IMAGE_URL, imageURL);
            sInstance.getWritableDatabase().update(TABLE_MAIN, values, COL0_URL + "= ?", new String[] {url});
            if(BuildConfig.DEBUG){Log.i(TAG,"SUCESSFUL INSERT TITLE AND IMAGE URL FOR: " + title + ", " + imageURL);}
        }
    }



    /**
     * Inserts the URI link to the location in the phone of a saved RSS feed's icon image
     * @param URI URI of saved image
     * @param imageTitle title of saved image (row number of rss item in db)
     */
    private void addIconImageFilePathURItoDB(String URI, String imageTitle) {
        if(BuildConfig.DEBUG){Log.d(TAG,"URI VALUE: " + imageTitle + " - " + URI);}
        ContentValues values = new ContentValues();
        values.put(COL3_IMAGE_FILE_PATH, URI);
        sInstance.getWritableDatabase().update(TABLE_MAIN, values, COL_ID + "= ?", new String[] {imageTitle});
    }


    /**
     * Takes the url of an icon image from a saved RSS list, downloads the image with picasso
     * and saves it to a file on the phone
     *
     * Note: Using the row number of the item as title of saved img  {@link #getRowIDforURL(String)}
     *
     * @param rssList RSSList object for list icon that is being downloaded. RSSList URL is used
     *                to determine the row number of the RSSList in the db. RSSLIst Image URL is the one
     *                downloaded by picasso and saved
     */
    public void downloadRSSListIcon(final Context context, final RSSList rssList) {
        final int savedImgTitle = getRowIDforURL(rssList.getURL());
        if(savedImgTitle>=0 && rssList.getImageURL() != null) {
            Picasso.with(context).load(rssList.getImageURL()).into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                File file = checkForImagePath(context, String.valueOf(savedImgTitle));
                                if(!file.exists()) {
                                    FileOutputStream ostream = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                                    ostream.flush();
                                    ostream.close();
                                }
                                Uri uri = Uri.fromFile(file);
                                addIconImageFilePathURItoDB(uri.toString(),String.valueOf(savedImgTitle));
                            } catch (IOException e) {
                                Log.e(TAG, "IOException" + e.getLocalizedMessage());
                            } catch (NullPointerException n) {
                                Log.e(TAG,"Nullpointer exception on downloadRSSListIcon " +n.getCause());
                            }
                        }
                    }).start();

                }
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        } else {
            Log.e(TAG,"Cant get row number for img: " + rssList.getURL() + " /" + rssList.getImageURL());
        }

    }

    /**
     * Gets _id value for a saved RSS list, given the url for that list. _id is used
     * as the "name" of the saved icon files (since urls cannot be used as file names), so
     * to save/delete rss list icons, it is necessary to get the rowid first
     * @param url url of saved RSS list
     * @return row _id for a given url
     *
     * @see #deletedRSSFeed(Context, String)
     * @see #downloadRSSListIcon(Context, RSSList)
     */
    private int getRowIDforURL(String url) {

        String queryRecordExists = "Select _id From " + TABLE_MAIN + " where " + COL0_URL + " = ?" ;
        Cursor c = sInstance.getReadableDatabase().rawQuery(queryRecordExists, new String[]{url.trim()});
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


    /**
     * Checks whether directory for saving twitter user icons already exist. If not, it creates the directory
     * @param userId title of image icon
     * @return file for image icon (to then be saved)
     */
    private File checkForImagePath(Context context, String userId) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("icons", Context.MODE_PRIVATE);

        if (!directory.exists()) {
            directory.mkdir();
        }
        if(BuildConfig.DEBUG){Log.d(TAG,"URI directory: " + directory.getAbsolutePath() + ", FILE: " + userId +".png" );}
        return new File(directory, userId + ".png");
    }




}