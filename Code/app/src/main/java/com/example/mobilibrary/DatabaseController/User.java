package com.example.mobilibrary.DatabaseController;

public class User {
    private final String username;
    private String email;
    private final String fullname;
    private String phoneNo;


    /**
     * Full constructor
     *
     * @param username:  unique username
     * @param email: user's email address
     * @param fullname: user's  name
     * @param phoneNo: user's phone number
     */
    public User(String username, String email, String fullname, String phoneNo) {
        if (username == null || email == null || fullname == null|| phoneNo == null ) {
            throw new IllegalArgumentException("These fields must not be null.");
        }
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phoneNo = phoneNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return fullname;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}

