package com.aadevelopers.onlyscratchiapp.models;

public class User {
    private String name;
    private String number;
    private String email;
    private final String image;
    private String points;
    private final String isBLocked;
    private final String referCode;
    private final String userReferCode;

    public User(String name, String number, String email, String points, String referCode, String image, String isBLocked, String userReferCode) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.points = points;
        this.referCode = referCode;
        this.image = image;
        this.isBLocked = isBLocked;
        this.userReferCode = userReferCode;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getIsBLocked() {
        return isBLocked;
    }

    public String getReferCode() {
        return referCode;
    }

    public String getUserReferCode() {
        return userReferCode;
    }

}
