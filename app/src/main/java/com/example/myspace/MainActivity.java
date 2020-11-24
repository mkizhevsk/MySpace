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
import com.example.myspace.data.entity.Contact;
import com.example.myspace.data.entity.Note;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BaseService baseService;

    public static List<Note> notes;

    public static List<Contact> contacts;

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

            Log.d(TAG, "MainActivity onServiceConnected");

            notes = baseService.getNotes();
            Log.d(TAG, "" + notes.size());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d(TAG, "MainActivity onServiceDisconnected");
        }
    };

    protected void onResume(Bundle savedInstanceState) {
        Log.d(TAG, "onResume");
    }

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
                Contact contact = new Contact();
                contact.setPhone("24-12-19");
                contact.setEmail("mmm@dd.ru");
                contact.setGroupId(1);
                baseService.insertContact(contact);

                Note note = new Note();
                note.setDate(LocalDate.now());
                note.setContent("Важная запись");
                baseService.insertNote(note);
                break;
            case 2:

//                baseService.updateData(1, "hello");
                break;
            case 3:
//                baseService.deleteData(1);
                break;
            case 4:
                baseService.getContacts();
                baseService.readNotes();
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

    public void goToDiary(View view) {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    public void goToPhones(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();



        Log.d(TAG, "onResume");
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
