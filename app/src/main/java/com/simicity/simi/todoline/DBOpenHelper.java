package com.simicity.simi.todoline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by simi on 2/11/16.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "todo.db";
    static final int DB_VERSION = 1;
    public static final String TABLE_NAME_TODO = "todo";
    public static final String TABLE_NAME_LIST = "list";
    protected SQLiteDatabase db;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME_TODO + "("
                        + " _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + " task TEXT NOT NULL,"
                        + " memo TEXT,"
                        + " time INTEGER,"
                        + " done INTEGER NOT NULL,"
                        + " list_id INTEGER NOT NULL);"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_NAME_LIST + "("
                        + " _list_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + " list_name TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TODO + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LIST + ";");
        onCreate(db);
    }
}
