package com.example.sudokuvocabulary.models;

import java.io.Serializable;
import java.util.Random;

public class SudokuModel implements Serializable {

    // Serialization UID
    private static final long serialVersionUID = 2;

    // Height of each sub grid
    private final int mSubGridRows;
    // Width of each sub grid
    private final int mSubGridColumns;
    // Height/Width of the total Sudoku board
    private final int mGridLength;
    // Number of cells in the Sudoku board
    private final int mGridSize;
    // Matrix containing the current puzzle
    private int[][] mSudokuGrid;
    // Matrix containing the full solution to the puzzle
    private int[][] mSudokuSolution;
    // Array containing the number used to fill each cell
    private final int[] mNumberArray;
    // Current number of empty cells in the puzzle
    private int mNumOfEmptyCells;

    /**
     * <h1>SudokuModel</h1>
     * <p>The main constructor used to initialize Sudoku Boards of various sizes</p>
     * @param gridLength The side length of the Sudoku grid
     * @param subGridColumns The width of the sub grids
     * @param subGridRows The height of the sub grids
     * @param numOfEmptyCells The number of empty cell the grid should start with
     */
    public SudokuModel(int gridLength, int subGridColumns, int subGridRows, int numOfEmptyCells) {
        mSubGridRows = subGridRows;
        mSubGridColumns = subGridColumns;
        mGridLength = gridLength;
        mGridSize = gridLength*gridLength;
        mNumOfEmptyCells = numOfEmptyCells;
        mSudokuGrid = new int[gridLength][gridLength];
        mNumberArray = sequenceArray(gridLength);
        newFilledGrid();
        mSudokuSolution = copy(getGridAsMatrix());
        newPuzzle(numOfEmptyCells);
    }

    /**
     * <h1>SudokuModel</h1>
     * <p>Default constructor, constructs a normal 9x9 Sudoku Board with 5 empty cells</p>
     */
    public SudokuModel() {
        this(9, 3, 3, 5);
    }

    /**
     * <h1>SudokuModel</h1>
     * <p>Constructor used to initialize a puzzle from an integer matrix which already contains
     * a valid Sudoku puzzle.</p>
     * <p><b>Note:</b> This constructor is only used for testing as the model is already capable
     * of generating its own puzzles.</p>
     * @param grid A square integer matrix which contains a pre-defined Sudoku puzzle
     * @param subGridRows The height of each sub grid
     * @param subGridColumns The width of each sub grid
     */
    public SudokuModel(int[][] grid, int subGridRows, int subGridColumns) {
        mGridLength = grid.length;
        mGridSize = grid.length*grid.length;
        mSubGridRows = subGridRows;
        mSubGridColumns = subGridColumns;
        mNumberArray = sequenceArray(grid.length);
        setGrid(grid);
        mSudokuSolution = copy(getGridAsMatrix());
        mNumOfEmptyCells = findNumOfEmptyCells();
    }

    /**
     * <h1>SudokuModel</h1>
     * <p>Simple constructor which takes a pre-defined matrix containing a puzzle
     * and assumes the sub grids are square.</p>
     * @param grid Square integer matrix containing a valid Sudoku puzzle
     */
    public SudokuModel(int[][] grid) {
        this(grid, (int) Math.sqrt(grid.length), (int) Math.sqrt(grid.length));
    }

    /**
     * <h1>SudokuModel</h1>
     * <p>Constructor which initializes a puzzle from an integer array by expanding the array
     * into a matrix. Mainly used to initialize the model when restoring from an integer array
     * from a savedInstanceState.</p>
     * @param grid Integer array containing the numbers of a Sudoku puzzle, grid.length must be the
     *             square of some number greater than zero
     * @param subGridRows The height of a sub grid
     * @param subGridColumns The width of a sub grid
     */
    public SudokuModel(int[] grid, int subGridRows, int subGridColumns) {
        this(expand(grid), subGridRows, subGridColumns);
    }

    /**
     * <h1>SudokuModel</h1>
     * <p>Constructor used to initialize a 9x9 Sudoku puzzle from an array.</p>
     * @param grid An integer array containing a Sudoku puzzle, grid.length == 81
     */
    public SudokuModel(int[] grid) {
        this(grid, 3, 3);
    }

