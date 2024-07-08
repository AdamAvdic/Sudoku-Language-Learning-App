package com.example.sudokuvocabulary.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sudokuvocabulary.R;

public class SudokuActivity extends BaseSudokuActivity {

    @Override
    public View.OnClickListener onClick() {
        return view -> {
            // Get the user selected word from the button
            mChoicePicked = (String) ((Button) view).getText();
            String toastMessage;

            if (isCorrect()) {
                // Fill the cell with the selected word
                mSudokuModel.checkAndFillCellAt(mCellRow, mCellColumn, mCellValue);
                mSudokuView.setWordToDrawAt(mCellRow, mCellColumn, mTranslations[mCellValue-1]);
                mSudokuView.invalidate();
                toastMessage = getString(R.string.game_correct_toast_text);
            } else {
                toastMessage = getString(R.string.game_incorrect_toast_text);
            }

            // Hide the input prompt, and tell user if they are correct or not
            mQuestionCard.setVisibility(View.GONE);
            Toast.makeText(view.getContext(), toastMessage, Toast.LENGTH_SHORT).show();

            // Stop the game if the entire board has been filled
            if (mSudokuModel.isGridFilled()) {
                timer.stop();
                Intent intent = newIntent(view.getContext(), mWords, mTranslations);
                intent.putExtra(getString(R.string.mode_key), false);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void initializeGrid() {
        mSudokuView.setView(mSudokuModel.getGridAsMatrix(), mWords);
    }

    @Override
    protected boolean isCorrect() {
        return mChoicePicked.equals(
                mTranslations[mSudokuModel.getSolutionAt(mCellRow, mCellColumn)-1]);
    }

    @Override
    protected void onCellNotEmpty(int cellValue) {
        mQuestionCard.setCard(mWords);
        Toast.makeText(this, mWords[cellValue-1], Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCardRestore() {
        mQuestionCard = findViewById(R.id.questionCardView);
        mQuestionCard.setCard(mTranslations);
    }
}