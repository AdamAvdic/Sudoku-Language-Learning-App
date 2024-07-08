package com.example.sudokuvocabulary.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudokuvocabulary.R;
import com.example.sudokuvocabulary.fragments.ExitGameDialogFragment;
import com.example.sudokuvocabulary.models.SudokuModel;
import com.example.sudokuvocabulary.models.TimerModel;
import com.example.sudokuvocabulary.utils.PrefUtils;
import com.example.sudokuvocabulary.utils.SaveFileUtil;
import com.example.sudokuvocabulary.views.QuestionCardView;
import com.example.sudokuvocabulary.views.SudokuView;

import java.io.Serializable;

public abstract class BaseSudokuActivity extends MenuForAllActivity implements View.OnTouchListener {
    protected QuestionCardView mQuestionCard;
    protected SudokuView mSudokuView;
    protected SudokuModel mSudokuModel;
    protected String[] mWords, mTranslations;
    protected int mCellRow=0, mCellColumn=0, mCellValue=0, mStackLevel=0;
    protected String mWordPrompt, mChoicePicked, saveFileName;
    protected TextView TimerText;
    protected TimerModel timer;
    protected double startTime = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        saveFileName = getString(R.string.save_game_file); // Get the name of the save file

        mSudokuView = findViewById(R.id.sudokuGridView);
        mSudokuView.setOnTouchListener(this);

        int subWidth, subHeight;

        // Load existing save file if load button was pressed from main menu and save file exists
        if (PrefUtils.loadBoolPreference(this, getString(R.string.save_game_key)) &&
                SaveFileUtil.saveExists(this, saveFileName)) {
            Object[] savedObjects = SaveFileUtil.readAllFromSave(this, saveFileName);
            mSudokuModel = (SudokuModel) savedObjects[SaveFileUtil.SaveObjects.SUDOKU_MODEL.ordinal()];
            mWords = (String[]) savedObjects[SaveFileUtil.SaveObjects.WORDS.ordinal()];
            mTranslations = (String[]) savedObjects[SaveFileUtil.SaveObjects.TRANSLATIONS.ordinal()];
            mSudokuView.setView((String[][]) savedObjects[SaveFileUtil.SaveObjects.SUDOKU_GRID.ordinal()]);
            startTime = (double) savedObjects[SaveFileUtil.SaveObjects.TIME.ordinal()];
        } else {
            // Initialize a new game
            mWords = getIntent().getStringArrayExtra(getString(R.string.words_key));
            mTranslations = getIntent().getStringArrayExtra(getString(R.string.translations_key));
            subWidth = (int) Math.ceil(Math.sqrt(mWords.length));
            subHeight = (int) Math.floor(Math.sqrt(mWords.length));
            mSudokuModel = new SudokuModel(mWords.length, subWidth, subHeight, (mWords.length*mWords.length)/2);

            // Set the words to draw on the grid and the dimensions of the grid
            initializeGrid();

            // Restore the previous time if initializing after device rotation
            if (savedInstanceState != null) {
                startTime = savedInstanceState.getDouble(getString(R.string.time_key));
            }
        }

        // Hide the input prompt
        mQuestionCard = findViewById(R.id.questionCardView);
        mQuestionCard.hide();