    /**
     * @return The length of the Sudoku grid/board.
     * i.e. for a 9x9 Sudoku board, it will return 9
     */
    public int getGridLength() { return mGridLength; }

    /**
     * @return The number of cells in the Sudoku board.
     * i.e. for a 9x9 Sudoku board, it will return 81
     */
    public int getGridSize() { return mGridSize; }

    /**
     * @return An integer array containing the numbers used to fill the puzzle.
     * i.e. for a typical 9x9 puzzle, it will return some permutation of the array:
     * {1, 2, 3, 4, 5, 6, 7, 8, 9}.
     */
    public int[] getNumberArray() { return mNumberArray; }

    /**
     * @return The current Sudoku board/grid as an integer matrix
     */
    public int[][] getGridAsMatrix() { return mSudokuGrid; }

    /**
     * @return The current Sudoku board/grid as an integer array
     */
    public int[] getGridAsArray() { return flatten(mSudokuGrid); }

    /**
     * @return The fully solved Sudoku puzzle as an array
     */
    public int[] getSolutionAsArray() {return flatten(mSudokuSolution); }

    /**
     * @param row The row number of the cell to access, starting from the top at index zero
     * @param column The column number of the cell to access, starting from the left at index zero
     * @return The correct answer for the specified cell
     */
    public int getSolutionAt(int row, int column) { return mSudokuSolution[row][column]; }

    /**
     * <p>Sets the current Sudoku puzzle to the given integer matrix.</p>
     * @param newGrid A Square integer matrix containing a valid Sudoku puzzle
     */
    public void setGrid(int[][] newGrid) {
        mSudokuGrid = newGrid;
        mNumOfEmptyCells = findNumOfEmptyCells();
    }

    /**
     * <p>Sets the current Sudoku puzzle to the given integer array.</p>
     * @param newArray An integer array containing a valid Sudoku puzzle, newArray.length must
     *                 be the square of some number
     */
    public void setGridFromArray(int[] newArray) {
        mSudokuGrid = expand(newArray);
    }

    /**
     * Set the solution to a Sudoku puzzle, only used when restoring after a device rotation
     * @param solution An integer array containing the solution to a Sudoku puzzle
     */
    public void setSolutionFromArray(int[] solution) {
        mSudokuSolution = expand(solution);
    }

    /**
     * <h1>Retrieves the number in the puzzle at the specified row and column.</h1>
     * @param row The row number of the value to get, counting from the top starting at zero
     * @param column The column number of the value to get, counting from the left starting at zero
     * @return The value at the specified row and column, zero if empty
     */
    public int getValueAt(int row, int column) {
        return mSudokuGrid[row][column];
    }

    /**
     * <p>Sets the cell at the specified row and column to the given value.</p>
     * @param row The row number of the value to get, counting from the top starting at zero
     * @param column The column number of the value to get, counting from the left starting at zero
     * @param value The number to set the cell to
     */
    public void setValueAt(int row, int column, int value) {
        mSudokuGrid[row][column] = value;
    }

    /**
     * <p>Sets the number of empty cell in the puzzle.</p>
     * @param numberOfEmptyCells The number of empty cells in the puzzle,
     *                           0 <= numberOfEmptyCells <= mGridLength
     */
    public void setNumberOfEmptyCells(int numberOfEmptyCells) {
        mNumOfEmptyCells = numberOfEmptyCells;
    }

    /**
     * @return The number of empty cells in the puzzle
     */
    public int getNumberOfEmptyCells() { return mNumOfEmptyCells; }

    /**
     * @param row The row number of the cell to check, index starts at zero counting from the top
     * @param column The column number of the cell to check, index starts at zero counting from
     *               the left
     * @return True if the cell is not empty, false otherwise
     */
    public boolean cellNotEmpty(int row, int column) { return getValueAt(row, column) != 0; }

    /**
     * @return True if there are no empty cells in the puzzle, false otherwise
     */
    public boolean isGridFilled() { return mNumOfEmptyCells == 0; }

