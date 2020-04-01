package com.example.polydraw.Socket;

import android.app.Activity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.SocketIOException;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class SocketIO {
    private static final String serverUrl = "http://192.168.2.194:5050";
    private static Socket socket;
    private static SocketIO instance;
    private static Activity act;
    private static String id;

    public SocketIO() {
    }

    public static void initInstance(String uid) {
        if (instance == null) {
            instance = new SocketIO();
            instance.initID(uid);
            if (SocketIO.getSocket() == null) {
                try{
                    Socket newSocket = IO.socket(serverUrl);
                    SocketIO.setSocket(newSocket);
                    SocketIO.connectIO();
                    emit("connection","General", null);
                }
                catch(URISyntaxException e){
                    e.printStackTrace();

                }
            }
        }
    }

    public static void setActivity(Activity a) {
        SocketIO.act = a;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        SocketIO.socket = socket;
    }

    public String getId() {
        return id;
    }

    private void initID(String uid) {
        if (SocketIO.id == null) {
            SocketIO.id = uid;
        }
    }

    public static void connectIO() {
//        try {
//            Socket.setDefaultSSLSocketFactory(SSLContext.getDefault());
//        } catch (NoSuchAlgorithmException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
        socket.connect();
    }

    public static void emit(String event, String channel, Object args) {
        if (!SocketIO.getSocket().connected()) {
            SocketIO.getSocket().connect();
        }
        SocketIO.getSocket().emit(event, channel,args);
    }

//    public static void emitWithAcknowledge(String event, Object args)
//            throws MalformedURLException {
//        if (!SocketIO.getSocket().connected()) {
//            SocketIO.getSocket().connect();
//        }
//        SocketIO.getSocket().emit(event, new IOAcknowledge() {
//
//            @Override
//            public void ack(Object... args) {
//                // TODO Auto-generated method stub
//
//            }
//        }, args);
//    }
}