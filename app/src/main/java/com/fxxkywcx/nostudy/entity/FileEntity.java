package com.fxxkywcx.nostudy.entity;

import java.io.Serializable;
import java.util.Arrays;

public class FileEntity implements Serializable {
    private String fileName;
    private byte[] file;

    public FileEntity(String fileName, byte[] file) {
        this.fileName = fileName;
        this.file = file;
    }

    public FileEntity() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + file.length +
                '}';
    }
}
