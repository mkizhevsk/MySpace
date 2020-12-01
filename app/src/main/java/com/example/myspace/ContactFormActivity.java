package com.example.myspace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ContactFormActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;

    private int contactId;
    private int groupId;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_form);

        editTextName = findViewById(R.id.name);
        editTextPhone = findViewById(R.id.phone);
        editTextEmail = findViewById(R.id.email);

        Intent intent = getIntent();

        editTextName.setText(intent.getStringExtra("name"));
        editTextPhone.setText(intent.getStringExtra("phone"));
        editTextEmail.setText(intent.getStringExtra("email"));

        contactId = intent.getIntExtra("id", 0);
        groupId = intent.getIntExtra("groupId", 0);
    }

    public  void saveContact(View view) {
        setResult(RESULT_OK, getContactIntent());
        finish();
    }

    public Intent getContactIntent() {
        Intent intent = new Intent();

        intent.putExtra("newName", editTextName.getText().toString());
        intent.putExtra("newPhone", editTextPhone.getText().toString());
        intent.putExtra("newEmail", editTextEmail.getText().toString());

        intent.putExtra("id", contactId);
        intent.putExtra("groupId", groupId);

        return intent;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, getContactIntent());
        finish();
    }
}
