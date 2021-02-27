package com.example.myspace.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MainActivity";

    public DBHelper(Context context) {
        super(context, "my_space.db", null, 1);
    }

    private static final String TABLE_CONTACT =
            "create table if not exists contact ("
                    + "id integer primary key autoincrement, "
                    + "name text, "
                    + "phone text, "
                    + "email text, "
                    + "group_id integer" + ");";

    private static final String TABLE_NOTE =
            "create table if not exists note ("
                    + "id integer primary key autoincrement, "
                    + "date text, "
                    + "content text" + ");";

    private static final String TABLE_CARD =
            "create table if not exists card ("
                    + "id integer primary key autoincrement, "
                    + "date text, "
                    + "front text, "
                    + "back text, "
                    + "example text, "
                    + "status integer" + ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "--- onCreate database ---");

        // создаем таблицу, если ее нет
        db.execSQL(TABLE_CONTACT);
        db.execSQL(TABLE_NOTE);
        db.execSQL(TABLE_CARD);
        Log.d(TAG, "--- onCreate database finish ---");
    }

    @Override
    public  void onOpen(SQLiteDatabase database) {
        Log.d(TAG, "--- onOpen database ---");
//        database.execSQL(TABLE_CARD);

        super.onOpen((database));
        if(Build.VERSION.SDK_INT >= 28) {
            database.disableWriteAheadLogging();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}