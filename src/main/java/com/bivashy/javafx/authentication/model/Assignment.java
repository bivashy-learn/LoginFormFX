package com.bivashy.javafx.authentication.model;

public class Assignment {
    private final long subjectId;
    private final Type type;
    private final int score;

    public Assignment(long subjectId, Type type) {
        this(subjectId, type, 0);
    }

    public Assignment(long subjectId, Type type, int score) {
        this.subjectId = subjectId;
        this.type = type;
        this.score = score;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public Type getType() {
        return type;
    }

    public int getScore() {
        return score;
    }

    public enum Type {
        NOT_PROVIDED,
        ABSENT,
        SCORE
    }

}
