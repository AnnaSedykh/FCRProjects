package com.fcrcompany.fcrprojects.screens.start;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.fcrcompany.fcrprojects.R;

public class QuitDialog extends DialogFragment {

    public static final String QUIT_DIALOG_TAG = "quit_dialog_tag";

    private DialogInterface.OnClickListener listener;

    public void setListener(DialogInterface.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.quit_dialog_msg))
                .setNegativeButton(R.string.cancel, listener)
                .setPositiveButton(R.string.ok, listener)
                .create();

        return dialog;
    }
}
