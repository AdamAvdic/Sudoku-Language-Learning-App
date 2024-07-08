package com.example.sudokuvocabulary.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.sudokuvocabulary.R;
import com.example.sudokuvocabulary.activities.BaseSudokuActivity;
import com.example.sudokuvocabulary.activities.MainMenuActivity;

public class ExitGameDialogFragment extends DialogFragment {

    public static ExitGameDialogFragment newInstance(int num) {
        ExitGameDialogFragment f = new ExitGameDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_exit_game)
                .setPositiveButton(R.string.exit, (dialog, id) -> {
                    ((BaseSudokuActivity) getActivity()).saveGame();
                    Intent intent = new Intent(getContext(), MainMenuActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> ((BaseSudokuActivity) getActivity()).resumeTimer());
        // Create the AlertDialog object and return it
        return builder.create();
    }
}