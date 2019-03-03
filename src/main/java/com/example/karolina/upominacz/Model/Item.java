package com.example.karolina.upominacz.Model;

public class Item {

    private String name;
    private String moreInfo;
    private String dateItemAdded;
    private int id;

    public Item() {
    }

    public Item(String name, String moreInfo, String dateItemAdded, int id) {
        this.name = name;
        this.moreInfo = moreInfo;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