    /**
     * @return The number of empty cells in the puzzle
     */
    public int findNumOfEmptyCells() {
        int numOfEmptyCells = 0;
        for (int n: getGridAsArray()) {
            if (n==0) {
               numOfEmptyCells++;
            }
        }
        return numOfEmptyCells;
    }

    /**
     * <p>Checks whether the given value is the correct answer at the specified cell and
     * places it in the cell.</p>
     *
     * @param row    The row number of the cell to check/modify, index starts at zero
     * @param column The column number of the cell to check/modify, index starts at zero
     * @param value  The number to place at the specified cell, 0 <= value <= mGridLength
     */
    public void checkAndFillCellAt(int row, int column, int value) {
        if (value == getSolutionAt(row, column)) {
            setValueAt(row, column, value);
            setNumberOfEmptyCells(mNumOfEmptyCells-1);
        }
    }

    /**
     * <p>Checks if the given number would be valid at the specified cell by
     * checking whether it already exists on the same row, column, and sub grid.</p>
     * @param value_row The row number of the cell to check
     * @param value_column The column number of the cell to check
     * @param number The number to check at the specified cell
     * @return True if the number is valid, false otherwise
     */
    public boolean gridValid(int value_row, int value_column, int number) {
        int subGridRowStart = calculateSubGridIndex(value_row, mSubGridRows);
        int subGridColumnStart = calculateSubGridIndex(value_column, mSubGridColumns);

        boolean valid = rowValid(value_row, number) && columnValid(value_column, number);
        return valid && subGridValid(subGridRowStart, subGridColumnStart, number);
    }

    /**
     * <p>Converts an integer matrix into an array by taking each row and
     * appending it to the end of an array. Used for converting grid to an array
     * to be saved in an savedInstanceState IntArrayExtra.</p>
     * @param matrix The integer matrix to convert to an array
     * @return An integer array of the values in the matrix
     */
    public static int[] flatten(int[][] matrix) {
        int[] flattenedArray = new int[matrix.length * matrix.length];
        int index = 0;
        for(int[] row: matrix) {
            for (int value: row) {
                flattenedArray[index++] = value;
            }
        }
        return flattenedArray;
    }

    /**
     * <p>Converts a String matrix into an array by taking each row and
     * appending it to the end of an array. Used for converting grid to an array
     * to be saved in an savedInstanceState StringArrayExtra.</p>
     * @param matrix The string matrix to convert to an array
     * @return A string array of the values in the matrix
     */
    public static String[] flatten(String[][] matrix) {
        String[] flattenedArray = new String[matrix.length * matrix.length];
        int index = 0;
        for(String[] row: matrix) {
            for (String value: row) {
                flattenedArray[index++] = value;
            }
        }
        return flattenedArray;
    }

    /**
     * <p>Converts an integer array into a square matrix in row-major order. Used to
     * restore a Sudoku puzzle from a savedInstanceState IntArray</p>
     * @param array The array to convert into a matrix, array.length must be the square
     *              of some number
     * @return A square integer matrix containing the values of the array
     */
    public static int[][] expand(int[] array) {
        int matrixLength = (int) Math.sqrt(array.length);
        int[][] expandedMatrix = new int[matrixLength][matrixLength];
        int index = 0;
        for (int value: array) {
            expandedMatrix[index/matrixLength][index%matrixLength] = value;
            index++;
        }
        return expandedMatrix;
    }

    /**
     * <p>Converts a string array into a square matrix in row-major order. Used to
     * restore the matrix from the savedInstanceState for SudokuView</p>
     * @param array The array to convert into a matrix, array.length must be the square
     *              of some number
     * @return A square string matrix containing the values of the array
     */
    public static String[][] expand(String[] array) {
        int matrixLength = (int) Math.sqrt(array.length);
        String[][] expandedMatrix = new String[matrixLength][matrixLength];
        int index = 0;
        for (String value: array) {
            expandedMatrix[index/matrixLength][index%matrixLength] = value;
            index++;
        }
        return expandedMatrix;
    }

