package com.example.polydraw;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

//source: https://codinginflow.com/tutorials/android/custom-dialog-interface

public class NewChatChannel extends AppCompatDialogFragment {


    private EditText editChannel;
    String newChannelName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_channel_dialog, null);

        builder.setView(view)
                .setTitle("Nouveau canal de discussion")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newChannelName = editChannel.getText().toString();
                    }
                });

        editChannel = view.findViewById(R.id.new_channel);

        return builder.create();
    }

}
