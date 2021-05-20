package com.example.myspace;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myspace.data.BaseService;
import com.example.myspace.data.entity.Note;

import java.time.LocalDate;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    BaseService baseService;

    Button buttonAdd;
    Spinner yearSpinner;
    ListView lvMain;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        this.setTitle("Diary");

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

            yearSpinner = findViewById(R.id.year_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(NoteActivity.this, R.array.diary_years, R.layout.contact_spinner_item);
            adapter.setDropDownViewResource(R.layout.contact_spinner_dropdown_item);
            yearSpinner.setAdapter(adapter);
            yearSpinner.setSelection(0); //
            yearSpinner.setOnItemSelectedListener(NoteActivity.this);

            Log.d(TAG, "ContactActivity  onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "ContactActivity  onServiceDisconnected");
        }
    };

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = yearSpinner.getSelectedItem().toString();
        Log.d(TAG, "Выбор года: position " + position + ", id " + id + " " + text);
        List<Note> notes = baseService.getNotesByYear(text);
        Log.d(TAG, "" + notes.size());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected");
    }

    public void addDiaryRecord(View view) {
        Note diaryNote = new Note();
        diaryNote.setDate(LocalDate.now());
        diaryNote.setContent("Тестовая запись");
        baseService.insertNote(diaryNote);
    }

}
