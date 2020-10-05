package com.example.myspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myspace.data.BaseService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    DBHelper dbHelper;
//    private static final String dbName = "my_space.db";

    BaseService baseService;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermissions()) startApp();
    }

    protected void startApp() {
        Log.d(TAG, "start app");

        Intent intent = new Intent(this, BaseService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BaseService.LocalBinder binder = (BaseService.LocalBinder) service;
            baseService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    // top right menu
    public  boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "insert");
        menu.add(0, 2, 0, "update");
        menu.add(0, 3, 0, "delete");
        menu.add(0, 4, 0, "read db");
        menu.add(0, 5, 0, "export");
        menu.add(0, 6, 0, "import");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                baseService.insertData("2-12-85-02");
                break;
            case 2:
                baseService.updateData(1, "hello");
                break;
            case 3:
                baseService.deleteData(1);
                break;
            case 4:
                baseService.readData();
                break;
            case 5:
                baseService.exportDatabase();
                break;
            case 6:
                baseService.importDatabase();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void insertData(String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("content", content);

        long rowID = db.insert("note", null, cv);
        Log.d(TAG, "row inserted, ID = " + rowID);

        dbHelper.close();
    }*/

    /*private void deleteData(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int delCount = db.delete("note", "id = " + noteId, null);
        Log.d(TAG, "deleted rows count = " + delCount);

        dbHelper.close();
    }*/

    /*private void readData() {
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
    }*/

    /*private void updateData(int noteId, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        Log.d(TAG, "--- Update mytable: ---");
        // подготовим значения для обновления
        cv.put("content", content);
        // обновляем по id
        int updCount = db.update("note", cv, "id = " + noteId, null);
        Log.d(TAG, "updated rows count = " + updCount);
        dbHelper.close();
    }*/

   /*public void exportDatabase() {
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
    }*/

    /*public void importDatabase() {
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
    }*/

    public void goToDiary(View view) {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    public void goToPhones(View view) {
        Intent intent = new Intent(this, PhonesActivity.class);
        startActivity(intent);
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

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        Log.d(TAG, " разрешений: " + grantResults.length);
        if(grantResults.length > 1) startApp();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BaseService.class));

        super.onDestroy();
    }

}
