package com.example.mobilibrary;

import com.example.mobilibrary.DatabaseController.User;

/**
 * Used for asynchronous tasks such as onSuccessListener and onCompleteListener
 */
public interface Callback {
    void onCallback(User user);
}

