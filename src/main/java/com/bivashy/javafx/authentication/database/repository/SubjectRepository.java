package com.bivashy.javafx.authentication.database.repository;

import com.bivashy.javafx.authentication.database.JDBCConnectionPool;
import com.bivashy.javafx.authentication.model.Assignment;
import com.bivashy.javafx.authentication.model.Subject;
import com.bivashy.javafx.authentication.util.Lazy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*

 */
public class SubjectRepository extends BaseJDBCRepository<Subject, Integer> {

    public static final String CREATE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS subjects (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) NOT NULL UNIQUE
            );
            """;
    public static final String INSERT_QUERY = "INSERT INTO subjects (name) VALUES (?);";
    private final AssignmentRepository assignmentRepository;

    public SubjectRepository(JDBCConnectionPool connectionPool, AssignmentRepository assignmentRepository) {
        super(connectionPool, "subjects", "id");
        this.assignmentRepository = assignmentRepository;
        executeQuery(CREATE_TABLE_QUERY);
    }

    @Override
    protected Subject mapRowToEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");

        Lazy<List<Assignment>> assignmentsLazy = Lazy.lazy(() -> assignmentRepository.findBySubjectId(id));

        return new Subject(name, assignmentsLazy);
    }

    @Override
    protected void mapEntityToPreparedStatement(Subject subject, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, subject.getName());
    }

}
