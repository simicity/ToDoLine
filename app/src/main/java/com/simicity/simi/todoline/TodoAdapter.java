package com.simicity.simi.todoline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public Cursor getAllList(int list_id) {
        String[] cols = {"task", "memo", "time", "done", "list_id"};
        String where = "list_id = ?";
        String[] param = {String.valueOf(list_id)};
        return db.query(DBOpenHelper.TABLE_NAME_TODO, cols, where, param, null, null, null);
    }

    public Cursor getList(int id) {
        String[] cols = {"task", "memo", "time", "done", "list_id"};
        String where = "_id = ?";
        String[] param = {String.valueOf(id)};
        return db.query(DBOpenHelper.TABLE_NAME_TODO, cols, where, param, null, null, null);
    }

    public void insert(String task, String memo, int time, int list_id) {
        ContentValues values = new ContentValues();
        values.put("task", task);
        values.put("memo", memo);
        values.put("time", time);
        values.put("done", 0);
        values.put("list_id", list_id);
        db.insertOrThrow(DBOpenHelper.TABLE_NAME_TODO, null, values);
    }

    public void update(int id, String task, String memo, int time, int done, int list_id) {
        ContentValues values = new ContentValues();
        values.put("task", task);
        values.put("memo", memo);
        values.put("time", time);
        values.put("done", done);
        values.put("list_id", list_id);
        db.update(DBOpenHelper.TABLE_NAME_TODO, values, "_id = " + id, null);
    }

    public void delete(int id) {
        db.delete(DBOpenHelper.TABLE_NAME_TODO, "_id = " + id, null);
    }

    public void selectivedelete() {
        db.delete(DBOpenHelper.TABLE_NAME_TODO, "done = " + 1, null);
    }

    public void selectivedeleteList(int list_id) {
        db.delete(DBOpenHelper.TABLE_NAME_TODO, "list_id = " + list_id, null);
    }

    public void deleteAll() {
        db.delete(DBOpenHelper.TABLE_NAME_TODO, null, null);
    }
}
