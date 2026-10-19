package com.bookfinder.backend;


public class Book{
    // Declare the fields
    private String title;
    private String author;
    private String description;
    private String thumbnail;
    private String id;


    //Constructor method
    public Book(String title, String author, String description, String thumbnail, String id ){
        this.title = title;
        this.author = author;
        this.description = description;
        this.thumbnail = thumbnail;
        this.id = id;
        
    }

    // Get method for all our fields
    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getDescription(){
        return description;
    }

    public String getThumbnail(){
        return thumbnail;
    }

    public String getId(){
        return id;
    }







}

