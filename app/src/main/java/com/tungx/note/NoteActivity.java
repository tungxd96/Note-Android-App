package com.tungx.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.IOException;

public class NoteActivity extends AppCompatActivity {
    EditText editText;
    Intent intent;
    String note;
    SharedPreferences sharedPreferences;

    public void textReader() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                note = s.toString();
                sharedPreferences.edit().putString("note", note).apply();
                MainActivity.notes.set(MainActivity.cur, sharedPreferences.getString("note",""));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                try {
                    sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        sharedPreferences = this.getSharedPreferences("com.tungx.note", Context.MODE_PRIVATE);
        editText = (EditText) findViewById(R.id.editText);
        intent = getIntent();
        textReader();
        editText.setText(intent.getStringExtra("note"));
    }
}
