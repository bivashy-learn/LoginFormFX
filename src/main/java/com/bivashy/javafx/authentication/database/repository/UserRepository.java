package com.bivashy.javafx.authentication.database.repository;

import com.bivashy.javafx.authentication.database.JDBCConnectionPool;
import com.bivashy.javafx.authentication.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends BaseJDBCRepository<User, Long> {

    public static final String CREATE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                login VARCHAR(255) NOT NULL UNIQUE,
                hashed_password VARCHAR(255) NOT NULL
            );
            """;
    public static final String INSERT_QUERY = "INSERT INTO users (login, hashed_password) VALUES (?, ?);";

    public UserRepository(JDBCConnectionPool connectionPool) {
        super(connectionPool, "users", "id");
        execute(CREATE_TABLE_QUERY);
    }

    @Override
    protected User mapRowToEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String login = resultSet.getString("login");
        String hashedPassword = resultSet.getString("hashed_password");
        return new User(id, login, hashedPassword);
    }

    @Override
    protected void mapEntityToPreparedStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getHashedPassword());
    }

}
