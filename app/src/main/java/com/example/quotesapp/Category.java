package com.example.quotesapp;

public class Category {
    public int image;
    public String name, content;

    public Category(int image, String name, String content) {
        this.image = image;
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }
}
