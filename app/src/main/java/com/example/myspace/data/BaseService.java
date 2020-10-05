package com.example.myspace.data;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BaseService extends Service {

    private final IBinder mBinder = new LocalBinder();

    DBHelper dbHelper;

    private static final String dbName = "my_space.db";

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
        cv.put("phone", content);

        long rowID = db.insert("contact", null, cv);
        Log.d(TAG, "row inserted, ID = " + rowID);

        dbHelper.close();
    }

    public void updateData(int noteId, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        Log.d(TAG, "--- Update mytable: ---");
        // подготовим значения для обновления
        cv.put("content", content);
        // обновляем по id
        int updCount = db.update("note", cv, "id = " + noteId, null);
        Log.d(TAG, "updated rows count = " + updCount);
        dbHelper.close();
    }

    public void deleteData(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int delCount = db.delete("note", "id = " + noteId, null);
        Log.d(TAG, "deleted rows count = " + delCount);

        dbHelper.close();
    }

    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("contact", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int contentColIndex = c.getColumnIndex("phone");

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

    public void exportDatabase() {
        Log.d(TAG, "start export..");
        try {
            File sd = Environment.getExternalStorageDirectory();
            Log.d(TAG, "exportDatabase: " + sd.toString());
            if (sd.canWrite()) {
                Log.d(TAG, "exportDatabase: 2");
                File currentDB = new File("/data/data/" + getPackageName() +"/databases/", dbName);
                File backupDB = new File(sd.toString() + "/Download/", dbName);

                if (currentDB.exists()) {
                    Log.d(TAG, "exportDatabase: 3");
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            } else {
                Log.d(TAG, "нет доступа к памяти телефона");
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void importDatabase() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            //Log.d(TAG, "exportDatabase: " + sd.toString());
            if (sd.canWrite()) {
                Log.d(TAG, "importDatabase: 2");
                File importedDB = new File(sd.toString() + "/Download/", dbName);
                File currentDB = new File("/data/data/" + getPackageName() +"/databases/", dbName);

                if(!currentDB.exists()) {
                    dbHelper.getWritableDatabase();
                }

                if (currentDB.exists()) {
                    Log.d(TAG, "importDatabase: 3");
                    FileChannel src = new FileInputStream(importedDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
