package com.fxxkywcx.nostudy.entity;

import android.location.Location;

import java.util.Date;

public class UserSignEntity {
    private int signId;
    private int studentId;
    private Date signTime;
    private Location signLocation;


    public UserSignEntity() {
    }

    public UserSignEntity(int studentId, Date signTime, Location signLocation) {
        this.studentId = studentId;
        this.signTime = signTime;
        this.signLocation = signLocation;
    }

    public UserSignEntity(int signId, int studentId, Date signTime, Location signLocation) {
        this.signId = signId;
        this.studentId = studentId;
        this.signTime = signTime;
        this.signLocation = signLocation;
    }

    /**
     * 获取
     * @return studentId
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * 设置
     * @param studentId
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     * 获取
     * @return signTime
     */
    public Date getSignTime() {
        return signTime;
    }

    /**
     * 设置
     * @param signTime
     */
    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    /**
     * 获取
     * @return signLocation
     */
    public Location getSignLocation() {
        return signLocation;
    }

    /**
     * 设置
     * @param signLocation
     */
    public void setSignLocation(Location signLocation) {
        this.signLocation = signLocation;
    }

    public String toString() {
        return "SignEntity{studentId = " + studentId + ", signTime = " + signTime + ", signLocation = " + signLocation + "}";
    }

    /**
     * 获取
     * @return signId
     */
    public int getSignId() {
        return signId;
    }

    /**
     * 设置
     * @param signId
     */
    public void setSignId(int signId) {
        this.signId = signId;
    }
}

