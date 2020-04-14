package com.example.polydraw;

public class Player {

//    {"player":{"_id":"5e910a205566572317d7a56c","username":"snoopy","lastName":"Brown","firstName":"Snoopy","password":"$2a$08$koN3Tu10281OU0tZ0.IdKeFxFQ6ZGob42b1KQFOt3B25rTH8aHPNC","token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InNub29weSIsImlhdCI6MTU4NjU3NDUwNH0.hBXym3o793NljYhJO6_ef3aBLI5SeMvRCcDICyTpnGk","__v":0}}

    private String username;
    private String lastName;
    private String firstName;
    private String token;

    public Player(String username, String lastName, String firstName, String token) {
        this.username = username;
        this.lastName = lastName;
        this.firstName = firstName;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

