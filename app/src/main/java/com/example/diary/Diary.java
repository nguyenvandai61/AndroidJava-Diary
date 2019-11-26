package com.example.diary;

import java.io.Serializable;

public class Diary implements Serializable {
    private String id;
    private String mDate;
    private String mTime;
    private String mTitle;
    private String mContent;
    private String mColor;
    private String mPublisher;



    public Diary(String id, String mDate, String mTime, String mTitle,
                 String mContent, String mPublisher) {
        this.id = id;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mColor = "#FFAA00";
        this.mPublisher = mPublisher;
    }

    public Diary(String id, String mDate, String mTime, String mTitle, String mContent,
                 String mColor, String mPublisher) {
        this(id, mDate, mTime, mTitle, mContent, mPublisher);
        this.mColor = mColor;
    }

    public Diary() {

    }
    public String getId() {return id;}
    public void setId(String id) {
        this.id = id;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public void setmPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "id='" + id + '\'' +
                "mDate='" + mDate + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mColor='" + mColor + '\'' +
                '}';
    }
    public void getValue(Diary diary) {
        this.id = diary.getId();
        this.mTitle = diary.getmTitle();
        this.mDate = diary.getmDate();
        this.mTime = diary.getmTime();
        this.mColor = diary.getmColor();
        this.mContent = diary.getmContent();
        this.mPublisher = diary.getmPublisher();
    }
}
