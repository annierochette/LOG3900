package com.example.polydraw;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.List;

//source: https://codinginflow.com/tutorials/android/custom-dialog-interface

public class NewChatChannel extends AppCompatDialogFragment {


    private EditText editChannel;
    String newChannelName;
    private NewChatChannelListener listener;
    List<String> list;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_channel_dialog, null);

        builder.setView(view)
                .setTitle("Nouveau canal de discussion");


        editChannel = view.findViewById(R.id.new_channel);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NewChatChannelListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface NewChatChannelListener {
        void addChannel(String channel);

    }

}
