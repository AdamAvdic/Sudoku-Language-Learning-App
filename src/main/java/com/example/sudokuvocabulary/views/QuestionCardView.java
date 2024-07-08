package com.example.sudokuvocabulary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.sudokuvocabulary.R;

import java.util.Random;

public class QuestionCardView extends CardView {
    private final Context mContext;
    private final TableLayout mQuestionChoices;
    private Button[] mWordChoiceButtons;
    private int mButtonColumns;

    public QuestionCardView(@NonNull Context context) {
        this(context, null);
    }

    public QuestionCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        LayoutInflater cardInflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cardInflater.inflate(R.layout.question_card_layout, this, true);

        mQuestionChoices = findViewById(R.id.questionButtonLayout);
    }

    @Override
    public void onMeasure(int width, int height) {
        setMeasuredDimension(width, height);
        super.onMeasure(width, height);
    }

    public void setCard(String[] choices) {
        int numberOfChoices = choices.length;
        mButtonColumns = numberOfColumns(numberOfChoices);
        if (mWordChoiceButtons == null) {
            mWordChoiceButtons = newButtonArray(numberOfChoices);
            setTable();
        }
        setWordChoiceButtonsText(choices);
        this.postInvalidate();
    }

    public void show() {
        setVisibility(true);
    }

    public void hide() {
        setVisibility(false);
    }

    public void setVisibility(boolean isVisible) {
        this.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public boolean isVisible() {
        return this.getVisibility() == View.VISIBLE;
    }

    public Button[] getWordChoiceButtons() { return mWordChoiceButtons; }

    public void setWordChoiceButtonsText(String[] choices) {
        int buttonNum = 0;
        String[] shuffledChoices = choices.clone();
        int index;
        String temp;
        Random random = new Random();
        for (int i = shuffledChoices.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = shuffledChoices[index];
            shuffledChoices[index] = shuffledChoices[i];
            shuffledChoices[i] = temp;
        }
        for (Button button: mWordChoiceButtons) {
            button.setText(shuffledChoices[buttonNum++]);
            if (getResources().getBoolean(R.bool.isTablet)) {
                button.setTextSize((float) (42));
            }
        }
    }

    private TableRow newTableRow(Context context) {
        TableRow row = new TableRow(context);
        row.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return row;
    }

    private int numberOfColumns(int choices) {
        return (int) Math.ceil(Math.sqrt(choices));
    }

    private Button[] newButtonArray(int choices) {
        Button[] buttons = new Button[choices];
        for (int index = 0; index < choices; index++) {
            buttons[index] = new Button(mContext);
            buttons[index].setId(generateViewId());
        }
        return buttons;
    }

    private void setTable() {
        mQuestionChoices.removeAllViews();
        TableRow row = newTableRow(mContext);
        int buttonNum = 0;
        for (Button button: mWordChoiceButtons) {
            row.addView(button);
            if ((++buttonNum) % mButtonColumns == 0) {
                mQuestionChoices.addView(row);
                row = newTableRow(mContext);
            }
        }
        if (buttonNum % mButtonColumns != 0) mQuestionChoices.addView(row);
    }
}

