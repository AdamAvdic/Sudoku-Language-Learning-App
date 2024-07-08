package com.example.sudokuvocabulary.activities;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.sudokuvocabulary.R;

public class TutorialActivity extends MenuForAllActivity {

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Hide the question mark button when inside the tutorial activity
        if (menu.findItem(R.id.action_tutorialBtn) != null) {
            menu.findItem(R.id.action_tutorialBtn).setVisible(false);
            return true;
        }
        return false;
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.tutorial);
        TextView timerText = findViewById(R.id.TimerText);
        timerText.setVisibility(View.GONE);
    }
}
