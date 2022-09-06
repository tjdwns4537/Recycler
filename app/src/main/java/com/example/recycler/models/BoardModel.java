package com.example.recycler.models;

public class BoardModel {
     public String title = "";
     public String content = "";
     public String uid = "";
     public String time = "";

    public BoardModel() {

    }

    public BoardModel(String title, String content, String uid, String time) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.time = time;
    }

    public  String getTitle() {
        return title;
    }

    public  String getContent() {
        return content;
    }

    public  String getUid() {
        return uid;
    }

    public  String getTime() {
        return time;
    }
}
