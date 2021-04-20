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

import java.util.List;

public class DiaryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    BaseService baseService;

    Button buttonAdd;
    Spinner groupSpinner;
    ListView lvMain;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
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

            groupSpinner = findViewById(R.id.year_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DiaryActivity.this, R.array.diary_years, R.layout.contact_spinner_item);
            adapter.setDropDownViewResource(R.layout.contact_spinner_dropdown_item);
            groupSpinner.setAdapter(adapter);
            groupSpinner.setSelection(0); //
            groupSpinner.setOnItemSelectedListener(DiaryActivity.this);

            Log.d(TAG, "ContactActivity  onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "ContactActivity  onServiceDisconnected");
        }
    };

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "Выбор года: position " + position + ", id " + id);
        List<Note> notes = baseService.getNotesByYear(2000);
        Log.d(TAG, "" + notes.size());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected");
    }

    public void addDiaryRecord(View view) {

    }

}
