package com.bivashy.javafx.authentication.database.repository;

import com.bivashy.javafx.authentication.database.JDBCConnectionPool;
import com.bivashy.javafx.authentication.model.Teacher;
import com.bivashy.javafx.authentication.model.User;
import com.bivashy.javafx.authentication.util.Lazy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TeacherRepository extends BaseJDBCRepository<Teacher, Long> {

    public static final String CREATE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS teachers (
                id SERIAL PRIMARY KEY,
                user_id INT NOT NULL UNIQUE,
                FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
            );
            """;
    public static final String INSERT_QUERY = "INSERT INTO teachers (user_id) VALUES (?);";
    private final UserRepository userRepository;

    public TeacherRepository(JDBCConnectionPool connectionPool, UserRepository userRepository) {
        super(connectionPool, "teachers", "id");
        this.userRepository = userRepository;
        executeQuery(CREATE_TABLE_QUERY);
    }

    @Override
    protected Teacher mapRowToEntity(ResultSet resultSet) throws SQLException {
        long userId = resultSet.getLong("user_id");

        Lazy<User> userLazy = Lazy.lazy(() ->
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found for teacher"))
        );

        return new Teacher(userLazy);
    }

    @Override
    protected void mapEntityToPreparedStatement(Teacher teacher, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, teacher.getUser().getId());
    }

    public void save(Teacher teacher) {
        String insertQuery = "INSERT INTO teachers (user_id) VALUES (?)";
        super.save(teacher, insertQuery);
    }

    public Optional<Teacher> findByUserId(long userId) {
        return findOneByColumn("user_id", userId);
    }

}
