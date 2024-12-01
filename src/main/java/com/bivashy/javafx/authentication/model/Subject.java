package com.bivashy.javafx.authentication.model;

import com.bivashy.javafx.authentication.util.Lazy;

import java.util.List;

public class Subject {

    private final String name;
    private final Lazy<List<Assignment>> assigmentsLazy;

    public Subject(String name, Lazy<List<Assignment>> assigmentsLazy) {
        this.name = name;
        this.assigmentsLazy = assigmentsLazy;
    }

    public String getName() {
        return name;
    }

    public List<Assignment> getAssignmentsLazy() {
        return assigmentsLazy.get();
    }

}
