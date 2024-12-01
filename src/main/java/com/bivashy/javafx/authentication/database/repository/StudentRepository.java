package com.bivashy.javafx.authentication.database.repository;

import com.bivashy.javafx.authentication.database.JDBCConnectionPool;
import com.bivashy.javafx.authentication.model.Student;
import com.bivashy.javafx.authentication.model.Subject;
import com.bivashy.javafx.authentication.model.User;
import com.bivashy.javafx.authentication.util.Lazy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentRepository extends BaseJDBCRepository<Student, Long> {

    public static final String CREATE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS students (
                id SERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
            );
            """;
    public static final String INSERT_QUERY = "INSERT INTO students (user_id) VALUES (?);";
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public StudentRepository(JDBCConnectionPool connectionPool, UserRepository userRepository, SubjectRepository subjectRepository) {
        super(connectionPool, "students", "id");
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        executeQuery(CREATE_TABLE_QUERY);
    }

    @Override
    protected Student mapRowToEntity(ResultSet resultSet) throws SQLException {
        long userId = resultSet.getInt("user_id");

        Lazy<User> userLazy = Lazy.lazy(() ->
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"))
        );

        Lazy<List<Subject>> subjectsLazy = Lazy.lazy(subjectRepository::findAll);

        return new Student(userLazy, subjectsLazy);
    }

    @Override
    protected void mapEntityToPreparedStatement(Student student, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, student.getUser().getId());
    }

    public Optional<Student> findByUserId(long userId) {
        return findOneByColumn("user_id", userId);
    }

}
