package com.example.diary;

public class History {
    private String id;
    private String mDate;
    private String mTime;
    private String mContent;
    private String mPublisher;



    public History(String id, String mDate, String mTime,
                 String mContent, String mPublisher) {
        this.id = id;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mContent = mContent;
        this.mPublisher = mPublisher;
    }


    public History() {

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



    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public void setmPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    @Override
    public String toString() {
        return "History{" +
                "id='" + id + '\'' +
                "mDate='" + mDate + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }
}
