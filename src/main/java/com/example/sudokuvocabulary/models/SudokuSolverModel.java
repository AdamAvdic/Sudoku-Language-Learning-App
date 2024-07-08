package com.example.sudokuvocabulary.models;

import java.util.Random;

public class SudokuSolverModel {
    private interface RunInSolver {
        void execute(int solutions, int row, int column);
    }
    public static int solutions(SudokuModel board) {
        RunInSolver function = (solutions, row, column) ->
                board.setValueAt(row, column, 0);
        return solver(board, 0, 0, function);
    }
    public static boolean solve(SudokuModel board) {
        RunInSolver function = (solution, row, column) -> {
            if (solution > 0) {
                board.setNumberOfEmptyCells(board.getNumberOfEmptyCells()-1);
            } else {
                board.setValueAt(row, column, 0);
            }
        };
        return solver(board, 0, 0, function) == 1;
    }
    private static int solver(SudokuModel board, int index, int solutions, RunInSolver function) {
        if (solutions > 1) { return solutions; }
        if (index >= board.getGridSize()) {
            return solutions + 1;
        }
        int row = (index / board.getGridLength()), column = (index % board.getGridLength());
        if (board.cellNotEmpty(row, column)) {
            return solver(board, index+1, solutions, function);
        }

        for (int number: shuffleArray(board.getNumberArray())) {
            if (board.gridValid(row, column, number)) {
                board.setValueAt(row, column, number);
                solutions = solver(board, index+1, solutions, function);
                function.execute(solutions, row, column);
            }
        }
        return solutions;
    }

    private static int[] shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            int index = random.nextInt(i+1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }
}
