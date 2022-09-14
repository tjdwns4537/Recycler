package com.example.recycler.models;

public class StoreModel {

    public String title = "";
    public String content = "";
    public String uid = "";
    public String time = "";
    public String photo = "";
    public String photoName = "";
    public String category = "";

    public StoreModel() {

    }

    public StoreModel(String title, String content, String uid, String time, String photo, String photoName, String category) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.time = time;
        this.photo = photo;
        this.photoName = photoName;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
