package com.example.polydraw;


import android.content.Intent;

import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    public String currentChannel = "Général";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        messageTxt = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        addChannel = (ImageButton) findViewById(R.id.addChannel);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        myRecyclerView = (RecyclerView) findViewById(R.id.messagelist);
        channelsRecyclerView = (RecyclerView) findViewById(R.id.channelsList);

        MessageList = new ArrayList<>();
        ChannelList = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getApplicationContext());
        channelsRecyclerView.setLayoutManager(cLayoutManager);
        channelsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        socket.getSocket().emit("channels");
        socket.getSocket().on("channels", getChannels);

//        ChatChannel c = new ChatChannel("General");
//        c.setCurrent(true);
//        ChannelList.add(c);
        for(ChatChannel channel: ChannelList) {
            if(channel.getChannelName().equals(currentChannel)){
                channel.setCurrent(true);
            }
        }

        socket.getSocket().emit("history", currentChannel);
//        socket.getSocket().on("history", retrieveHistory);

        updateChannelRecycler(ChannelList);
        setToolbarName(currentChannel);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageTxt.getText().toString().trim().isEmpty() && !messageTxt.getText().toString().isEmpty()) {

                    socket.getSocket().emit("chat message", Username, currentChannel, messageTxt.getText().toString());
                    messageTxt.setText(" ");
                }
            }
        });

        messageTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    if (!messageTxt.getText().toString().trim().isEmpty() && !messageTxt.getText().toString().isEmpty()) {

                        socket.getSocket().emit("chat message", Username, currentChannel, messageTxt.getText().toString());
                        messageTxt.setText(" ");
                        handled = true;
                    }
                }
                return handled;
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

                            if(data.getString("channel").equals(currentChannel)){
                                String username = data.getString("username");
                                String message = data.getString("message");
                                String timestamp = data.getString("timestamp");

                                Message m = new Message(username,message,timestamp);

                                MessageList.add(m);
                            }

                            updateMessageRecycler(MessageList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        socket.getSocket().on("newChannel", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String newChannel = (String) args[0];
                        ChatChannel nc = new ChatChannel(newChannel);

                        System.out.println("New channel " + newChannel);

                        if(currentChannel.equals(newChannel)){
                            switchCurrentChannelNoPosition();
                            nc.setCurrent(true);
                        }

                        ChannelList.add(nc);
                        updateChannelRecycler(ChannelList);
                    }
                });
            }
        });

        socket.getSocket().on("deleteChannel", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String deletedChannel = (String) args[0];
                        List<ChatChannel> channelsToDelete = new ArrayList<ChatChannel>();

                        System.out.println("Channel deleted " + deletedChannel);

                        for(ChatChannel channel: ChannelList) {
                            if(channel.getChannelName().equals(deletedChannel)){
                                channelsToDelete.add(channel);
                            }
                        }
                        ChannelList.removeAll(channelsToDelete);
                        updateChannelRecycler(ChannelList);
                    }
                });
            }
        });

        socket.getSocket().on("history", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject messages = (JSONObject) args[0];

                        System.out.println("Retrieve messages");
                        System.out.println(messages);
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

                            socket.getSocket().emit("joinChannel", Username, result);
                            System.out.println("Joined " + result);


                            socket.getSocket().emit("history", result);
//                            socket.getSocket().on("history", retrieveHistory);
//                            socket.getSocket().emit("leaveChannel", Username, currentChannel);
//                            System.out.println("Left " + currentChannel);

                            currentChannel = result;

                            updateChannelRecycler(ChannelList);

//                            ChatChannel newChannel = new ChatChannel(result);
//                            switchCurrentChannelNoPosition();
//                            newChannel.setCurrent(true);
//                            ChannelList.add(newChannel);
//                            updateChannelRecycler(ChannelList);

                            Toast.makeText(ChatBoxActivity.this, "Le canal '" + result + "' a été créé", Toast.LENGTH_SHORT).show();
                            Toast.makeText(ChatBoxActivity.this, "Vous êtes désormais dans le canal " + result , Toast.LENGTH_SHORT).show();
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

    private void updateChannelRecycler(List<ChatChannel> channelList){

        chatChannelAdapter = new ChatChannelAdapter(channelList, this);
        chatChannelAdapter.notifyDataSetChanged();
        channelsRecyclerView.setAdapter(chatChannelAdapter);
        System.out.println("ChannelRecycler updated");
    }

    private void updateMessageRecycler(List<Message> messageList){

        chatBoxAdapter = new ChatBoxAdapter(messageList);
        chatBoxAdapter.notifyDataSetChanged();
        myRecyclerView.setAdapter(chatBoxAdapter);
    }

    @Override
    public void onChannelClick(int position) {
        ChatChannel channel = ChannelList.get(position);

        if(!channel.getChannelName().equals(currentChannel)){

            socket.getSocket().emit("leaveChannel", Username, currentChannel);
            currentChannel = channel.getChannelName();

            socket.getSocket().emit("history", currentChannel);
            switchCurrentChannel(position);

            socket.getSocket().emit("joinChannel", Username, channel.getChannelName());

            Toast.makeText(ChatBoxActivity.this, "Vous êtes désormais dans le canal " + channel.getChannelName() , Toast.LENGTH_SHORT).show();
//            socket.getSocket().emit("channels");
//            socket.getSocket().on("channels", getChannels);

        }
    }

    private void switchCurrentChannel(int position){
        for (ChatChannel channel: ChannelList) {
            channel.setCurrent(false);
        }

        ChannelList.get(position).setCurrent(true);

        MessageList = new ArrayList<Message>();

        updateMessageRecycler(MessageList);
        updateChannelRecycler(ChannelList);
        setToolbarName(currentChannel);
    }

    private void switchCurrentChannelNoPosition(){
        for (ChatChannel channel: ChannelList) {
            channel.setCurrent(false);
        }
        MessageList = new ArrayList<Message>();

        updateMessageRecycler(MessageList);
        updateChannelRecycler(ChannelList);
        setToolbarName(currentChannel);
    }

//    private Emitter.Listener retrieveHistory = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            JSONObject messages = (JSONObject) args[0];
//
//            System.out.println("Retrieve messages");
//            System.out.println(messages);
//        }
//    };

    private Emitter.Listener getChannels = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray channels = (JSONArray) args[0];
                    System.out.println(channels);
                    List<ChatChannel> channelsToAdd = new ArrayList<ChatChannel>();

                    try{
                        for(int i = 0; i < channels.length(); i++){
                            String newChannel = channels.getString(i);
                            channelsToAdd.add(new ChatChannel(newChannel));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    ChannelList = new ArrayList<ChatChannel>();
                    ChannelList.addAll(channelsToAdd);
                    updateChannelRecycler(ChannelList);
                }
            });
        }
    };
}
