package com.example.sudokuvocabulary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sudokuvocabulary.R;
import com.example.sudokuvocabulary.models.WordDictionaryModel;

public class DisplayListActivity extends MenuForAllActivity {

    private String tableName;
    private String[] words;
    private String[] translations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity header to the new table name
        tableName = getIntent().getStringExtra(getString(R.string.new_table_name_key));
        TextView activityTitle = findViewById(R.id.display_list_name_text);
        activityTitle.setText(tableName);

        // Get the currently selected words
        words = getIntent().getStringArrayExtra(
                getString(R.string.words_key));
        translations = getIntent().getStringArrayExtra(
                getString(R.string.translations_key));

        WordDictionaryModel dictionary = new WordDictionaryModel(words, translations);
        LinearLayout layout = findViewById(R.id.display_list_word_view);

        // Display the selected words
        for (String word: dictionary.getWordsAsArray()) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            textView.setText(word);
            textView.setTextSize(24);
            layout.addView(textView);
        }

        Button backButton = findViewById(R.id.display_list_back_button);
        backButton.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_display_list);
        TextView timerText = findViewById(R.id.TimerText);
        timerText.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        String categoryKey = getString(R.string.category_key);
        Intent intent = new Intent(DisplayListActivity.this,
                AddWordsActivity.class);

        // Pass the words and their translations to the word adding activity
        intent.putExtra(getString(R.string.new_table_name_key), tableName);
        intent.putExtra(getString(R.string.words_key), words);
        intent.putExtra(getString(R.string.translations_key), translations);
        intent.putExtra(categoryKey, getIntent().getStringExtra(categoryKey));
        startActivity(intent);
    }
}