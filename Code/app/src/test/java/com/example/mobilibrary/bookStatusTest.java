package com.example.mobilibrary;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class bookStatusTest {
    private Book mockBookTest() {
        Book mockBook = new Book("Harry Potter","1234567890123","J.K Rowling","available",null);
        return mockBook;
    }

    @Test
    void testStatus() {
        Book book = mockBookTest();
        assertEquals("available",book.getStatus());
    }

    @Test
    void testChangeStatus() {
        Book book = mockBookTest();
        book.setStatus("borrowed");
        assertEquals("borrowed",book.getStatus());
        book.setStatus("available");
        assertEquals("available",book.getStatus());
    }
}
