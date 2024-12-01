package com.bivashy.javafx.authentication.database.repository;

import com.bivashy.javafx.authentication.database.JDBCConnectionPool;
import com.bivashy.javafx.authentication.model.Assignment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AssignmentRepository extends BaseJDBCRepository<Assignment, Long> {

    public static final String CREATE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS assignments (
                id SERIAL PRIMARY KEY,
                type VARCHAR(50) NOT NULL CHECK (type IN ('NOT_PROVIDED', 'ABSENT', 'SCORE')),
                score INT NOT NULL DEFAULT 0,
                subject_id BIGINT NOT NULL,
                FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE
            );
            """;
    public static final String INSERT_QUERY = "INSERT INTO assignments (type, score, subject_id) VALUES (?, ?, ?);";

    public AssignmentRepository(JDBCConnectionPool connectionPool) {
        super(connectionPool, "assignments", "id");
        executeQuery(CREATE_TABLE_QUERY);
    }

    @Override
    protected Assignment mapRowToEntity(ResultSet resultSet) throws SQLException {
        Assignment.Type type = Assignment.Type.valueOf(resultSet.getString("type"));
        int score = resultSet.getInt("score");
        int subjectId = resultSet.getInt("subject_id");
        return new Assignment(subjectId, type, score);
    }

    @Override
    protected void mapEntityToPreparedStatement(Assignment assignment, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, assignment.getType().name());
        preparedStatement.setInt(2, assignment.getScore());
        preparedStatement.setLong(3, assignment.getSubjectId());
    }

    public List<Assignment> findBySubjectId(int subjectId) {
        return findAllByColumn("subject_id", subjectId);
    }

}
