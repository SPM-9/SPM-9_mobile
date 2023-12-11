package com.fxxkywcx.nostudy.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {
    private int uid;
    private String userName;
    private String password;
    private String nickName;
    private String email;
    private int money;
    private boolean isChosenCourse;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isChosenCourse() {
        return isChosenCourse;
    }

    public void setChosenCourse(boolean chosenCourse) {
        isChosenCourse = chosenCourse;
    }

    public UserEntity(int uid, String userName, String password, String nickName, String email, int money, boolean isChosenCourse) {
        this.uid = uid;
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.money = money;
        this.isChosenCourse = isChosenCourse;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid=" + uid +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", money=" + money +
                ", isChosenCourse=" + isChosenCourse +
                '}';
    }
}
