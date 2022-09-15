package com.example.recycler.models;

public class MainPageData {
    private String storeTitle = "";
    private String storeTime = "";
    private String storePrice = "";
    private String storePhoto = "";

    private String boardTitle = "";
    private String boardContent = "";
    private String boardTime = "";
    private String boardPhoto = "";

    private int type; // 1: store 2: board

    public MainPageData(String storeTitle, String storeTime, String storePrice, String storePhoto, int type) {
        this.storeTitle = storeTitle;
        this.storeTime = storeTime;
        this.storePrice = storePrice;
        this.storePhoto = storePhoto;
        this.type = type;
    }

    public MainPageData(int type, String boardTitle, String boardContent, String boardTime, String boardPhoto) {
        this.type = type;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardTime = boardTime;
        this.boardPhoto = boardPhoto;
    }


    public String getStorePhoto() {
        return storePhoto;
    }

    public void setStorePhoto(String storePhoto) {
        this.storePhoto = storePhoto;
    }

    public String getBoardPhoto() {
        return boardPhoto;
    }

    public void setBoardPhoto(String boardPhoto) {
        this.boardPhoto = boardPhoto;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStoreTitle() {
        return storeTitle;
    }

    public void setStoreTitle(String storeTitle) {
        this.storeTitle = storeTitle;
    }

    public String getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(String storeTime) {
        this.storeTime = storeTime;
    }

    public String getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(String storePrice) {
        this.storePrice = storePrice;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getBoardTime() {
        return boardTime;
    }

    public void setBoardTime(String boardTime) {
        this.boardTime = boardTime;
    }
}
