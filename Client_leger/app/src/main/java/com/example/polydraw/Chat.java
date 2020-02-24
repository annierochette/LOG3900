package com.example.polydraw;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Chat extends Fragment {

    private ChatViewModel mViewModel;

    public static Chat newInstance() {
        return new Chat();
    }

    public RecyclerView myRecyclerView;
    public List<Message> MessageList;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messageTxt;
    public Button send;


    private Socket socket;

    public String Username;
    public String IpAddress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        messageTxt = (EditText) view.findViewById(R.id.message);
        send = (Button) view.findViewById(R.id.send);

        /*Bundle extras = view.getIntent().getExtras();

        Username = (String) extras.getString(MainActivity.USERNAME);
        IpAddress = (String) extras.getString(MainActivity.IP_ADDRESS);*/

        try {

            socket = IO.socket("http://" + IpAddress + ":5050");

            socket.connect();

            socket.emit("connection");
            socket.emit("changeUsername", Username);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MessageList = new ArrayList<>();
        myRecyclerView = (RecyclerView) view.findViewById(R.id.messagelist);
        /*RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());*/

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageTxt.getText().toString().trim().isEmpty() && !messageTxt.getText().toString().isEmpty()) {

                    socket.emit("chat message", Username, messageTxt.getText().toString());
                    messageTxt.setText(" ");
                }
            }
        });



        /*socket.on("disconnection", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Toast.makeText(Chat.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        socket.on("changeUsername", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean data = (Boolean) args[0];
                        if(!data){
                            Toast.makeText(ChatBoxActivity.this, "Nom déjà utilisé", Toast.LENGTH_SHORT).show();
                            socket.emit("disconnection");
                            startActivity(new Intent(ChatBoxActivity.this, MainActivity.class));
                            socket.disconnect();
                        }

                    }
                });
            }
        });

        socket.on("chat message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject data = (JSONObject) args[0];
                        try {

                            String username = data.getString("username");
                            String message = data.getString("message");
                            String timestamp = data.getString("timestamp");

                            Message m = new Message(username,message,timestamp);


                            MessageList.add(m);

                            chatBoxAdapter = new ChatBoxAdapter(MessageList);

                            chatBoxAdapter.notifyDataSetChanged();

                            myRecyclerView.setAdapter(chatBoxAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });*/

        return view;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        // TODO: Use the ViewModel
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();

        socket.disconnect();
    }


}
