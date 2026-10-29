package ru.Vlad.NauJava.entity;

import java.io.File;

public class FileItem {
    private Long id;
    private String originalName;
    private String downloadCode;
    private Long size;
    private String uploadDate;

    public FileItem(){
    }

    public FileItem(Long id, String originalName, String downloadCode, Long size, String uploadDate) {
        this.id = id;
        this.originalName = originalName;
        this.downloadCode = downloadCode;
        this.size = size;
        this.uploadDate = uploadDate;
    }

    public Long getId() {
        return id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getDownloadCode() {
        return downloadCode;
    }

    public Long getSize() {
        return size;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setDownloadCode(String downloadCode) {
        this.downloadCode = downloadCode;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "Файл [ID: " + id +
                ", Имя: '" + originalName + '\'' +
                ", Размер: " + size + " байт" +
                ", Код скачивания: '" + downloadCode + '\'' +
                ", Дата: " + uploadDate + "]";
    }
}
