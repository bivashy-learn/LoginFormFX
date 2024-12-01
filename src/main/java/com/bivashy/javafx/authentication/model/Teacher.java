package com.bivashy.javafx.authentication.model;

import com.bivashy.javafx.authentication.util.Lazy;

    public class Teacher {
        private final Lazy<User> user;

        public Teacher(Lazy<User> user) {
            this.user = user;
        }

        public User getUser() {
            return user.get();
        }

    }
