package com.bivashy.javafx.authentication.database;

import com.bivashy.javafx.authentication.model.User;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class MemoryUserDatabase {

    private final List<User> users = new ArrayList<>();
    private final MessageDigest hashing;
    private int idCounter = 0;

    public MemoryUserDatabase() {
        try {
            this.hashing = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(String login, String password) {
        String hashedPassword = hash(password);
        Optional<User> foundUser = users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
        return foundUser.filter(user -> hashedPassword.equals(user.getHashedPassword())).isPresent();
    }

    public boolean register(String login, String password) {
        String hashedPassword = hash(password);
        Optional<User> foundUser = users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
        if (foundUser.isPresent())
            return false;
        User user = new User(idCounter++, login, hashedPassword);
        System.out.println(user);
        users.add(user);
        return true;
    }

    private String hash(String password) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] hashedPassword = hashing.digest(passwordBytes);
        return new BigInteger(1, hashedPassword).toString(16).toUpperCase();
    }

}
