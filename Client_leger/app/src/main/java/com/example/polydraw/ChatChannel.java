package com.example.polydraw;

public class ChatChannel {
    private String channelName;

    public ChatChannel(){

    }
    public ChatChannel(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void createChannel(){
        //post request to create new channel
    }
}
