package com.example.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.myspace.data.BaseService;
import com.example.myspace.data.entity.Contact;

public class PhonesActivity extends AppCompatActivity {

    BaseService baseService;

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

            Contact contact = new Contact();
            contact.setPhone("24-12-20");
            contact.setEmail("mmm@dd.ru");
            contact.setGroupId(1);
            baseService.insertContact(contact);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "PhonesActivity  onServiceDisconnected");
        }
    };

    public void addPhone(View view) {

    }
}