package com.example.myapplication.model;

import java.io.Serializable;

public class Data implements Serializable {
    String title;
    String filePath;
    String imgPath;
    int sortId;
    String userId;
    String fileMd5;
    long bookProgress;
    long importTime;
    
    
    public long getImportTime() {
        return importTime;
    }
    
    public void setImportTime(long importTime) {
        this.importTime = importTime;
    }
    
    
    public long getBookProgress() {
        return bookProgress;
    }
    
    public void setBookProgress(long bookProgress) {
        this.bookProgress = bookProgress;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
