package com.example.polydraw;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

//https://github.com/nkzawa/socket.io-android-chat/blob/master/app/src/main/java/com/github/nkzawa/socketio/androidchat/MainFragment.java

public class Chat extends Fragment {

    private ChatViewModel mViewModel;

    public static Chat newInstance() {
        return new Chat();
    }

    private RecyclerView recyclerView;
    private List<Message> MessageList;
    private ChatBoxAdapter chatBoxAdapter;
    private EditText messageTxt;
    private Button send;


    private Socket socket;

    public String Username;
    public String IpAddress;

    public Chat(){
        super();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof MainActivity){
            //=context

        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        //null

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        socket.connect();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.messagelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(chatBoxAdapter);

        messageTxt = (EditText) view.findViewById(R.id.message);

        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageTxt.getText().toString().trim().isEmpty() && !messageTxt.getText().toString().isEmpty()) {

                    socket.emit("chat message", Username, messageTxt.getText().toString());
                    messageTxt.setText(" ");
                }
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        // TODO: Use the ViewModel
        ChatViewModel viewHolder = new ChatViewModel(MessageList);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        socket.disconnect();
    }

}
