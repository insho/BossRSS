package com.inshodesign.bossrss.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.inshodesign.bossrss.XMLModel.RSSList;

import java.util.ArrayList;
import java.util.List;

//import com.jukuproject.juku.CharacterLists;


public class InternalDB extends SQLiteOpenHelper {

//    private static boolean debug = false;
    private static String TAG = "InternalDB";
    private static InternalDB sInstance;

    public static  String DB_NAME =  "JQuiz";
    public static String DATABASE_NAME = DB_NAME + ".db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_MAIN = "EndpointURL";
    public static final String TABLE_SUB = "RSSData";
    public static final String COL_ID = "_id";
    public static final String COL_T1_TITLE = "Title";


    public static final String COL_FOREIGNKEY = "FK_Endpoint_RSSData";
    public static final String COL0 = "URL";
    public static final String COL1 = "Name";
    public static final String COL2 = "Image";


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
                                "%s INTEGER AUTOINCREMENT PRIMARY KEY, " +
                                "%s TEXT, " +
                                "%s TEXT)", TABLE_MAIN,
                        COL_ID,
                        COL0,
                        COL_T1_TITLE);

        sqlDB.execSQL(sqlQueryMain);

        String sqlQuerySub =
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "%s INTEGER AUTOINCREMENT PRIMARY KEY, " +
                                "%s INTEGER,  " +
                                "%s TEXT, " +
                                "%s BLOB)", TABLE_SUB,
                        COL_ID,
                        COL_FOREIGNKEY,
                        COL1,
                        COL2);

        sqlDB.execSQL(sqlQuerySub);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        onCreate(sqlDB);
    }



    public int  saveEndPointURL(RSSList rssList) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL0, rssList.getURL());
            values.put(COL_T1_TITLE, rssList.getTitle());

        long x=db.insert(TABLE_MAIN, null, values);
            db.close();
            Log.d(TAG,"insert x value:" + x);
            return (int)x;
    }


    public List<RSSList> getRSSLists() {
        List<RSSList> rssLists = new ArrayList<RSSList>();
        String refQuery = "Select * From " + TABLE_MAIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(refQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    RSSList itemData = new RSSList();

                    itemData.setId(cursor.getInt(0));
                    itemData.setTitle(cursor.getString(1));
                    itemData.setImage(cursor.getBlob(2));
                    itemData.setURL(cursor.getString(3));

                    rssLists.add(itemData);
                } while (cursor.moveToNext());
            }

        } finally {
            db.close();
        }

//        Collections.sort(itemDataList);
        return rssLists;
    }

}