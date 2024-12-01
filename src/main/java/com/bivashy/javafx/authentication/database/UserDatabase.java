package com.bivashy.javafx.authentication.database;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public interface UserDatabase {

    boolean login(String login, String password);

    boolean register(String login, String password);

    default String hash(MessageDigest hashing, String password) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] hashedPassword = hashing.digest(passwordBytes);
        return new BigInteger(1, hashedPassword).toString(16).toUpperCase();
    }

}