    /**
     * <p>Generates a new puzzle with the given number of empty cells.
     * <b>Note:</b> Due to the random nature of the puzzle generating algorithm,
     * puzzles with more than 55 empty cells are not guaranteed to have the
     * correct number of empty cells.</p>
     * @param numOfEmptyCells The number of empty cells in the puzzle.
     *                        0 <= numOfEmptyCells <= 55
     */
    private void newPuzzle(int numOfEmptyCells) {
        setNumberOfEmptyCells(0);
        int[][] cells = new int[mGridSize][2];
        for (int index=0; index < mGridSize; index++) {
            cells[index][0] = index / mGridLength;
            cells[index][1] = index % mGridLength;
        }
        shuffleMatrix(cells);
        for (int[] cell: cells) {
            if (numOfEmptyCells  <= 0) { break; }
            int row = cell[0], column = cell[1], temp = getValueAt(row, column);
            setValueAt(row, column, 0);
            boolean uniqueSol = hasUniqueSolution();
            if (uniqueSol) {
                mNumOfEmptyCells++;
                numOfEmptyCells--;
            } else {
                setValueAt(row, column, temp);
            }
        }
    }

    /**
     * @return True if the current puzzle has only one solution, false otherwise
     */
    private boolean hasUniqueSolution() {
        return SudokuSolverModel.solutions(this) == 1;
    }

    /**
     * <p>Generates a new completely solved Sudoku board.</p>
     */
    private void newFilledGrid() {
        SudokuSolverModel.solve(this);
        mNumOfEmptyCells = 0;
    }

    /**
     * @param matrix The matrix to copy
     * @return A deep copy of the given matrix
     */
    private int[][] copy(int[][] matrix) {
        int[][] copy = new int[matrix.length][matrix.length];
        for (int i=0; i<matrix.length* matrix.length; i++) {
            int row = i/matrix.length, column = i% matrix.length;
            copy[row][column] = matrix[row][column];
        }
        return copy;
    }

    /**
     * @param length The length of the array/the number of elements to generate
     * @return An integer array containing numbers from 1 to length
     */
    private int[] sequenceArray(int length) {
        int[] array = new int[length];
        for (int num = 0; num < length; num++) {
            array[num] = num+1;
        }
        return array;
    }

    /**
     * <p>Shuffles the rows of the given matrix.</p>
     * @param matrix The matrix to shuffle
     */
    private void shuffleMatrix(int[][] matrix) {
        Random random = new Random();
        for (int i = 0; i < matrix.length; i++) {
            int index = random.nextInt(i+1);
            int[] temp = matrix[index];
            matrix[index] = matrix[i];
            matrix[i] = temp;
        }
    }

    /**
     * @param index The row/column number to calculate
     * @param subGridDimension The side length of the sub grid
     * @return The row/column number of the top-left corner of the sub grid the
     * given index is in
     */
    private int calculateSubGridIndex(int index, int subGridDimension) {
        return index - (index % subGridDimension) ;
    }

    /**
     * @param grid_row The row number to check
     * @param value The value to check
     * @return True if the value is not in the current row, false otherwise
     */
    private boolean rowValid(int grid_row, int value) {
        for (int number : mSudokuGrid[grid_row]) {
            if (number == value) { return false; }
        }
        return true;
    }

    /**
     * @param grid_column The column number to check
     * @param value The value to check
     * @return True if the value is not in the current column, false otherwise
     */
    private boolean columnValid(int grid_column, int value) {
        for (int row = 0; row < mGridLength; row++) {
            if (value == getValueAt(row, grid_column)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param grid_row_start The row number of the top left cell in the sub grid
     * @param grid_column_start The column number of the top left cell in the sub grid
     * @param value the value to check
     * @return True if the sub grid does not contain the value, false otherwise
     */
    private boolean subGridValid(int grid_row_start, int grid_column_start, int value) {
        int grid_row_end = grid_row_start + mSubGridRows;
        int grid_column_end = grid_column_start + mSubGridColumns;
        for (int row = grid_row_start; row < grid_row_end; row++) {
            for (int column = grid_column_start; column < grid_column_end; column++) {
                if (getValueAt(row, column) == value) {
                    return false;
                }
            }
        }
        return true;
    }
}