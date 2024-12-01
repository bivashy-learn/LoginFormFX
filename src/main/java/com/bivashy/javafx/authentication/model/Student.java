package com.bivashy.javafx.authentication.model;

import com.bivashy.javafx.authentication.util.Lazy;

import java.util.List;

public class Student {

    private final Lazy<User> user;
    private final Lazy<List<Subject>> subjects;

    public Student(Lazy<User> user, Lazy<List<Subject>> subjects) {
        this.user = user;
        this.subjects = subjects;
    }

    public User getUser() {
        return user.get();
    }

    public List<Subject> getSubjects() {
        return subjects.get();
    }

}