        // Setup and start the timer
        TimerText = findViewById(R.id.TimerText);
        timer = new TimerModel(startTime, new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@Nullable Message msg) {
                super.handleMessage(msg);
                TimerText.setText(timer.getTimerText()); // Update timer view every second
            }
        });
        timer.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        // Check if the user deliberately pressed down on a cell and that
        // the input prompt is not currently visible
        if (motionEvent.getAction() != MotionEvent.ACTION_DOWN || mQuestionCard.isVisible()) {
            return false;
        }

        // Calculate which cell was pressed
        mCellRow = (int) (Math.ceil(motionEvent.getY() / mSudokuView.getCellHeight())) - 1;
        mCellColumn = (int) ((Math.ceil(motionEvent.getX()) / mSudokuView.getCellWidth()));
        mCellValue = mSudokuModel.getSolutionAt(mCellRow, mCellColumn);

        if (mSudokuModel.cellNotEmpty(mCellRow, mCellColumn)) {
            onCellNotEmpty(mCellValue);

        // Set up and show the input card
        } else {
            mWordPrompt = mWords[mSudokuModel.getSolutionAt(mCellRow, mCellColumn) - 1];
            mQuestionCard.setCard(mTranslations);
            mQuestionCard.show();
        }
        setButtonListeners(mQuestionCard.getWordChoiceButtons());
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save necessary components of the model for restoration
        savedInstanceState.putIntArray(getString(R.string.current_grid_key), mSudokuModel.getGridAsArray());
        savedInstanceState.putIntArray(getString(R.string.solution_grid_key), mSudokuModel.getSolutionAsArray());
        savedInstanceState.putInt(getString(R.string.cells_empty_key), mSudokuModel.getNumberOfEmptyCells());

        // Save the cell that the user has currently selected
        savedInstanceState.putInt(getString(R.string.cell_row_key), mCellRow);
        savedInstanceState.putInt(getString(R.string.cell_column_key), mCellColumn);
        savedInstanceState.putInt(getString(R.string.cell_value_key), mCellValue);

        // Save the elapsed time of the game
        savedInstanceState.putDouble(getString(R.string.time_key), timer.getTime());

        // Save the words used in the game
        savedInstanceState.putStringArray(getString(R.string.words_key), mWords);
        savedInstanceState.putStringArray(getString(R.string.translations_key), mTranslations);
        savedInstanceState.putString(getString(R.string.word_prompt_key), mWordPrompt);

        // Save the state of the visible Sudoku Board
        savedInstanceState.putStringArray(getString(R.string.word_grid_key),
                SudokuModel.flatten(mSudokuView.getWordsToDraw()));

        // Save the visible state of the input prompt card
        savedInstanceState.putBoolean(getString(R.string.popup_visibility_key), (mQuestionCard.getVisibility() == View.VISIBLE));

        // Save the game to a file,
        // can be restored even after quitting from app prematurely
        saveGame();
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the model
        mSudokuModel = new SudokuModel();
        mSudokuModel.setGridFromArray(savedInstanceState.getIntArray(getString(R.string.current_grid_key)));
        mSudokuModel.setSolutionFromArray(savedInstanceState.getIntArray(getString(R.string.solution_grid_key)));
        mSudokuModel.setNumberOfEmptyCells(savedInstanceState.getInt(getString(R.string.cells_empty_key)));

        mWords = savedInstanceState.getStringArray(getString(R.string.words_key));
        mTranslations = savedInstanceState.getStringArray(getString(R.string.translations_key));
        mWordPrompt = savedInstanceState.getString(getString(R.string.word_prompt_key));

        // Redraw the view
        mSudokuView = findViewById(R.id.sudokuGridView);
        String[][] wordsToDraw = SudokuModel.expand(
                savedInstanceState.getStringArray(getString(R.string.word_grid_key)));
        mSudokuView.setView(wordsToDraw);

        mCellRow = savedInstanceState.getInt(getString(R.string.cell_row_key));
        mCellColumn = savedInstanceState.getInt(getString(R.string.cell_column_key));
        mCellValue = savedInstanceState.getInt(getString(R.string.cell_value_key));

        onCardRestore();

        setButtonListeners(mQuestionCard.getWordChoiceButtons());

        // Set the card visibility to what it previously was
        mQuestionCard.setVisibility(savedInstanceState.getBoolean(getString(R.string.popup_visibility_key)));
    }

    @Override
    public void onBackPressed() {
        timer.stop();
        showExitDialog();
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_base_sudoku);
    }

    /**
     * @param packageContext Current activity context.
     * @param words The words currently used in the game.
     * @param translations The translated version of words.
     * @return An Intent to GameCompleteActivity containing words, translations, and
     * the current game time.
     */
    @NonNull
    public Intent newIntent(Context packageContext, String[] words, String[] translations) {
        Intent intent = new Intent(packageContext, GameCompleteActivity.class);
        intent.putExtra(getString(R.string.words_key), words);
        intent.putExtra(getString(R.string.translations_key), translations);
        intent.putExtra(getString(R.string.time_key), timer.getTime());
        return intent;
    }

    /**
     * @return An OnClickListener for the input prompt buttons.
     */
    protected abstract OnClickListener onClick();

    /**
     * Initializes the SudokuView for the activity.
     */
    protected abstract void initializeGrid();

    /**
     * Used to confirm that the option that the user selected is correct.
     *
     * @return True if the button selected from the input prompt card
     * is correct, false otherwise.
     */
    protected abstract boolean isCorrect();

    /**
     * Called when the user has pressed a filled cell in the game.
     *
     * @param cellValue The value inside the cell retrieved from the model.
     */
    protected abstract void onCellNotEmpty(int cellValue);

    /**
     * Called when restoring the input prompt card,
     * should contain a call to QuestionCardView.setCard()
     */
    protected abstract void onCardRestore();

    public void resumeTimer() {
        timer.start();
    }

    private void setButtonListeners(Button[] buttons) {
        for (Button button: buttons) {
            button.setOnClickListener(onClick());
        }
    }

    /**
     * Displays a dialog confirming that the user wishes to exit the active game.
     */
    private void showExitDialog() {
        mStackLevel++;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = ExitGameDialogFragment.newInstance(mStackLevel);
        newFragment.show(ft, "dialog");
    }

    /**
     * Saves necessary components of the game to a serialized file using SaveFileUtil.
     */
    public void saveGame() {
        Serializable[] objectsToSave = {
                this.getClass() == ListenModeActivity.class, // Current game mode
                mSudokuModel,
                mSudokuView.getWordsToDraw(),
                timer.getTime(),
                mWords,
                mTranslations,
        };
        SaveFileUtil.writeToSave(this, objectsToSave, saveFileName);
    }
}