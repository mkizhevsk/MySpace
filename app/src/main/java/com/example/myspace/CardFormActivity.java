package com.example.myspace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CardFormActivity extends AppCompatActivity {

    private EditText editTextFront;
    private EditText editTextBack;
    private EditText editTextExample;

    private int cardId;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_form);

        editTextFront = findViewById(R.id.front);
        editTextBack = findViewById(R.id.back);
        editTextExample = findViewById(R.id.example);

        Intent intent = getIntent();

        editTextFront.setText(intent.getStringExtra("front"));
        editTextBack.setText(intent.getStringExtra("back"));
        editTextExample.setText(intent.getStringExtra("example"));

        cardId = intent.getIntExtra("id", 0);
    }

    public  void saveCard(View view) {
//        Log.d(TAG, "saveCard");
        setResult(RESULT_OK, getCardIntent());
        finish();
    }

    public Intent getCardIntent() {
        Intent intent = new Intent();

        intent.putExtra("newFront", editTextFront.getText().toString());
        intent.putExtra("newBack", editTextBack.getText().toString());
        intent.putExtra("newExample", editTextExample.getText().toString());

        intent.putExtra("id", cardId);

        return intent;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, getCardIntent());
        finish();
    }
}
