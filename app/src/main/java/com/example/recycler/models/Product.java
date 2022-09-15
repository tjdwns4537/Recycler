package com.example.recycler.models;

public class Product {
    String Name;
    String Cost;
    String Notification;
    int Pic;

    public Product(String name, String cost, String notification, int pic) {
        Name = name;
        Cost = cost;
        Notification = notification;
        Pic = pic;
    }

    public int getPic(){
        return Pic;
    }
    public void setPic(int pic){
        Pic = pic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }
}