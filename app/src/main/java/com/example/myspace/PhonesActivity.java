package com.example.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
import java.util.List;

public class PhonesActivity extends AppCompatActivity {

    BaseService baseService;

//    List<Contact> contactList;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phones);

        Intent intent = new Intent(this, BaseService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BaseService.LocalBinder binder = (BaseService.LocalBinder) service;
            baseService = binder.getService();

            Log.d(TAG, "PhonesActivity  onServiceConnected");

            List<Contact> contactList = baseService.getContacts();
            Log.d(TAG, "contactList: " + contactList.size());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "PhonesActivity  onServiceDisconnected");
        }
    };

    // top right menu
    public  boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "insert");
        menu.add(0, 2, 0, "update");
        menu.add(0, 3, 0, "delete");
        menu.add(0, 4, 0, "read");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Contact newContact = new Contact();
                newContact.setPhone("24-12-19");
                newContact.setEmail("mmm@dd.ru");
                newContact.setGroupId(1);
                baseService.insertContact(newContact);

                break;
            case 2:
                Contact updatedContact = new Contact();
                updatedContact.setId(2);
                updatedContact.setPhone("24-12-20");
                updatedContact.setEmail("mmm@dd.ru");
                updatedContact.setGroupId(1);

                baseService.updateContact(updatedContact);
                break;
            case 3:
                baseService.deleteContact(1);
                break;
            case 4:
                List<Contact> contactList = baseService.getContacts();
                for(Contact contact : contactList) {
                    Log.d(TAG, contact.toString());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addPhone(View view) {
        Contact contact = new Contact();
        contact.setPhone("24-12-20");
        contact.setEmail("mmm@dd.ru");
        contact.setGroupId(1);
        baseService.insertContact(contact);
    }
}