package com.project.lrnshub.ui.activitylog;


import android.graphics.drawable.Drawable;

public class ItemActivity {

    private Drawable image;
    private String title;
    private String packageN;
    private String time;
    private double seconds;

    public ItemActivity(Drawable image, String title, String packageN, String time, double seconds) {
        this.image = image;
        this.title = title;
        this.packageN = packageN;
        this.time = time;
        this.seconds = seconds;
    }

    public double getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getPackageN() {
        return packageN;
    }

    public void setPackageN(String packageN) {
        this.packageN = packageN;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
