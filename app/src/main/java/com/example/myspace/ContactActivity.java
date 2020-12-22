package com.example.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myspace.data.BaseService;
import com.example.myspace.data.ContactAdapter;
import com.example.myspace.data.entity.Contact;

import java.util.List;

public class ContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    BaseService baseService;

    Button buttonAdd;
    Spinner groupSpinner;
    ListView lvMain;

    private static int initialGroupId = 1;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent intent = new Intent(this, BaseService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        buttonAdd = findViewById(R.id.button_add);

        lvMain = findViewById(R.id.list_items);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Log.d(TAG, "finish onCreate");
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BaseService.LocalBinder binder = (BaseService.LocalBinder) service;
            baseService = binder.getService();

            groupSpinner = (Spinner) findViewById(R.id.type_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ContactActivity.this, R.array.contact_groups, R.layout.contact_spinner_item);
            adapter.setDropDownViewResource(R.layout.contact_spinner_dropdown_item);
            groupSpinner.setAdapter(adapter);
            groupSpinner.setSelection(initialGroupId);
            groupSpinner.setOnItemSelectedListener(ContactActivity.this);

            Log.d(TAG, "ContactActivity  onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "ContactActivity  onServiceDisconnected");
        }
    };

    private void showListView(final int groupId) {
//        Log.d(TAG, "showListView");
        List<Contact> contacts;
        if(groupId == 0) {
            contacts = baseService.getContacts();
        } else {
            contacts = baseService.findContactsByGroup(groupId);
        }

        final ContactAdapter phonesAdapter = new ContactAdapter(this, 100, contacts);
        lvMain.setAdapter(phonesAdapter);

        // выбор контакта для редактирования
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "itemClick: position = " + position + ", id = " + id + " : " + view.getId());
                view.setSelected(true);

                Contact contact = baseService.getContact((int) id);

                Intent intent = new Intent(ContactActivity.this, ContactFormActivity.class);
                intent.putExtra("name", contact.getName());
                intent.putExtra("phone", contact.getPhone());
                intent.putExtra("email", contact.getEmail());

                intent.putExtra("id", contact.getId());
                intent.putExtra("groupId", groupId);

                startActivityForResult(intent, 1);
            }
        });

        // долгое нажатие для удаления контакта
        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "itemLongClick: position = " + position + ", id = " + id);
                final int contactId = (int) id;

                AlertDialog.Builder alert = new AlertDialog.Builder(ContactActivity.this);
                alert.setTitle("Удаление контакта");
                alert.setMessage("Удалить выбранный контакт?");
                alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int groupId = baseService.getContact(contactId).getGroupId();
                        baseService.deleteContact(contactId);

                        showListView(groupId);
                    }
                });
                alert.show();

                return true;
            }
        });
    }

    public void addContact(View view) {
        int groupId = groupSpinner.getSelectedItemPosition();
//        Log.d(TAG, "position = " + groupId);

        Intent intent = new Intent(ContactActivity.this, ContactFormActivity.class);
        intent.putExtra("name", "");
        intent.putExtra("phone", "");
        intent.putExtra("email", "");

        intent.putExtra("id", 0);
        intent.putExtra("groupId", groupId);

        startActivityForResult(intent, 0);
    }

    // сохранение отредактированного контакта
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        int groupId = data.getIntExtra("groupId", 0);

        if(resultCode == RESULT_OK) {
            if(requestCode == 0) {  // новый
                Contact newContact = new Contact(data.getStringExtra("newName"), data.getStringExtra("newPhone"), data.getStringExtra("newEmail"));
                newContact.setGroupId(groupId);
                baseService.insertContact(newContact);
            } else { // редактирование существующего
                int contactId = data.getIntExtra("id", 0);
                Contact contact = baseService.getContact(contactId);

                contact.setName(data.getStringExtra("newName"));
                contact.setPhone(data.getStringExtra("newPhone"));
                contact.setEmail(data.getStringExtra("newEmail"));

                baseService.updateContact(contact);
            }
        }

        showListView(groupId);
    }

    // выбор группы
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG, "Выбор группы: position " + position + ", id " + id);
        if(position == 0) {
            buttonAdd.setEnabled(false);
//            buttonAdd.getBackground().setColorFilter(Color.argb(90, 217, 217, 217), PorterDuff.Mode.MULTIPLY);
//            buttonAdd.setTextColor(Color.argb(1, 217, 217, 217));
        } else {
            buttonAdd.setEnabled(true);
//            buttonAdd.getBackground().setColorFilter(null);
//            buttonAdd.setTextColor(Color.argb(1, 92, 92, 92));
        }

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

    @Override
    protected void onDestroy() {
        Log.d(TAG, "ContactActivity onDestroy");
        initialGroupId = groupSpinner.getSelectedItemPosition();

        super.onDestroy();
    }

}