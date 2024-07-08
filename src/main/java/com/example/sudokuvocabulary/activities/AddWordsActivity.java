package com.example.sudokuvocabulary.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.example.sudokuvocabulary.R;
import com.example.sudokuvocabulary.adapters.DBAdapter;
import com.example.sudokuvocabulary.models.WordDictionaryModel;
import com.example.sudokuvocabulary.models.WordSampleModel;

import java.util.ArrayList;

public class AddWordsActivity extends MenuForAllActivity implements View.OnClickListener {
    private static int NUM_ROWS = 8;
    private static final int NUM_COLS = 3;
    private WordDictionaryModel words = new WordDictionaryModel();
    private WordDictionaryModel wordsAdded;
    private DBAdapter db;
    private String category;
    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the toolbar in the timer
        TextView timer = findViewById(R.id.TimerText);
        timer.setVisibility(View.GONE);

        // Get the category that was selected in the previous activity
        category = getIntent().getStringExtra(getString(R.string.category_key)).toLowerCase();
        TextView title = findViewById(R.id.category_title);

        // Creates a string with the first letter capitalized
        String title_text = String.format(getString(R.string.title_text),
                category.substring(0,1).toUpperCase(), category.substring(1));
        title.setText(title_text);

        // Retrieve the words for the given category from the database
        db = new DBAdapter(this);
        db.open();
        ArrayList<ArrayList<String>> words = db.getWordsFromCategory(category);
        this.words = new WordDictionaryModel(words.get(0), words.get(1));

        // Check if activity was returned to from other activity
        // Restore existing values if so
        if (getIntent().getStringArrayExtra(getString(R.string.words_key)) != null) {
            String[] wordsArray = getIntent()
                    .getStringArrayExtra(getString(R.string.words_key));
            String[] translationsArray = getIntent()
                    .getStringArrayExtra(getString(R.string.translations_key));
            wordsAdded = new WordDictionaryModel(wordsArray, translationsArray);
        } else {
            wordsAdded = new WordDictionaryModel();
        }
        tableName = getIntent().getStringExtra(getString(R.string.new_table_name_key));

        // Calculate the number of rows needed to put all words into table
        NUM_ROWS = (int) Math.ceil((double) this.words.getLength() / NUM_COLS);

        populateButtons();

        Button displayListButton = findViewById(R.id.display_list_button);
        displayListButton.setOnClickListener(this);

        Button playButton = findViewById(R.id.animal_category_play_button);
        playButton.setOnClickListener(this);

        Button prevButton = findViewById(R.id.animal_category_prev_button);
        prevButton.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_add_words);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.animal_category_play_button:
                if (wordsAdded.getLength() < 4) {
                    Toast.makeText(this,
                            "At least 4 words are needed!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Add the new word list to database
                    db.newTable(tableName);
                    for (WordSampleModel wordSampleModel : wordsAdded.getWords()) {
                        db.insertRow(wordSampleModel.getWord(), wordSampleModel.getTranslation(), tableName);
                    }
                    // Launch the sudoku game
                    intent = newIntent(AddWordsActivity.this, SudokuActivity.class);
                    intent.putExtra(getString(R.string.size_key), wordsAdded.getLength());
                    intent.putExtra(getString(R.string.sub_width_key),
                            (int) Math.ceil(Math.sqrt(wordsAdded.getLength()))
                    );
                    intent.putExtra(getString(R.string.sub_height_key),
                            (int) Math.floor(Math.sqrt(wordsAdded.getLength()))
                    );
                }
                break;
            case R.id.display_list_button:
                intent = newIntent(AddWordsActivity.this, DisplayListActivity.class);
                break;
            case R.id.animal_category_prev_button:
                intent = newIntent(AddWordsActivity.this,
                        WordCategoryActivity.class);
                break;
        }
        if (db.isOpen()) db.close();
        if (intent != null) startActivity(intent);
    }

    private void populateButtons() {
        //define table
        TableLayout table = (TableLayout) findViewById(R.id.table_for_buttons);
        for (int row = 0; row < NUM_ROWS; row++) {

            //add new row for each row
            TableRow tableRow = new TableRow(this);
            //set layout
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f ));//scaling weight
            //add row
            table.addView(tableRow);

            // Add table buttons
            for (int col = 0; col < NUM_COLS && col + row * NUM_COLS < words.getLength(); col++) {
                Button button = newTableButton(row, col);
                tableRow.addView(button);
            }
        }
    }

    private void gridButtonCLicked(String word) {
        wordsAdded.add(word, words.findTranslation(word));
        if (wordsAdded.getLength() >= 9) {
            Toast.makeText(this, "Nine words have been selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public Intent newIntent(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);

        // Pass currently selected words to next activity
        intent.putExtra(getString(R.string.words_key),
                wordsAdded.getWordsAsArray());
        intent.putExtra(getString(R.string.translations_key),
                wordsAdded.getTranslationsAsArray());

        // Pass new word list name and the selected word category
        intent.putExtra(getString(R.string.new_table_name_key), tableName);
        intent.putExtra(getString(R.string.category_key), category);

        return intent;
    }

    private Button newTableButton(int row, int column) {
        ToggleButton button = new ToggleButton(this);

        //set layout
        button.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f ));//scaling weight

        //put text on button
        String currentWord = words.getWord(column + row * NUM_COLS);
        // This line is necessary because other setText methods
        // do not immediately appear on buttons
        button.setText(currentWord);

        button.setTextOn(currentWord);
        button.setTextOff(currentWord);
        // Re-selects button if it was selected previously
        button.setChecked(wordsAdded.contains(currentWord));

        // Generate an ID for the button
        button.setId(View.generateViewId());

        //padding
        button.setPadding(0,0,0,0);

        //action for button clicked
        button.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            int index = column + row * NUM_COLS;
            if (isChecked) { // Add word to list
                gridButtonCLicked(words.getWord(index));
            } else { // Remove word from list if deselected
                wordsAdded.remove(words.getWord(index));
            }
        });
        return button;
    }
}
