package com.example.myspace;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myspace.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class NoteFormActivity extends AppCompatActivity {

    private EditText editTextContent;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    private int noteId;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_form);

        Log.d(TAG, "start NoteFormActivity");

        editTextContent = findViewById(R.id.content);
        dateButton = findViewById(R.id.datePickerButton);

        Intent intent = getIntent();

        editTextContent.setText(intent.getStringExtra("content"));

        LocalDate date = LocalDate.parse(intent.getStringExtra("date"), formatter);
        Log.d(TAG, " " + intent.getStringExtra("date") + " | " + date.toString());

        initDatePicker(date);
    }

    private void initDatePicker(LocalDate date) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1) return "ЯНВ";
        if(month == 2) return "ФЕВ";
        if(month == 3) return "МАРТ";
        if(month == 4) return "АПР";
        if(month == 5) return "МАЙ";
        if(month == 6) return "ИЮНЬ";
        if(month == 7) return "ИЮЛЬ";
        if(month == 8) return "АВГ";
        if(month == 9) return "СЕНТ";
        if(month == 10) return "ОКТ";
        if(month == 11) return "НОЯБ";
        if(month == 12) return "ДЕК";
        return "ЯНВ";

    }


    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
