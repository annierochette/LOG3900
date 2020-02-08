package com.example.polydraw;

public class Message {

    private String username;
    private String message;
    private String timestamp;

    public Message(){

    }
    public Message(String username, String message, String timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getTimestamp() { return timestamp; }
}
