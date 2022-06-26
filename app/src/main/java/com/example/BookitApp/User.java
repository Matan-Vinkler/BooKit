package com.example.BookitApp;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String email;
    private String id;
    private String creditCardId;
    private String ccv;
    private String imgId;

    public User() {
        this.username = "";
        this.email = "";
        this.id = "";
        this.creditCardId = "";
        this.ccv = "";
        this.imgId = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String cvv) {
        this.ccv = cvv;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}
