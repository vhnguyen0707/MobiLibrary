package com.example.mobilibrary;

//Unit test for User

import com.example.mobilibrary.DatabaseController.User;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class UserTest {
    private static final String username ="test123";
    private static final String email = "test123@gmail.com";
    private static final String fullname = "test test";
    private static final String phoneNo = "7801112222";


    private User mockUserTest(){
        return new User(username, email, fullname, phoneNo);
    }

    @Test
    public void test_getUsername(){
        User user = mockUserTest();
        assertEquals(user.getUsername(), "test123");
    }

    @Test
    public void test_getEmail(){
        User user = mockUserTest();
        assertEquals(user.getEmail(),"test123@gmail.com");
    }

    @Test
    public void test_getName(){
        User user = mockUserTest();
        assertEquals(user.getName(),"test test");
    }

    @Test
    public void test_getPhone(){
        User user = mockUserTest();
        assertEquals(user.getPhoneNo(), "7801112222");
    }
}
