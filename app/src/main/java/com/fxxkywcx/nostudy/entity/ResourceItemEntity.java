package com.fxxkywcx.nostudy.entity;

import android.widget.ImageView;
import android.widget.TextView;

public class ResourceItemEntity {
    private Integer resID;
    private String title;
    private String body;
    private String filename;
    private byte[] file;
    private Long fileSize;

    public Integer getResID() {
        return resID;
    }

    public void setResID(Integer resID) {
        this.resID = resID;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public ResourceItemEntity(Integer resID, String title, String body, String filename, byte[] file, Long fileSize) {
        this.resID = resID;
        this.title = title;
        this.body = body;
        this.filename = filename;
        this.file = file;
        this.fileSize = fileSize;
    }

    public ResourceItemEntity() {
    }
}
