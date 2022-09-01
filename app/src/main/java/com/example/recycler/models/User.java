package com.example.recycler.models;

import java.io.Serializable;

public class User implements Serializable {
    private String name, imageUrl, email, token, id;

    public User() {
    }

    public User(String name, String imageUrl, String email, String id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
        this.id = id;
    }

    public User(String name, String imageUrl, String email) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
