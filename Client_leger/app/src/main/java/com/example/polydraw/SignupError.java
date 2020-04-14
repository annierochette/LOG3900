package com.example.polydraw;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SignupError extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ce pseudonyme est déjà utilisé...")
                .setMessage("Veuillez utiliser un autre pseudonyme");


        return builder.create();
    }
}
