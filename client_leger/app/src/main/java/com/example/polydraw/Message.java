package com.example.polydraw;

public class Message {
    private String memberInfo;
    private String message;
    private boolean isSentByUser;

    public Message(String memberInfo, String message, boolean isSentByUser){
        this.memberInfo = memberInfo;
        this.message = message;
        this.isSentByUser = isSentByUser;
    }

    public String getMemberInfo() {
        return memberInfo;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }
}
