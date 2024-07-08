package com.example.sudokuvocabulary.activities;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sudokuvocabulary.R;

import java.util.Locale;

public class ListenModeActivity extends BaseSudokuActivity {

    private TextToSpeech mTextToSpeech;

    @Override
    protected void onStart() {
        super.onStart();

        // Set up the text to Speech
        mTextToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                mTextToSpeech.setLanguage(Locale.CHINESE);
            }
        });
    }

    @Override
    public View.OnClickListener onClick() {
        return view -> {
            mChoicePicked = (String) ((Button) view).getText();
            String toastMessage;

            if (isCorrect()) {
                // Fill the cells with the word
                if (mSudokuModel.getValueAt(mCellRow, mCellColumn) == 0) {
                    mSudokuModel.checkAndFillCellAt(mCellRow, mCellColumn, mCellValue);
                }
                mSudokuView.setWordToDrawAt(mCellRow, mCellColumn, mWords[mCellValue-1]);
                toastMessage = getString(R.string.game_correct_toast_text);
            } else {
                toastMessage = getString(R.string.game_incorrect_toast_text);
            }
            mQuestionCard.setVisibility(View.GONE);
            Toast.makeText(view.getContext(), toastMessage, Toast.LENGTH_SHORT).show();

            // Stop the game if the board has been completely filled
            if (mSudokuModel.isGridFilled()) {
                timer.stop();
                Intent intent = newIntent(view.getContext(), mWords, mTranslations);
                intent.putExtra(getString(R.string.mode_key), true);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void initializeGrid() {
        String[] numbers  = new String[mSudokuModel.getGridLength()];
        int[] numbersToCopy = mSudokuModel.getNumberArray();
        int index = 0;
        for (int number: numbersToCopy) {
            numbers[index++] = Integer.toString(number);
        }
        mSudokuView.setView(mSudokuModel.getGridAsMatrix(), numbers);
    }

    @Override
    protected boolean isCorrect() {
        String[] wordsToCompare = (mSudokuModel.getValueAt(mCellRow, mCellColumn) != 0) ? mWords : mTranslations;
        return wordsToCompare[mCellValue-1].equals(mChoicePicked);
    }

    @Override
    protected void onCellNotEmpty(int cellValue) {
        // Checks if the cell has already been solved
        if (Character.isLetter(mSudokuView.getWordToDrawAt(mCellRow, mCellColumn).charAt(0))) {
            // Show a toast with the full word
            Toast.makeText(this, mWords[cellValue-1], Toast.LENGTH_SHORT).show();
            return;
        }

        // Read out the word from the selected cell
        mTextToSpeech.speak(
                mTranslations[cellValue-1],
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
        );

        mQuestionCard.setCard(mWords);
        mQuestionCard.show();
    }

    @Override
    protected void onCardRestore() {
        mQuestionCard = findViewById(R.id.questionCardView);
        mQuestionCard.setCard(mWords);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTextToSpeech.shutdown();
    }
}