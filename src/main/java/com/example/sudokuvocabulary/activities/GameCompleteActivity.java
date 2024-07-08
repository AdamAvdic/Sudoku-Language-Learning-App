package com.example.sudokuvocabulary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sudokuvocabulary.R;
import com.example.sudokuvocabulary.utils.PrefUtils;
import com.example.sudokuvocabulary.utils.SaveFileUtil;

public class GameCompleteActivity extends MenuForAllActivity {

    private String[] mWords, mTranslations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mWords = getIntent().getStringArrayExtra(getString(R.string.words_key));
        mTranslations = getIntent().getStringArrayExtra(getString(R.string.translations_key));

        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(view -> {

            // Delete the save file if the finished game was from a loaded save
            if (PrefUtils.loadBoolPreference(this, getString(R.string.save_game_key))) {
                SaveFileUtil.deleteSave(this, getString(R.string.save_game_file));
            }

            // Launch the main menu
            Intent intent = new Intent(GameCompleteActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });

        // Set up the restart button
        Button restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(view -> {
            Intent intent;
            // Check which mode the game is currently in
            if (getIntent().getBooleanExtra(getString(R.string.mode_key), false)) {
                intent = new Intent(this, ListenModeActivity.class);
            } else {
                intent = new Intent(GameCompleteActivity.this, SudokuActivity.class);
            }
            intent.putExtra(getString(R.string.words_key), mWords);
            intent.putExtra(getString(R.string.translations_key), mTranslations);
            startActivity(intent);
        });

        // Get and show the time the user took to complete the puzzle
        long duration = (long) getIntent().getDoubleExtra(
                getString(R.string.time_key), 0);

        TextView durationTextView = findViewById(R.id.duration_textview);

        int minutes = (int)(duration / 60);
        int seconds = (int) (duration % 60);

        String timeText = "Time taken to complete: ";
        if (minutes > 0) timeText += minutes + " minutes ";
        timeText += seconds + " seconds";

        durationTextView.setText(timeText);
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_game_complete);
        TextView timerText = findViewById(R.id.TimerText);
        timerText.setVisibility(View.GONE);
    }
}