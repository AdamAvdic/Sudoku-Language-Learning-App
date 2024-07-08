package com.example.sudokuvocabulary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.sudokuvocabulary.models.SudokuModel;

import org.junit.Test;

public class SudokuModelTest {

    private final int[][] testGrid1 = {
            {4, 5, 6, 9, 7, 3, 1, 8, 2},
            {3, 7, 1, 2, 8, 5, 4, 6, 9},
            {2, 8, 9, 6, 4, 1, 7, 3, 5},
            {9, 6, 4, 1, 2, 7, 3, 5, 8},
            {1, 2, 5, 3, 6, 8, 9, 7, 4},
            {8, 3, 7, 4, 5, 9, 2, 1, 6},
            {7, 9, 8, 5, 3, 4, 6, 2, 1},
            {5, 1, 2, 7, 9, 6, 8, 4, 3},
            {6, 4, 3, 8, 1, 2, 5, 9, 7},
    };

    private final int[] testArray1 = {
            4, 5, 6, 9, 7, 3, 1, 8, 2,
            3, 7, 1, 2, 8, 5, 4, 6, 9,
            2, 8, 9, 6, 4, 1, 7, 3, 5,
            9, 6, 4, 1, 2, 7, 3, 5, 8,
            1, 2, 5, 3, 6, 8, 9, 7, 4,
            8, 3, 7, 4, 5, 9, 2, 1, 6,
            7, 9, 8, 5, 3, 4, 6, 2, 1,
            5, 1, 2, 7, 9, 6, 8, 4, 3,
            6, 4, 3, 8, 1, 2, 5, 9, 7,
    };

    private final int[][] testGrid2 = {
            {4, 6, 9, 8, 7, 1, 5, 3, 2},
            {5, 2, 7, 9, 6, 3, 4, 1, 8},
            {3, 1, 8, 2, 5, 4, 9, 6, 7},
            {7, 5, 1, 4, 9, 6, 8, 2, 3},
            {2, 9, 4, 3, 1, 8, 7, 5, 6},
            {8, 3, 6, 5, 2, 7, 1, 4, 9},
            {6, 4, 2, 1, 8, 9, 3, 7, 5},
            {9, 7, 3, 6, 4, 5, 2, 8, 1},
            {1, 8, 5, 7, 3, 2, 6, 9, 4}
    };

    private final int[] testArray2 = {
            4, 6, 9, 8, 7, 1, 5, 3, 2,
            5, 2, 7, 9, 6, 3, 4, 1, 8,
            3, 1, 8, 2, 5, 4, 9, 6, 7,
            7, 5, 1, 4, 9, 6, 8, 2, 3,
            2, 9, 4, 3, 1, 8, 7, 5, 6,
            8, 3, 6, 5, 2, 7, 1, 4, 9,
            6, 4, 2, 1, 8, 9, 3, 7, 5,
            9, 7, 3, 6, 4, 5, 2, 8, 1,
            1, 8, 5, 7, 3, 2, 6, 9, 4
    };

    int[][] testGrid3 = {
            {0, 7, 0, 0, 0, 8, 0, 0, 0},
            {0, 0, 0, 0, 0, 9, 7, 4, 0},
            {8, 4, 0, 2, 0, 0, 0 ,6, 0},
            {1, 0, 0, 0, 0, 6, 0, 0, 0},
            {3, 9, 0, 0, 0, 0, 0, 8, 5},
            {0, 0, 0, 3, 0, 0, 0, 0, 1},
            {0, 2, 0, 0, 0, 4, 0, 9, 3},
            {0, 5, 8, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 5, 0, 0, 0, 1, 0},
    };

    int[] testArray3 = {
            0, 7, 0, 0, 0, 8, 0, 0, 0,
            0, 0, 0, 0, 0, 9, 7, 4, 0,
            8, 4, 0, 2, 0, 0, 0 ,6, 0,
            1, 0, 0, 0, 0, 6, 0, 0, 0,
            3, 9, 0, 0, 0, 0, 0, 8, 5,
            0, 0, 0, 3, 0, 0, 0, 0, 1,
            0, 2, 0, 0, 0, 4, 0, 9, 3,
            0, 5, 8, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 5, 0, 0, 0, 1, 0,
    };

    private final int[][][] testGridSuite = {testGrid1, testGrid2, testGrid3};
    private final int[][] testArraySuite = {testArray1, testArray2, testArray3};

    @Test
    public void getGridLength() {
        for (int[][] grid: testGridSuite) {
            SudokuModel model = new SudokuModel(grid);
            assertEquals(grid.length, model.getGridLength());
        }
        for (int[] array: testArraySuite) {
            SudokuModel model = new SudokuModel(array);
            assertEquals((int) Math.sqrt(array.length), model.getGridLength());
        }
    }

    @Test
    public void getGridAsMatrix() {
        for (int num=0; num < testGridSuite.length; num++) {
            int[][] grid = testGridSuite[num];
            SudokuModel model = new SudokuModel(grid);
            assertArrayEquals(grid, model.getGridAsMatrix());
            model = new SudokuModel(testArraySuite[num]);
            assertArrayEquals(testGridSuite[num], model.getGridAsMatrix());
        }
    }

    @Test
    public void setGrid() {
        for (int[][] grid: testGridSuite) {
            SudokuModel model = new SudokuModel();
            model.setGrid(grid);
            assertArrayEquals(grid, model.getGridAsMatrix());
        }
    }

    @Test
    public void setGridFromArray() {
        for (int[] array: testArraySuite) {
            SudokuModel model = new SudokuModel();
            model.setGridFromArray(array);
            assertArrayEquals(array, model.getGridAsArray());
        }
    }

    @Test
    public void getValueAt() {
        for (int[][] grid: testGridSuite) {
            SudokuModel model = new SudokuModel(grid);
            for (int num = 0; num < grid.length * grid.length; num++) {
                int row = num / grid.length, column = num % grid.length;
                assertEquals(grid[row][column], model.getValueAt(row, column));
            }
        }
    }

    @Test
    public void setValueAt() {
        int[][] emptyGrid = new int[9][9];
        SudokuModel model = new SudokuModel(emptyGrid);
        for (int num = 0; num < emptyGrid.length * emptyGrid.length; num++) {
            int row = num / emptyGrid.length, column = num % emptyGrid.length;
            model.setValueAt(row, column, num);
            assertEquals(num, model.getValueAt(row, column));
        }
    }

    @Test
    public void cellNotEmpty() {
        for (int[][] grid: testGridSuite) {
            SudokuModel model = new SudokuModel(grid);
            for(int row=0; row < grid.length; row++) {
                for (int column=0; column < grid.length; column++) {
                    model.setValueAt(row, column, grid[row][column]);
                    assertEquals(grid[row][column]!=0, model.cellNotEmpty(row, column));
                }
            }
        }
        for (int[] array: testArraySuite) {
            SudokuModel model = new SudokuModel(array);
            for(int index=0; index < array.length; index++) {
                int gridLength = (int) Math.sqrt(array.length);
                int row = index/gridLength, column = index%gridLength;
                model.setValueAt(row, column, array[index]);
                assertEquals(array[index]!=0, model.cellNotEmpty(row, column));
            }
        }
    }

    @Test
    public void isGridFilled() {
        SudokuModel model = new SudokuModel();
        model.setGrid(testGrid3);
        assertFalse(model.isGridFilled());
    }

    @Test
    public void getGridAsArray() {
        for (int[] array: testArraySuite) {
            SudokuModel model = new SudokuModel(array);
            assertArrayEquals(array, model.getGridAsArray());
        }
    }
}