package com.example.polydraw;

public class ChatChannel {
    private String channelName;
    private Boolean isCurrent;

    public ChatChannel(){

    }
    public ChatChannel(String channelName) {
        this.channelName = channelName;
        this.isCurrent = false;
    }

    public String getChannelName() {
        return channelName;
    }

    public Boolean isCurrent(){
        return isCurrent;
    }

    public void setCurrent(Boolean current){
        this.isCurrent = current;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void createChannel(){
        //post request to create new channel
    }
}
