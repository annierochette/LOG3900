package com.example.polydraw;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatBoxActivity extends AppCompatActivity {
    public RecyclerView myRecyclerView;
    public List<Message> MessageList;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messageTxt;
    public Button send;

    private Socket socket;

    public String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        messageTxt = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);

        Username = (String) getIntent().getExtras().getString(MainActivity.USERNAME);

        try {

            socket = IO.socket("http://10.200.17.65:5050");

            socket.connect();

            socket.emit("connection");
            socket.emit("changeUsername", Username);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MessageList = new ArrayList<>();
        myRecyclerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageTxt.getText().toString().trim().isEmpty() && !messageTxt.getText().toString().isEmpty()) {

                    socket.emit("chat message", Username, messageTxt.getText().toString());
                    messageTxt.setText(" ");
                }
            }
        });

        socket.on("disconnection", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Toast.makeText(ChatBoxActivity.this, data, Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();

    }
}
