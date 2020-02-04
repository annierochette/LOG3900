package com.example.polydraw;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.net.Socket;
import java.util.List;

public class ChatBoxActivity extends AppCompatActivity {
    public RecyclerView myRecyclerView;
    public List<Message> MessageList;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt;
    public Button send;

    private Socket socket;

    public String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        messagetxt = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);

        Username = (String)getIntent().getExtras().getString(MainActivity.USERNAME);

    }
}
