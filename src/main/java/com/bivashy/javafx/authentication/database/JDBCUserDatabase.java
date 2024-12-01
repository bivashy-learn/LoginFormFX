package com.bivashy.javafx.authentication.database;

import com.bivashy.javafx.authentication.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class JDBCUserDatabase implements UserDatabase {

    private final String url, username, password;
    private final MessageDigest hashing;

    public JDBCUserDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            this.hashing = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = createConnection(); Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS auth_users (id SERIAL PRIMARY KEY, login VARCHAR(40) UNIQUE NOT NULL, password VARCHAR(512) NOT NULL);");
        } catch (SQLException e) {
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
        try (Connection connection = createConnection(); PreparedStatement preparedStatement =
                connection.prepareStatement("INSERT INTO auth_users (login, password) VALUES (?, ?)")) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getHashedPassword());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<User> findUser(String login) {
        try (Connection connection = createConnection(); PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM auth_users WHERE login = ?;")) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return Optional.empty();
            int id = resultSet.getInt("id");
            String foundLogin = resultSet.getString("login");
            String foundPassword = resultSet.getString("password");
            return Optional.of(new User(id, foundLogin, foundPassword));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
