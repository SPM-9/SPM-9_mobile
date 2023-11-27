package com.fxxkywcx.nostudy.entity;

import java.io.Serializable;
import java.util.Date;

public class NotificationEntity implements Serializable{
    private Integer notifId;
    private Integer fori_taskId;
    private Integer fori_annId;
    private Integer notifType;
    private String title;
    private String body;
    private Date uploadTime;

    public NotificationEntity() {
    }

    public NotificationEntity(Integer notifType, String title, String body) {
        this.notifType = notifType;
        this.title = title;
        this.body = body;
    }

    public Integer getNotifId() {
        return notifId;
    }

    public void setNotifId(Integer notifId) {
        this.notifId = notifId;
    }

    public Integer getFori_taskId() {
        return fori_taskId;
    }

    public void setFori_taskId(Integer fori_taskId) {
        this.fori_taskId = fori_taskId;
    }

    public Integer getFori_annId() {
        return fori_annId;
    }

    public void setFori_annId(Integer fori_annId) {
        this.fori_annId = fori_annId;
    }

    public Integer getNotifType() {
        return notifType;
    }

    public void setNotifType(Integer notifType) {
        this.notifType = notifType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
