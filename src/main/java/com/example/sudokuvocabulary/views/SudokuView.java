package com.example.sudokuvocabulary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sudokuvocabulary.R;

public class SudokuView extends View {
    private final int mGridColour;
    private final int mCellItemFillColour;
    private final Paint mGridColourPaint = new Paint();
    private final Paint mCellItemFillColourPaint = new Paint();
    private int mCellHeight;
    private int mCellWidth;
    private int mGridLength = 9;
    private int mSubGridWidth = 3;
    private int mSubGridHeight = 3; // To be used for drawing various sized grids
    private String[][] mWordsToDraw = null;

    public SudokuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSaveEnabled(true);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SudokuGrid, 0, 0);
        try {
            mGridColour = attributes.getInteger(
                    R.styleable.SudokuGrid_gridColor,
                    0
            );
            mCellItemFillColour = attributes.getInteger(
                    R.styleable.SudokuGrid_cellItemFillColour,
                    0
            );
        } finally {
            attributes.recycle();
        }
    }

    @Override
    public void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int min = Math.min(getMeasuredWidth(), getMeasuredHeight());
        // Calculate the size for each individual cell
        mCellHeight = (min / mGridLength);
        mCellWidth = getMeasuredWidth() / mGridLength;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mGridColourPaint.setStyle(Paint.Style.STROKE);
        mGridColourPaint.setColor(mGridColour);
        mGridColourPaint.setStrokeWidth(14);
        mGridColourPaint.setAntiAlias(true);

        mCellItemFillColourPaint.setStyle(Paint.Style.FILL);
        if (getResources().getBoolean(R.bool.isTablet)) {
            mCellItemFillColourPaint.setTextSize(36);
        } else {
            mCellItemFillColourPaint.setTextSize((float) (getCellWidth()/3));
        }
        mCellItemFillColourPaint.setColor(mCellItemFillColour);
        mCellItemFillColourPaint.setAntiAlias(true);
        mCellItemFillColourPaint.setTextAlign(Paint.Align.CENTER);

        // Draw grid border
        canvas.drawRect(0,0, getWidth(), getHeight(), mGridColourPaint);
        // Draw the Sudoku grid and fill each cell with the correct word
        drawGrid(canvas);
        drawCellItems(canvas);
    }

    public void setView(int[][] grid, String[] words) {
        mGridLength = grid.length;
        mSubGridWidth = (int) Math.ceil(Math.sqrt(mGridLength));
        mSubGridHeight = (int) Math.floor(Math.sqrt(mGridLength));
        mWordsToDraw = new String[mGridLength][mGridLength];
        for (int i = 0; i < mGridLength* mGridLength; i++) {
            int row = i / mGridLength, column = i % mGridLength;
            // Set each cell to the english word specified by grid
            if (grid[row][column] != 0) {
                mWordsToDraw[row][column] = words[grid[row][column] - 1];
            }
        }
        // Call invalidate to redraw the grid
        this.invalidate();
    }

    public void setView(String[][] wordsToDraw) {
        mGridLength = wordsToDraw.length;
        mWordsToDraw = wordsToDraw;
        mSubGridWidth = (int) Math.ceil(Math.sqrt(mGridLength));
        mSubGridHeight = (int) Math.floor(Math.sqrt(mGridLength));
        this.invalidate();
    }

    public int getCellHeight() {
        return mCellHeight;
    }

    public int getCellWidth() {
        return mCellWidth;
    }

    public String[][] getWordsToDraw() {
        return mWordsToDraw;
    }

    public String getWordToDrawAt(int row, int column) {
        return mWordsToDraw[row][column];
    }

    public void setWordToDrawAt(int row, int column, String word) {
        mWordsToDraw[row][column] = word;
        this.invalidate();
    }

    public void drawCellItems(Canvas canvas) {
        if (mWordsToDraw == null) return;
        for (int i = 0; i < mGridLength*mGridLength; i++) {
            int row = i / mGridLength, column = i % mGridLength;
            String word = mWordsToDraw[row][column];
            if (word != null) {
                int x_axis = (int) ((column * mCellWidth) + (0.5 * mCellWidth));
                int y_axis = (int) ((row * mCellHeight) + (0.8 * mCellHeight));
                int textLength = (int)(mCellWidth / mCellItemFillColourPaint.getTextSize() + 1);

                String wordToDraw = word.length() > textLength ?
                        word.substring(0, textLength) : word;
                canvas.drawText(wordToDraw, x_axis, y_axis, mCellItemFillColourPaint);
            }
        }
    }

    private void drawThickLine() {
        mGridColourPaint.setStyle(Paint.Style.STROKE);
        mGridColourPaint.setStrokeWidth(12);
        mGridColourPaint.setColor(mGridColour);
    }

    private void drawThinLine() {
        mGridColourPaint.setStyle(Paint.Style.STROKE);
        mGridColourPaint.setStrokeWidth(6);
        mGridColourPaint.setColor(mGridColour);
    }

    private void drawGrid(Canvas canvas) {
        // Draw rows
        for (int line = 0; line < mGridLength +1; line++) {
            // Check if current line is a major line, draw a thicker line if so
            if (line % mSubGridHeight == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }
            //Draw row lines
            canvas.drawLine(0, mCellHeight *line, getWidth(), mCellHeight *line, mGridColourPaint);
        }

        // Draw columns
        for (int line = 0; line < mGridLength +1; line++) {
            // Check if current line is a major line, draw a thicker line if so
            if (line % mSubGridWidth == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }
            // Draw column lines
            canvas.drawLine(mCellWidth *line, 0, mCellWidth * line, getHeight(), mGridColourPaint);
        }
    }
}
