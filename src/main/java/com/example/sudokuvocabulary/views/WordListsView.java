package com.example.sudokuvocabulary.views;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sudokuvocabulary.R;

import java.util.ArrayList;

public class WordListsView extends ScrollView {

    private Button[] mListButtons;

    private final LinearLayout mButtonLayout;

    public WordListsView(@NonNull Context context) { this(context, null); }

    public WordListsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Inflate the layout file for the view
        LayoutInflater scrollInflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        scrollInflater.inflate(R.layout.view_word_lists, this, true);

        LinearLayout parentLayout = (LinearLayout) getChildAt(0);
        ScrollView scrollView = (ScrollView) parentLayout.getChildAt(0);

        // Set the linear layout to show content vertically/horizontally
        // depending on the device orientation
        mButtonLayout = (LinearLayout) scrollView.getChildAt(0);
        int orientation = getResources().getConfiguration().orientation;
        mButtonLayout.setOrientation(orientation == Configuration.ORIENTATION_PORTRAIT ?
                LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
    }

    @Override
    public void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight());
    }

    public Button[] getListButtons() {
        return mListButtons;
    }

    public void setWordListText(ArrayList<String> categories) {
        mListButtons = new Button[categories.size()];
        for (int wordListNumber=0; wordListNumber < categories.size(); wordListNumber++) {
            mListButtons[wordListNumber] = new Button(this.getContext());
            mListButtons[wordListNumber].setId(generateViewId());
            mListButtons[wordListNumber].setText(categories.get(wordListNumber));
            mButtonLayout.addView(mListButtons[wordListNumber]);
        }
        invalidate();
    }
}
