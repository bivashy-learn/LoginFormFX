package com.bivashy.javafx.authentication.database;

import com.bivashy.javafx.authentication.database.repository.AssignmentRepository;
import com.bivashy.javafx.authentication.database.repository.StudentRepository;
import com.bivashy.javafx.authentication.database.repository.SubjectRepository;
import com.bivashy.javafx.authentication.database.repository.TeacherRepository;
import com.bivashy.javafx.authentication.database.repository.UserRepository;

public class JDBCDatabase {

    private final JDBCConnectionPool connectionPool;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;

    public JDBCDatabase(JDBCConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.userRepository = new UserRepository(connectionPool);
        this.teacherRepository = new TeacherRepository(connectionPool, userRepository);
        this.assignmentRepository = new AssignmentRepository(connectionPool);
        this.subjectRepository = new SubjectRepository(connectionPool, assignmentRepository);
        this.studentRepository = new StudentRepository(connectionPool, userRepository, subjectRepository);
        subjectRepository.createTableIfNotExists();
        assignmentRepository.createTableIfNotExists();
        studentRepository.createTableIfNotExists();
    }

    public JDBCConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public TeacherRepository getTeacherRepository() {
        return teacherRepository;
    }

    public AssignmentRepository getAssignmentRepository() {
        return assignmentRepository;
    }

    public SubjectRepository getSubjectRepository() {
        return subjectRepository;
    }

    public StudentRepository getStudentRepository() {
        return studentRepository;
    }

}
