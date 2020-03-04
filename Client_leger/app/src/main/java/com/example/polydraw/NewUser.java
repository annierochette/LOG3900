package com.example.polydraw;

public class NewUser {
    private String name;
    private String surname;
    private String username;
    private String password;
    private String passwordConfirmation;

    public NewUser(){

    }
    public NewUser(String name, String surname, String username, String pwd, String pwdConfirmation) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = pwd;
        this.passwordConfirmation = pwdConfirmation;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() { return passwordConfirmation; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public void setPasswordConfirmation(String pwdConfirmation) {
        this.passwordConfirmation = pwdConfirmation;
    }

}
