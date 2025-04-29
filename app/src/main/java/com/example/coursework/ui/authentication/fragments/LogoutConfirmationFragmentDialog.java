package com.example.coursework.ui.authentication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.coursework.R;
import com.example.coursework.ui.authentication.fragments.utils.AlertDialogCallback;


public class LogoutConfirmationFragmentDialog extends DialogFragment {
    private AlertDialogCallback callback = null;

    public void attachCallback(AlertDialogCallback callback){
        this.callback = callback;
    }

    public void detachCallback(){
        this.callback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.logout_confirmation)
                .setPositiveButton(R.string.confirm, (dialogInterface, i) ->
                    callback.confirmButtonClicked()
                )
                .setNegativeButton(R.string.cancel, (dialogInterface, i) ->
                    callback.cancelButtonClicked()
                )
                .create();
    }
}
