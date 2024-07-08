package com.example.sudokuvocabulary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sudokuvocabulary.R;

public class SelectModeActivity extends MenuForAllActivity {

    private String modeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Key to store mode selected
        // false = normal mode, true = listening mode
        modeKey = getString(R.string.mode_key);

        // Set up normal mode button
        Button normalMode = findViewById(R.id.select_mode_normal_button);
        normalMode.setOnClickListener(view -> startActivity(newModeIntent(false)));

        // Set up listening mode button
        Button listenMode = findViewById(R.id.select_mode_listen_button);
        listenMode.setOnClickListener(view -> startActivity(newModeIntent(true)));

        // Set back button to go back to previous activity
        Button backButton = findViewById(R.id.select_mode_back_button);
        backButton.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_select_mode);
        TextView timerText = findViewById(R.id.TimerText);
        timerText.setVisibility(View.GONE);
    }

    private Intent newModeIntent(boolean mode) {
        Intent intent = new Intent(
                SelectModeActivity.this,
                SetSudokuSizeActivity.class
        );
        intent.putExtra(modeKey, mode);
        return intent;
    }
}