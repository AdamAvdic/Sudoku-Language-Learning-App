package com.example.sudokuvocabulary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.sudokuvocabulary.R;
import com.example.sudokuvocabulary.fragments.OverrideSaveDialogFragment;
import com.example.sudokuvocabulary.utils.PrefUtils;
import com.example.sudokuvocabulary.utils.SaveFileUtil;

public class MainMenuActivity extends MenuForAllActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up play button
        Button playButton = findViewById(R.id.main_menu_play_button);
        playButton.setOnClickListener(v -> {
            if (SaveFileUtil.saveExists(this, getString(R.string.save_game_file))) {
                DialogFragment newFragment = OverrideSaveDialogFragment.newInstance(0);
                newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
            } else {
                PrefUtils.saveBoolPreference(this, getString(R.string.save_game_file), false);Intent intent = new Intent (
                    this,
                    SelectModeActivity.class
            );
            startActivity(intent);}
        });

        // Set up load button
        String saveFileName = getString(R.string.save_game_file);
        Button loadButton = findViewById(R.id.load_save_button);
        loadButton.setEnabled(SaveFileUtil.saveExists(this, saveFileName));
        loadButton.setOnClickListener(view -> {
            PrefUtils.saveBoolPreference(this, getString(R.string.save_game_key), true);
            Intent intent;

            // Retrieve the game mode from the save file
            boolean savedGameMode = (boolean)  SaveFileUtil.readFromSave(
                    this,
                    getString(R.string.save_game_file),
                    SaveFileUtil.SaveObjects.GAME_MODE
            );

            // Check which mode the game was in according to the save file
            if (savedGameMode) {
                // Saved game was in listening mode
                intent = new Intent(MainMenuActivity.this, ListenModeActivity.class);
            } else {
                // Saved game was in normal mode
                intent = new Intent(MainMenuActivity.this, SudokuActivity.class);
            }
            startActivity(intent);
        });

        // Set up word bank button
        Button wordBankButton = findViewById(R.id.main_menu_word_bank_button);
        wordBankButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenuActivity.this, WordListsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main_menu);
        TextView timerText = findViewById(R.id.TimerText);
        timerText.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        System.exit(0);
    }
}