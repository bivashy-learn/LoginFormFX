package com.bivashy.javafx.authentication.model;

public class User {

    private final int id;
    private final String login;
    private final String hashedPassword;

    public User(int id, String login, String hashedPassword) {
        this.id = id;
        this.login = login;
        this.hashedPassword = hashedPassword;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

}

