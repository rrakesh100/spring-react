package com.educative.security;

import com.educative.dao.Book;

public interface Ping {

    public Book saveBook();

    public Book readBook(String bookName);
}
