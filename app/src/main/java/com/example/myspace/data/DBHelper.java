package com.example.myspace.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MainActivity";

    public DBHelper(Context context) {
        super(context, "my_space.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table note ("
                + "id integer primary key autoincrement,"
                + "content text" + ");");
    }

    @Override
    public  void onOpen(SQLiteDatabase database) {
        super.onOpen((database));
        if(Build.VERSION.SDK_INT >= 28) {
            database.disableWriteAheadLogging();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}