package com.inshodesign.bossrss;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//import com.jukuproject.juku.CharacterLists;

import java.util.ArrayList;

public class InternalDB extends SQLiteOpenHelper {

    private static boolean debug = false;

    private static InternalDB sInstance;

    public static  String DB_NAME =  "JQuiz";
    public static String DATABASE_NAME = DB_NAME + ".db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "RSSLists";
    public static final String COL_ID = "_id";
    public static final String COL1 = "Name";
    public static final String COL2 = "Image";
    public static final String COL3 = "URL";

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
        String sqlQuery =
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "%s INTEGER AUTOINCREMENT PRIMARY KEY, " +
                                "%s TEXT, " +
                                "%s BLOB, " +
                                "%s TEXT)", TABLE,
                        COL_ID,
                        COL1,
                        COL2,
                        COL3);

        sqlDB.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        onCreate(sqlDB);
    }

}