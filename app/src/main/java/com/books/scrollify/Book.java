package com.books.scrollify;

public class Book {
    public String Title, AuthorName, ISBN;
    public Integer Year;
    public int Image;

    public Book(){}
    public Book(String title, String author, String ISBN, Integer year, int image) {
        Title = title;
        AuthorName = author;
        this.ISBN = ISBN;
        Year = year;
        Image = image;
    }
}
