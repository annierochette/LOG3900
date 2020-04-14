package com.example.polydraw;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class postMultiplayerGameDialog extends AppCompatDialogFragment {

    private String player;
    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private String _id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Intent intent = getActivity().getIntent();
        player = getArguments().getString("player");
        token = getArguments().getString("token");
        username = getArguments().getString("username");
        firstName = getArguments().getString("firstName");
        lastName = getArguments().getString("lastName");
        _id = getArguments().getString("_id");
        System.out.println(token);
        builder.setTitle("Fin de la partie")
                .setMessage(" ")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), PlayMenu.class);
                        intent.putExtra("token", token);
                        intent.putExtra("username", username);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("lastName", lastName);
                        intent.putExtra("_id", _id);
                        startActivity(intent);

                    }
                });

        return builder.create();
    }

}
