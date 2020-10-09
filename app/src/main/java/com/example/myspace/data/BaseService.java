package com.example.myspace.data;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myspace.data.entity.Contact;
import com.example.myspace.data.entity.Note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class BaseService extends Service {

    private final IBinder mBinder = new LocalBinder();

    DBHelper dbHelper;

    private static final String dbName = "my_space.db";

    private static final String TAG = "MainActivity";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LL-dd");

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

    // Contact
    public void insertContact(Contact contact) {
        if(contact != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("phone", contact.getPhone());
            cv.put("email", contact.getEmail());
            cv.put("group_id", contact.getGroupId());

            long rowID = db.insert("contact", null, cv);
            Log.d(TAG, "Contact row inserted, ID = " + rowID);

            dbHelper.close();
        }
    }

    public void updateContact(Contact contact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("phone", contact.getPhone());
        cv.put("email", contact.getEmail());
        cv.put("group_id", contact.getGroupId());

        int updCount = db.update("contact", cv, "id = " + contact.getId(), null);
        Log.d(TAG, "updated rows count = " + updCount);

        dbHelper.close();
    }

    public void deleteContact(int contactId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int delCount = db.delete("note", "id = " + contactId, null);
        Log.d(TAG, "deleted rows count = " + delCount);

        dbHelper.close();
    }

    public void readContacts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor contactCursor = db.query("contact", null, null, null, null, null, null);

        if (contactCursor.moveToFirst()) {

            int idColIndex = contactCursor.getColumnIndex("id");
            int phoneColIndex = contactCursor.getColumnIndex("phone");
            int emailColIndex = contactCursor.getColumnIndex("email");
            int groupIdColIndex = contactCursor.getColumnIndex("group_id");

            do {
                Log.d(TAG, contactCursor.getInt(idColIndex) + " " + contactCursor.getString(phoneColIndex) + " " + contactCursor.getString(emailColIndex) + " " + contactCursor.getInt(groupIdColIndex));
            } while (contactCursor.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        contactCursor.close();

        dbHelper.close();
    }

    public List<Contact> getContacts() {
        Log.d(TAG, "start getContacts");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor contactCursor = db.query("contact", null, null, null, null, null, null);

        List<Contact> contacts = new ArrayList<>();

        if (contactCursor.moveToFirst()) {

            int idColIndex = contactCursor.getColumnIndex("id");
            int phoneColIndex = contactCursor.getColumnIndex("phone");
            int emailColIndex = contactCursor.getColumnIndex("email");
            int groupIdColIndex = contactCursor.getColumnIndex("group_id");

            do {
                Contact contact = new Contact();
                contact.setId(contactCursor.getInt(idColIndex));
                contact.setPhone(contactCursor.getString(phoneColIndex));
                contact.setEmail(contactCursor.getString(emailColIndex));
                contact.setGroupId(contactCursor.getInt(groupIdColIndex));

                contacts.add(contact);
            } while (contactCursor.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        contactCursor.close();

        dbHelper.close();

        return contacts;
    }

    // Note
    public void insertNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        String date = formatter.format(note.getDate());
        cv.put("date", date);
        cv.put("content", note.getContent());
        long rowID = db.insert("note", null, cv);
        Log.d(TAG, "Note row inserted, ID = " + rowID);

        dbHelper.close();
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        Log.d(TAG, "--- Update mytable: ---");
        // подготовим значения для обновления
        String date = note.getDate().getYear() + "-" + note.getDate().getMonth().getValue() + "-" + note.getDate().getDayOfMonth();
        cv.put("date", date);
        cv.put("content", note.getContent());
        // обновляем по id
        int updCount = db.update("note", cv, "id = " + note.getId(), null);
        Log.d(TAG, "updated rows count = " + updCount);
        dbHelper.close();
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int delCount = db.delete("note", "id = " + noteId, null);
        Log.d(TAG, "deleted rows count = " + delCount);

        dbHelper.close();
    }

    public List<Note> getNotes() {
        Log.d(TAG, "start getNotes");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor noteCursor = db.query("note", null, null, null, null, null, null);

        List<Note> notes = new ArrayList<>();

        if (noteCursor.moveToFirst()) {
            int idColIndex = noteCursor.getColumnIndex("id");
            int dateColIndex = noteCursor.getColumnIndex("date");
            int contentColIndex = noteCursor.getColumnIndex("content");

            do {
                Note note = new Note();
                note.setId(noteCursor.getInt(idColIndex));
                note.setDate(LocalDate.parse(noteCursor.getString(dateColIndex) , formatter));
                note.setContent(noteCursor.getString(contentColIndex));

                notes.add(note);
            } while (noteCursor.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        noteCursor.close();

        dbHelper.close();

        return notes;
    }

    public void readNotes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor noteCursor = db.query("note", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (noteCursor.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = noteCursor.getColumnIndex("id");
            int dateColIndex = noteCursor.getColumnIndex("date");
            int contentColIndex = noteCursor.getColumnIndex("content");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(TAG, noteCursor.getInt(idColIndex) + " " + noteCursor.getString(dateColIndex) + " " + noteCursor.getString(contentColIndex));

                LocalDate date = LocalDate.parse(noteCursor.getString(dateColIndex) , formatter);
                Log.d(TAG, date.toString());
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (noteCursor.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        noteCursor.close();

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
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.d(TAG, "database was exported successfully");
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
            if (sd.canWrite()) {
                Log.d(TAG, "importDatabase: 2");
                File importedDB = new File(sd.toString() + "/Download/", dbName);
                File currentDB = new File("/data/data/" + getPackageName() +"/databases/", dbName);

                if(!currentDB.exists()) {
                    dbHelper.getWritableDatabase();
                }

                if (currentDB.exists()) {
                    Log.d(TAG, "database was imported successfully");
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
