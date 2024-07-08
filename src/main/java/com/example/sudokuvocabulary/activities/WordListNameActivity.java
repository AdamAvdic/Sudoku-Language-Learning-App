package com.example.sudokuvocabulary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sudokuvocabulary.R;
import com.example.sudokuvocabulary.adapters.DBAdapter;

import java.util.Locale;

public class WordListNameActivity extends MenuForAllActivity {

    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EditText text = findViewById(R.id.table_name_input); // Box for user input

        Button confirmButton = findViewById(R.id.confirm_name_button);
        confirmButton.setOnClickListener(view -> {
            // Get the new list name from the input box
            String listName = text.getText().toString();

            // Initialize the database
            db = new DBAdapter(this);
            db.open();

            // Check if the list name already exists, notify user if so
            if (db.getTableNames().contains(listName.toLowerCase(Locale.ROOT))) {
                Toast.makeText(
                        this,
                        "List name already used",
                        Toast.LENGTH_SHORT).show(
                );

            // Check if list name is valid, move to the word category selection menu
            } else if (listName.matches("[A-Za-z0-9]+")) {
                Intent intent = new Intent(
                        WordListNameActivity.this, WordCategoryActivity.class);
                intent.putExtra(getString(R.string.new_table_name_key), listName);
                startActivity(intent);

            // If the input box is empty, notify user
            } else if (listName.length() == 0) {
                Toast.makeText(
                        this,
                        "List name cannot be empty",
                        Toast.LENGTH_SHORT).show(
                );
            //The list name is invalid, notify the user
            } else {
                Toast.makeText(
                        this,
                        "List name cannot have spaces or special characters",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_word_list_name);
        TextView timerText = findViewById(R.id.TimerText);
        timerText.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }


}