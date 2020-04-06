package com.example.polydraw;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//sources: https://stackoverflow.com/questions/44300547/adding-items-to-listview-from-android-alertdialog

public class ChatBoxActivity extends AppCompatActivity implements NewChatChannel.NewChatChannelListener {

    public RecyclerView myRecyclerView;
    public List<Message> MessageList;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messageTxt;
    public Button send;
    ImageButton addChannel;
    public ListView channelsRecyclerView;
    public ChatChannelAdapter chatChannelAdapter;

    private SocketIO socket;

    String Username = MainActivity.editTextString;
    public String IpAddress = "192.168.2.243";
    public String channelName;
    String[] channels = {"General"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        messageTxt = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        addChannel = (ImageButton) findViewById(R.id.addChannel);
        ListView lv = (ListView) findViewById(R.id.channelsList);

        /*try {

            chatsocket = IO.socket("http://"+IpAddress+":5050"); //https://fais-moi-un-dessin.herokuapp.com/"

            chatsocket.connect();

            chatsocket.emit("connection");
            chatsocket.emit("changeUsername", Username);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/

        MessageList = new ArrayList<>();
        myRecyclerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        String[] channels = {"Général"};
        channelsRecyclerView = (ListView) findViewById(R.id.channelsList);
        final List<String> availableChannels = new ArrayList<String>(Arrays.asList(channels));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, availableChannels);

        lv.setAdapter(arrayAdapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageTxt.getText().toString().trim().isEmpty() && !messageTxt.getText().toString().isEmpty()) {

                    socket.getSocket().emit("chat message", Username, "General", messageTxt.getText().toString());
                    messageTxt.setText(" ");
                }
            }
        });

        socket.getSocket().on("disconnection", new Emitter.Listener() {
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

        socket.getSocket().on("changeUsername", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean data = (Boolean) args[0];
                        if(!data){
                            Toast.makeText(ChatBoxActivity.this, "Nom déjà utilisé", Toast.LENGTH_SHORT).show();
                            socket.getSocket().emit("disconnection");
                            startActivity(new Intent(ChatBoxActivity.this, MainActivity.class));
                            socket.getSocket().disconnect();
                        }

                    }
                });
            }
        });

        socket.getSocket().on("chat message", new Emitter.Listener() {
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

        addChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ChatBoxActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.new_channel_dialog,null);
                final EditText newChannelName = (EditText) mView.findViewById(R.id.new_channel);
                Button addButton = (Button) mView.findViewById(R.id.addButton);
                Button cancelButton = (Button) mView.findViewById(R.id.cancelButton);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!newChannelName.getText().toString().isEmpty()) {
                            String result = newChannelName.getText().toString();
                            availableChannels.add(result);
                            arrayAdapter.notifyDataSetChanged();
                            Toast.makeText(ChatBoxActivity.this, "Le canal '"+ result +"' a été créé", Toast.LENGTH_SHORT).show();
                            //dismiss dialog once item is added successfully
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(ChatBoxActivity.this, "Veuillez écrire un nom de canal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void addChannel(String channel) {
        messageTxt.setText(channel);

    }

    public void chatToolbarName(){
        //code pour choisir nom du channel selectionne

    }
}
