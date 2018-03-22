package com.tungx.note;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    public static ArrayList<String> notes = new ArrayList<>();
    public static ArrayAdapter arrayAdapter;
    public static int cur;
    Intent intent;
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        intent = new Intent(getApplicationContext(), NoteActivity.class);
        if(item.getItemId() == R.id.note) {
            notes.add("");
            arrayAdapter.notifyDataSetChanged();
            cur = MainActivity.notes.size() - 1;
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("com.tungx.note", Context.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.list);
        intent = new Intent(getApplicationContext(), NoteActivity.class);
        if(notes.size() == 0) {
            notes.add("Example note");
        }
        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes", ObjectSerializer.serialize(notes)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("note", notes.get(position));
                cur = position;
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this note?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    notes.remove(position);
                                    arrayAdapter.notifyDataSetChanged();
                                    try {
                                        sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                return true;
            }
        });
    }
}
