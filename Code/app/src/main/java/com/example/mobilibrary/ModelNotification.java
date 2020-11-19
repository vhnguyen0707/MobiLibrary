package com.example.mobilibrary;

public class ModelNotification {

    String otherUser;
    String user;
    String notification;
    String type;

    //empty constructor is required for firebase
    public ModelNotification() {

    }

    public ModelNotification(String otherUser, String user, String notification, String type) {
        this.otherUser = otherUser;
        this.user = user;
        this.notification = notification;
        this.type = type;

    }

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
