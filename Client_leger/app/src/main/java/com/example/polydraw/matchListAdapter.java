package com.example.polydraw;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polydraw.Socket.SocketIO;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class matchListAdapter extends RecyclerView.Adapter<matchListAdapter.MyViewHolder> {
    private ArrayList<String> matchList;
    private String token;
    private String username;
    private String lastName;
    private String firstName;
    private String _id;
    private SocketIO socket;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button button;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.gameName);
            button = (Button) view.findViewById(R.id.joinButton);
        }

    }

    public matchListAdapter(ArrayList<String> matchList, String token, String username, String lastName, String firstName, String _id, SocketIO socket) {
        this.matchList = matchList;
        this.token = token;
        this.username = username;
        this.lastName = lastName;
        this.firstName = firstName;
        this._id = _id;
        this.socket = socket;
    }

    @Override public int getItemCount() {
        return matchList.size();
    }

    @Override public matchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);

        return new matchListAdapter.MyViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final matchListAdapter.MyViewHolder holder, final int position){
        final String s = matchList.get(position);
        holder.name.setText(s);
        System.out.println();
        /*socket.getSocket().on("fullMatch", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String data = args[0].toString();
                System.out.println("FULL MATCH: "+data);

            }
        });*/


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WaitingRoom.class);
                intent.putExtra("matchId", s);
                intent.putExtra("token", token);
                intent.putExtra("username", username);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("_id", _id);
                System.out.print("PRINT USERNAME POUR EMIT JOIN: "+s +" "+ username);
                socket.getSocket().emit("joinGame", s, username);
                v.getContext().startActivity(intent);

            }
        });

    }
}
