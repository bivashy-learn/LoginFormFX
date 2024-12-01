package com.bivashy.javafx.authentication.database;

import com.bivashy.javafx.authentication.database.repository.UserRepository;
import com.bivashy.javafx.authentication.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class JDBCUserAuthentication implements UserAuthentication {

    private final MessageDigest hashing;
    private final UserRepository userRepository;

    public JDBCUserAuthentication(UserRepository userRepository) {
        this.userRepository = userRepository;
        try {
            this.hashing = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean login(String login, String password) {
        String hashedPassword = this.hash(hashing, password);
        Optional<User> foundUser = findUser(login);
        return foundUser.filter(user -> hashedPassword.equals(user.getHashedPassword())).isPresent();
    }

    @Override
    public boolean register(String login, String password) {
        String hashedPassword = hash(hashing, password);
        Optional<User> foundUser = findUser(login);
        if (foundUser.isPresent())
            return false;
        User user = new User(0, login, hashedPassword);
        createUser(user);
        return true;
    }

    private void createUser(User user) {
        userRepository.save(user, UserRepository.INSERT_QUERY);
    }

    private Optional<User> findUser(String login) {
        return userRepository.findOneByColumn("login", login);
    }

}
