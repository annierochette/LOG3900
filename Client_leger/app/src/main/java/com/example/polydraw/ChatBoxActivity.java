package com.example.polydraw;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Map;

//sources: https://stackoverflow.com/questions/44300547/adding-items-to-listview-from-android-alertdialog

public class ChatBoxActivity extends AppCompatActivity implements NewChatChannel.NewChatChannelListener, ChatChannelAdapter.OnChannelListener {

    public RecyclerView myRecyclerView;
    public List<Message> MessageList;
    public List<ChatChannel> ChannelList;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messageTxt;
    public Button send;
    ImageButton addChannel;
    public RecyclerView channelsRecyclerView;
    public ChatChannelAdapter chatChannelAdapter;
    public Toolbar toolbar;
    private SocketIO socket;

    String Username = MainActivity.editTextString;
    public String currentChannel = "General";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        messageTxt = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        addChannel = (ImageButton) findViewById(R.id.addChannel);
//        ListView lv = (ListView) findViewById(R.id.channelsList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        MessageList = new ArrayList<>();
        ChannelList = new ArrayList<>();

//        final List<ChatChannel> availableChannels = new ArrayList<ChatChannel>(Arrays.asList(channels));

        myRecyclerView = (RecyclerView) findViewById(R.id.messagelist);
        channelsRecyclerView = (RecyclerView) findViewById(R.id.channelsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getApplicationContext());
        channelsRecyclerView.setLayoutManager(cLayoutManager);
        channelsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        socket.getSocket().emit("channels");
        socket.getSocket().on("channels", getChannels);

        ChatChannel c = new ChatChannel("General");
//        ChatChannel g = new ChatChannel("GGG");
//        ChatChannel f = new ChatChannel("GGGGGGGGGGGGG");
        ChannelList.add(c);
//        ChannelList.add(g);
//        ChannelList.add(f);
//
//        chatChannelAdapter = new ChatChannelAdapter(ChannelList, this);
//        chatChannelAdapter.notifyDataSetChanged();
//
//        channelsRecyclerView.setAdapter(chatChannelAdapter);

        updateRecycler(ChannelList);

        setToolbarName(currentChannel);

//        channelsRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String channel = availableChannels.get(position);
//                socket.getSocket().emit("leaveChannel", Username, currentChannel);
//                currentChannel = channel;
//                socket.getSocket().emit("joinChannel", Username, channel);
//                setToolbarName(currentChannel);
//                Toast.makeText(ChatBoxActivity.this, "Vous êtes désormais dans le canal " + channel , Toast.LENGTH_SHORT).show();
//                socket.getSocket().emit("channels");
//                socket.getSocket().on("channels", getChannels);
//            }
//        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageTxt.getText().toString().trim().isEmpty() && !messageTxt.getText().toString().isEmpty()) {

                    socket.getSocket().emit("chat message", Username, currentChannel, messageTxt.getText().toString());
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

                            ChatChannel newChannel = new ChatChannel(result);
                            ChannelList.add(newChannel);

                            updateRecycler(ChannelList);


//
//                            chatChannelAdapter = new ChatChannelAdapter(ChannelList);
//                            chatChannelAdapter.notifyDataSetChanged();
//
//                            channelsRecyclerView.setAdapter(chatChannelAdapter);

                            Toast.makeText(ChatBoxActivity.this, "Le canal '" + result + "' a été créé", Toast.LENGTH_SHORT).show();
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

    public void setToolbarName(String channel){
        //code pour choisir nom du channel selectionne
        toolbar.setTitle(channel);
    }

    private void updateRecycler(List<ChatChannel> channelList){

        chatChannelAdapter = new ChatChannelAdapter(channelList, this);
        chatChannelAdapter.notifyDataSetChanged();

        channelsRecyclerView.setAdapter(chatChannelAdapter);
    }

    @Override
    public void onChannelClick(int position) {
        ChatChannel channel = ChannelList.get(position);
        socket.getSocket().emit("leaveChannel", Username, currentChannel);
        currentChannel = channel.getChannelName();

        switchCurrentChannel(position);

        socket.getSocket().emit("joinChannel", Username, channel.getChannelName());


        Toast.makeText(ChatBoxActivity.this, "Vous êtes désormais dans le canal " + channel , Toast.LENGTH_SHORT).show();
        socket.getSocket().emit("channels");
        socket.getSocket().on("channels", getChannels);
    }

    private void switchCurrentChannel(int position){
        for (ChatChannel channel: ChannelList) {
            channel.setCurrent(false);
        }

        ChannelList.get(position).setCurrent(true);
        updateRecycler(ChannelList);
        setToolbarName(currentChannel);
    }

    private Emitter.Listener getChannels = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject channels = (JSONObject) args[0];
            System.out.println(channels);
        }
    };
}
