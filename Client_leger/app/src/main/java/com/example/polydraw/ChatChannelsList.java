package com.example.polydraw;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChatChannelsList extends AppCompatDialogFragment {

    String[] channels = {"ajouter nouveau canal", "channel 1", "channel 2", "channel 3", "channel 4"};


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Canaux de discussion");

        builder.setItems(channels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                }
            }
        });

        return builder.create();
    }
}


