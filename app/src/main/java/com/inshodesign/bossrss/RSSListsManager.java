package com.inshodesign.bossrss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joe on 3/3/2017.
 */



public class RSSListsManager extends InternalDB {

    public RSSListsManager(Context context) {
        super(context);
    }

    public void deleteFirstTableDataList(List<RSSList> rssLists) {
        for (RSSList rssData : rssLists)
            deleteRSSData(rssData);
    }

    public void deleteRSSData(RSSList item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, item.getId() + "=" + COL_ID, null);
        db.close();
    }
    /**this method retrieves all the records from table and returns them as list of
     FirstTableData types. Now you use this list to display detail on your screen as per your
     requirements.
     */
    public List<RSSList> getRSSLists() {
        List<RSSList> rssLists = new ArrayList<RSSList>();
        String refQuery = "Select * From " + TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(refQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    RSSList itemData = new RSSList();

                    itemData.setId(cursor.getInt(0));

                    itemData.setRowNumber(cursor.getInt(1));
                    itemData.setName(cursor.getString(2));
                    itemData.setImage(cursor.getBlob(3));
                    itemData.setURL(cursor.getString(4));

                    rssLists.add(itemData);
                } while (cursor.moveToNext());
            }

        } finally {
            db.close();
        }

//        Collections.sort(itemDataList);
        return rssLists;
    }

    public int  addFirstTableData(RSSList rssList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL1, rssList.getRowNumber());
        values.put(COL2, rssList.getName());
        values.put(COL3, rssList.getImage());
        values.put(COL4, rssList.getURL());

        long x=db.insert(TABLE, null, values);
        db.close();
        return (int)x;
    }

    public void updateItemDetailData(RSSList rssList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL1, rssList.getRowNumber());
        values.put(COL2, rssList.getName());
        values.put(COL3, rssList.getImage());
        values.put(COL4, rssList.getURL());

        db.update(TABLE, values, COL_ID + "=" + rssList.getId(), null);
        db.close();
    }

}