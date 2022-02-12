package com.Duong.crowdshipping.model;

public class Noti {
    String MSG, PostID,Time,NotiID,isseen;
    public Noti(String MSG, String PostID, String Time, String NotiID, String isseen){
        this.MSG = MSG;
        this.PostID = PostID;
        this.Time = Time;
        this.NotiID = NotiID;
        this.isseen = isseen;
    }
    public Noti(){

    }

    public String getSeen() {
        return isseen;
    }

    public void setSeen(String seen) {
        isseen = seen;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getNotiID() {
        return NotiID;
    }

    public void setNotiID(String notiID) {
        NotiID = notiID;
    }
}
