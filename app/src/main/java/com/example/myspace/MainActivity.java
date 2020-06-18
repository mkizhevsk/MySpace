package com.example.myspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.myspace.data.DBHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    private static final String TAG = "MainActivity";
    private static final String dbName = "my_space.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if(checkPermissions()) {

            dbHelper = new DBHelper(this);

            insertData("Hello World3!");
            //updateData(1, "first row");
            //deleteData(6);
            readData();

            //exportDatabase();
            //importDatabase();
        //}
    }

    private void insertData(String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("content", content);

        long rowID = db.insert("note", null, cv);
        Log.d(TAG, "row inserted, ID = " + rowID);

        dbHelper.close();
    }

    private void deleteData(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int delCount = db.delete("note", "id = " + noteId, null);
        Log.d(TAG, "deleted rows count = " + delCount);

        dbHelper.close();
    }

    private void readData() {
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

    private void updateData(int noteId, String content) {
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

    public void exportDatabase() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            //Log.d(TAG, "exportDatabase: " + sd.toString());
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

    public boolean checkPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
}
