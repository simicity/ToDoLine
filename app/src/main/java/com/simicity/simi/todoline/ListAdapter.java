package com.simicity.simi.todoline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by simi on 4/28/16.
 */
public class ListAdapter {
    private SQLiteDatabase db;
    static private DBOpenHelper helper;

    public ListAdapter(Context context) {
        helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public Cursor getAllList() {
        return db.query(DBOpenHelper.TABLE_NAME_TODO, null, null, null, null, null, null);
    }

    public Cursor getAllListList() {
        return db.query(DBOpenHelper.TABLE_NAME_LIST, null, null, null, null, null, null);
    }

    public Cursor getListList(int list_id) {
        String[] cols = {"_list_id", "list_name"};
        String where = "_list_id = ?";
        String[] param = {String.valueOf(list_id)};
        return db.query(DBOpenHelper.TABLE_NAME_LIST, cols, where, param, null, null, null);
    }

    public void insertList(String list_name) {
        ContentValues values = new ContentValues();
        values.put("list_name", list_name);
        db.insertOrThrow(DBOpenHelper.TABLE_NAME_LIST, null, values);
    }

    public void updateList(int id, String list_name ) {
        ContentValues values = new ContentValues();
        values.put("list_name", list_name);
        db.update(DBOpenHelper.TABLE_NAME_LIST, values, "_list_id = " + id, null);
    }

    public void deleteList(int id) {
        db.delete(DBOpenHelper.TABLE_NAME_LIST, "_list_id = " + id, null);
    }

    public void deleteAllList() {
        db.delete(DBOpenHelper.TABLE_NAME_LIST, null, null);
    }
}
