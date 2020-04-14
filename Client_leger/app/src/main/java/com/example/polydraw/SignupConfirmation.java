package com.example.polydraw;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SignupConfirmation extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Le mot de passe contient une erreur...")
                .setMessage("Veuillez réécrire votre mot de passe");


        return builder.create();
    }
}
