package com.simicity.simi.todoline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by simi on 2/11/16.
 */
public class TodoAdapter {
    private SQLiteDatabase db;
    static private DBOpenHelper helper;

    public TodoAdapter(Context context) {
        helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public Cursor getAllList() {
        return db.query(DBOpenHelper.TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getList(int id) {
        String[] cols = {"task", "memo", "time", "done"};
        String where = "_id = ?";
        String[] param = {String.valueOf(id)};
        return db.query(DBOpenHelper.TABLE_NAME, cols, where, param, null, null, null);
    }

    public void insert(String task, String memo, int time) {
        ContentValues values = new ContentValues();
        values.put("task", task);
        values.put("memo", memo);
        values.put("time", time);
        values.put("done", 0);
        db.insertOrThrow(DBOpenHelper.TABLE_NAME, null, values);
    }

    public void update(int id, String task, String memo, int time, int done) {
        ContentValues values = new ContentValues();
        values.put("task", task);
        values.put("memo", memo);
        values.put("time", time);
        values.put("done", done);
        db.update(DBOpenHelper.TABLE_NAME, values, "_id = " + id, null);
    }

    public void delete(int id) {
        db.delete(DBOpenHelper.TABLE_NAME, "_id = " + id, null);
    }

    public void selectivedelete() {
        db.delete(DBOpenHelper.TABLE_NAME, "done = " + 1, null);
    }

    public void deleteAll() {
        db.delete(DBOpenHelper.TABLE_NAME, null, null);
    }
}
