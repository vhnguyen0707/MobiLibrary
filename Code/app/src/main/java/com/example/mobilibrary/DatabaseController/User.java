package com.example.mobilibrary.DatabaseController;

public class User {
    private final String username;
    private final String email;
    private final String name;


    /**
     * Full constructor
     *
     * @param username:  unique username
     * @param email: user's email address
     * @param name: user's  name
     */
    public User(String username, String email, String name) {
        if (username == null || email == null || name == null ) {
            throw new IllegalArgumentException("These fields must not be null.");
        }
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return name;
    }
}

