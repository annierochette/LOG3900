package com.example.polydraw.Socket;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.polydraw.Message;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ChatSocket {

    private String url;
    private String IpAddress;
    private Socket socket;


    public ChatSocket(){

    }


    public void init(){
        if(socket == null){
            try{
                socket = IO.socket("https://fais-moi-un-dessin.herokuapp.com/");
                socket = socket.connect();
                socket.emit("connection");
            }
            catch(URISyntaxException e){
                e.printStackTrace();

            }
        }
    }

    public void sendMessage(Button button){
        JSONObject data = new JSONObject();
        try {

            String username = data.getString("username");
            final String message = data.getString("message");
            String timestamp = data.getString("timestamp");

            final Message m = new Message(username,message,timestamp);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!m.getMessage().trim().isEmpty() && !m.getMessage().isEmpty()) {

                        socket.emit("chat message", m.getUsername(), m.getMessage());
                        m.setMessage(" ");
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void joinChannel(){


    }

    public void disconnect() {
        socket.disconnect();
    }

}
