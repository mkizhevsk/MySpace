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

import com.example.myspace.data.entity.Card;
import com.example.myspace.data.entity.Contact;
import com.example.myspace.data.entity.Note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseService extends Service {

    private final IBinder mBinder = new LocalBinder();

    DBHelper dbHelper;

    private static final String BASE_NAME = "my_space.db";
    private static final String TAG = "MainActivity";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LL-dd");

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

            long rowID = db.insert("contact", null, getContactContentValues(contact));
            Log.d(TAG, "contact row inserted, ID = " + rowID);

            dbHelper.close();
        }
    }

    private ContentValues getContactContentValues(Contact contact) {
        ContentValues cv = new ContentValues();

        cv.put("name", contact.getName());
        cv.put("phone", contact.getPhone());
        cv.put("email", contact.getEmail());
        cv.put("group_id", contact.getGroupId());

        return cv;
    }

    public void updateContact(Contact contact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int updCount = db.update("contact", getContactContentValues(contact), "id = " + contact.getId(), null);
        Log.d(TAG, "contact updated rows count = " + updCount);

        dbHelper.close();
    }

    public void deleteContact(int contactId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int delCount = db.delete("contact", "id = " + contactId, null);
        Log.d(TAG, "contact deleted rows count = " + delCount);

        dbHelper.close();
    }

    public Contact getContact(int contactId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM contact where id = " + contactId;
        Cursor contactCursor = db.rawQuery(sql,null);

        Contact contact = getCursorContacts(contactCursor).get(0);

        contactCursor.close();

        dbHelper.close();

        return contact;
    }

    private List<Contact> getCursorContacts(Cursor contactCursor) {
        List<Contact> contacts = new ArrayList<>();

        if (contactCursor.moveToFirst()) {
            int idColIndex = contactCursor.getColumnIndex("id");
            int nameColIndex = contactCursor.getColumnIndex("name");
            int phoneColIndex = contactCursor.getColumnIndex("phone");
            int emailColIndex = contactCursor.getColumnIndex("email");
            int groupIdColIndex = contactCursor.getColumnIndex("group_id");

            do {
                Contact contact = new Contact();
                contact.setId(contactCursor.getInt(idColIndex));
                contact.setName(contactCursor.getString(nameColIndex));
                contact.setPhone(contactCursor.getString(phoneColIndex));
                contact.setEmail(contactCursor.getString(emailColIndex));
                contact.setGroupId(contactCursor.getInt(groupIdColIndex));

                contacts.add(contact);
            } while (contactCursor.moveToNext());

        } else Log.d(TAG, "0 rows in all groups");

        return contacts;
    }

    public List<Contact> getContacts() {
        Log.d(TAG, "start getContacts");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor contactCursor = db.query("contact", null, null, null, null, null, null);

        List<Contact> contacts = getCursorContacts(contactCursor);

        contactCursor.close();

        dbHelper.close();

        Collections.sort(contacts);
        return contacts;
    }

    public List<Contact> findContactsByGroup(int groupId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM contact where group_id = " + groupId;
        Cursor contactCursor = db.rawQuery(sql,null);

        List<Contact> contacts = getCursorContacts(contactCursor);

        contactCursor.close();

        dbHelper.close();

        Collections.sort(contacts);
        return contacts;
    }

    // Note
    public void insertNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowID = db.insert("note", null, getNoteContentValues(note));
        Log.d(TAG, "note row inserted, ID = " + rowID);

        dbHelper.close();
    }

    private ContentValues getNoteContentValues(Note note) {
        ContentValues cv = new ContentValues();

        String date = formatter.format(note.getDate());

        cv.put("date", date);
        cv.put("content", note.getContent());

        return cv;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int updCount = db.update("note", getNoteContentValues(note), "id = " + note.getId(), null);
        Log.d(TAG, "note updated rows count = " + updCount);
        dbHelper.close();
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int delCount = db.delete("note", "id = " + noteId, null);
        Log.d(TAG, "note deleted rows count = " + delCount);

        dbHelper.close();
    }

    public Note getNote(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM note where id = " + noteId;
        Cursor noteCursor = db.rawQuery(sql,null);

        Note note = getCursorNotes(noteCursor).get(0);

        noteCursor.close();
        dbHelper.close();

        return note;
    }

    private List<Note> getCursorNotes(Cursor noteCursor) {
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
        } else Log.d(TAG, "notes: 0 rows");

        return notes;
    }

    public List<Note> getNotes() {
        Log.d(TAG, "start getNotes");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor noteCursor = db.query("note", null, null, null, null, null, null);

        List<Note> notes = getCursorNotes(noteCursor);

        noteCursor.close();

        dbHelper.close();

        return notes;
    }

    public List<Note> getNotesByYear(String year) {
        Log.d(TAG, "" + year);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //db.rawQuery("SELECT * FROM tblMoment WHERE strftime('%Y', searchDate) = ?", new String[]{year});

        //String sql = "SELECT * FROM note WHERE strftime('%Y', date) = " + year;
        //Log.d(TAG, sql);
        Cursor noteCursor = db.rawQuery("SELECT * FROM note WHERE strftime('%Y', date) = ?", new String[]{year});

        List<Note> notes = getCursorNotes(noteCursor);
        for(Note note : notes) {
            Log.d(TAG, note.toString());
        }

        noteCursor.close();

        dbHelper.close();

        return notes;
    }

    // Card
    public void insertCard(Card card) {
        Log.d(TAG, "start insert..");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowID = db.insert("card", null, getCardContentValues(card));
        Log.d(TAG, "Card row inserted, ID = " + rowID);

        dbHelper.close();
    }

    private ContentValues getCardContentValues(Card card) {
        ContentValues cv = new ContentValues();

        String date = formatter.format(card.getDate());

        cv.put("date", date);
        cv.put("front", card.getFront());
        cv.put("back", card.getBack());
        cv.put("example", card.getExample());
        cv.put("status", card.getStatus());

        return cv;
    }

    public void updateCard(Card card) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int updCount = db.update("card", getCardContentValues(card), "id = " + card.getId(), null);
        Log.d(TAG, "card updated rows count  = " + updCount);
        dbHelper.close();
    }

    public void deleteCard(int cardId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int delCount = db.delete("card", "id = " + cardId, null);
        Log.d(TAG, "card deleted rows count = " + delCount);

        dbHelper.close();
    }

    public Card getCard(int cardId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM card where id = " + cardId;
        Cursor cardCursor = db.rawQuery(sql,null);

        Card card = getCursorCards(cardCursor).get(0);

        cardCursor.close();
        dbHelper.close();

        return card;
    }

    public List<Card> getCursorCards(Cursor cardCursor) {
        List<Card> cards = new ArrayList<>();

        if (cardCursor.moveToFirst()) {
            int idColIndex = cardCursor.getColumnIndex("id");
            int dateColIndex = cardCursor.getColumnIndex("date");
            int frontColIndex = cardCursor.getColumnIndex("front");
            int backColIndex = cardCursor.getColumnIndex("back");
            int exampleColIndex = cardCursor.getColumnIndex("example");
            int statusIdColIndex = cardCursor.getColumnIndex("status");

            do {
                Card card = new Card();
                card.setId(cardCursor.getInt(idColIndex));
                card.setDate(LocalDate.parse(cardCursor.getString(dateColIndex) , formatter));
                card.setFront(cardCursor.getString(frontColIndex));
                card.setBack(cardCursor.getString(backColIndex));
                card.setExample(cardCursor.getString(exampleColIndex));
                card.setStatus(cardCursor.getInt(statusIdColIndex));

                cards.add(card);
            } while (cardCursor.moveToNext());

        } else Log.d(TAG, "cards: 0 rows");

        return cards;
    }

    public List<Card> getCards() {
        Log.d(TAG, "start getCards");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cardCursor = db.query("card", null, null, null, null, null, null);

        List<Card> cards = getCursorCards(cardCursor);

        cardCursor.close();
        dbHelper.close();

        return cards;
    }

    public List<Card> getCardsByStatus(int status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "SELECT * FROM card where status = " + status;
        Cursor cardCursor = db.rawQuery(sql,null);

        List<Card> cards = getCursorCards(cardCursor);

        cardCursor.close();

        dbHelper.close();

        return cards;
    }

    // export & import
    public void exportDatabase() {
//        Log.d(TAG, "start export..");
        try {
            File sd = Environment.getExternalStorageDirectory();
//            Log.d(TAG, "exportDatabase: " + sd.toString());
            if (sd.canWrite()) {
//                Log.d(TAG, "exportDatabase: 2");
                File currentDB = new File("/data/data/" + getPackageName() +"/databases/", BASE_NAME);

                String dateDbName = LocalDate.now().getYear() + "-" + Helper.getStringValue(LocalDate.now().getMonth().getValue()) +
                        "-" + Helper.getStringValue(LocalDate.now().getDayOfMonth()) + "_" + BASE_NAME;
                File backupDB = new File(sd.toString() + "/Download/", dateDbName);

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
//                Log.d(TAG, "importDatabase: 2");
                File importedDB = new File(sd.toString() + "/Download/", BASE_NAME);
                File currentDB = new File("/data/data/" + getPackageName() +"/databases/", BASE_NAME);

                if(!currentDB.exists()) {
                    dbHelper.getWritableDatabase();
                }

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(importedDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.d(TAG, "database was imported successfully");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
