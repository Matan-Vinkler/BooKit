package com.example.BookitApp;

import android.graphics.Bitmap;
import android.widget.Button;

public class Book {
    private int price;
    private String title;
    private String subTitle;
    private String imgId;
    private String imgId2;

    public Book() {
        this.price = 0;
        this.title = "";
        this.subTitle = "";
        this.imgId = "";
        this.imgId2 = "";
    }

    public Book(int price, String title, String subTitle, String imgId, String imgId2) {
        this.price = price;
        this.title = title;
        this.subTitle = subTitle;
        this.imgId = imgId;
        this.imgId2 = imgId2;
    }

    public int getPrice () {
            return price;
        }
    public void setPrice (int price) { this.price = price; }

    public String getTitle () {
            return title;
        }
    public void setTitle (String title){ this.title = title; }

    public String getSubTitle () {
            return subTitle;
        }
    public void setSubTitle (String subTitle){ this.subTitle = subTitle; }

    @Override
    public String toString() {
        return "Book{" + "price=" + price + ", title='" + title + '\'' + ", subTitle='" + subTitle + '}';
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImgId2() {
        return imgId2;
    }

    public void setImgId2(String imgId2) {
        this.imgId2 = imgId2;
    }
}
