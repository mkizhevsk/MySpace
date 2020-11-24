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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myspace.data.BaseService;
import com.example.myspace.data.ContactAdapter;
import com.example.myspace.data.entity.Contact;

import java.util.List;

public class ContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    BaseService baseService;

    private static final String TAG = "MainActivity";

//    private int groupId = -1;

    ListView lvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent intent = new Intent(this, BaseService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        Spinner spinner = (Spinner) findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.contact_groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        lvMain = findViewById(R.id.list_items);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

//        String[] stringPhones = {"212-85-02", "24-12-19", "58-63-91"};

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BaseService.LocalBinder binder = (BaseService.LocalBinder) service;
            baseService = binder.getService();

            Log.d(TAG, "PhonesActivity  onServiceConnected");

//            showListView(0);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "PhonesActivity  onServiceDisconnected");

        }
    };

    private void showListView(int groupId) {
        List<Contact> contacts;
        if(groupId == 0) {
            contacts = baseService.getContacts();
        } else {
            contacts = baseService.findContactsByGroup(groupId);
        }

        final ContactAdapter phonesAdapter = new ContactAdapter(this, 100, contacts);
        lvMain.setAdapter(phonesAdapter);

        // выбор контакта
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "itemClick: position = " + position + ", id = " + id + " : " + view.getId());
                view.setSelected(true);

                Intent intent = new Intent(ContactActivity.this, ContactFormActivity.class);
                intent.putExtra("name", "Ivanov");
                startActivity(intent);
            }
        });
    }

    // выбор группы
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "Выбор группы: position " + position + ", id " + id);

        showListView(position);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected");
    }

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
                newContact.setName("Иванов И.И.");
                newContact.setPhone("212-85-02");
                newContact.setEmail("ivanov@dd.ru");
                newContact.setGroupId(2);
                baseService.insertContact(newContact);

                showListView(newContact.getGroupId());

                break;
            case 2:
                Contact updatedContact = new Contact();
                updatedContact.setId(2);
                updatedContact.setName("Васечкин");
                updatedContact.setPhone("24-12-20");
                updatedContact.setEmail("mmm@dd.ru");
                updatedContact.setGroupId(1);

                baseService.updateContact(updatedContact);

                showListView(updatedContact.getGroupId());

                break;
            case 3:
                baseService.deleteContact(1);



                break;
            case 4:
                List<Contact> contactList = baseService.getContacts();
                for(Contact contact : contactList) {
                    Log.d(TAG, contact.toString());
                }

                List<Contact> contacts = baseService.getContacts();
//                final ContactAdapter phonesAdapter = new ContactAdapter(this, 100, contacts);
//                lvMain.setAdapter(phonesAdapter);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addPhone(View view) {
        Contact contact = new Contact();
        contact.setName("Петров С.И.");
        contact.setPhone("24-12-20");
        contact.setEmail("info@dd.ru");
        contact.setGroupId(1);
        baseService.insertContact(contact);
    }

}