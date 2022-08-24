package com.example.recycler;

public class BearItem {
    /* 아이템의 정보를 담기 위한 클래스 */
    String num;
    String name;
    String content;
    int likeit;
    int resId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeit() {
        return likeit;
    }

    public void setLikeit(int likeit) {
        this.likeit = likeit;
    }

    public BearItem(String num, String name, int resId, String content, int likeit) {
        this.num = num;
        this.name = name;
        this.content = content;
        this.likeit = likeit;
        this.resId = resId;

    }

    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }
}
