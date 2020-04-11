package com.example.polydraw;

import com.google.gson.annotations.SerializedName;

public class Player {

//    {"player":{"_id":"5e910a205566572317d7a56c","username":"snoopy","lastName":"Brown","firstName":"Snoopy","password":"$2a$08$koN3Tu10281OU0tZ0.IdKeFxFQ6ZGob42b1KQFOt3B25rTH8aHPNC","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InNub29weSIsImlhdCI6MTU4NjU3NDUwNH0.hBXym3o793NljYhJO6_ef3aBLI5SeMvRCcDICyTpnGk","__v":0}}

    private int userId;

    private Integer id;
    private String username;
    private String lastName;
    private String firstName;
    private String password;
    private String token;
    private int _v;

    @SerializedName("body")
    private String text;

    public Player(int userId, String username, String lastName, String firstName, String password, String token, int __v) {
        this.userId = userId;
        this.username = username;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.token = token;
        this._v = _v;
    }

    public int getUserId() {
        return userId;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public int get_v() {
        return _v;
    }
}

