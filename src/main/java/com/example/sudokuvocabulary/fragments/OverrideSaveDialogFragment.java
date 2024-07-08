package com.example.sudokuvocabulary.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.sudokuvocabulary.R;
import com.example.sudokuvocabulary.activities.SelectModeActivity;
import com.example.sudokuvocabulary.utils.PrefUtils;

public class OverrideSaveDialogFragment extends DialogFragment {

    public static OverrideSaveDialogFragment newInstance(int num) {
        OverrideSaveDialogFragment f = new OverrideSaveDialogFragment();

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
        builder.setMessage(getString(R.string.dialog_override_save))
                .setPositiveButton(getString(R.string.confirm), (dialog, id) -> {
                    PrefUtils.saveBoolPreference(getContext(), getString(R.string.save_game_file), false);
                    Intent intent = new Intent(getContext(), SelectModeActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {});
        // Create the AlertDialog object and return it
        return builder.create();
    }
}