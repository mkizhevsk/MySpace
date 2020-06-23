package com.example.myspace.data;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BaseService extends Service {

    private final IBinder mBinder = new LocalBinder();

    DBHelper dbHelper;

    private static final String TAG = "MainActivity";

    public void onCreate() {
        super.onCreate();

        dbHelper = new DBHelper(this);
        Log.d(TAG, "BaseService onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "BaseService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BaseService onDestroy");
    }

    public class LocalBinder extends Binder {
        public BaseService getService() {
            return BaseService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void insertData(String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("content", content);

        long rowID = db.insert("note", null, cv);
        Log.d(TAG, "row inserted, ID = " + rowID);

        dbHelper.close();
    }

    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("note", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int contentColIndex = c.getColumnIndex("content");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(TAG, c.getInt(idColIndex) + " " + c.getString(contentColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();
        dbHelper.close();
    }
}
