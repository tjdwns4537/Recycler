package com.example.recycler.models;

public class BoardModel {
    public String title = "";
    public String content = "";
    public String uid = "";
    public String time = "";
    public String photo;
    public String photoName;

    public BoardModel() {

    }

    public BoardModel(String title, String content, String uid, String time, String photo) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.time = time;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoName() {
        return photo;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }

    public String getTime() {
        return time;
    }
}
