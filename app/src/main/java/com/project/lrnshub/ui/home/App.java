package com.project.lrnshub.ui.home;


import android.graphics.drawable.Drawable;

public class App {

    private Drawable image;
    private String title;
    private String packageN;

    public App(Drawable image, String title, String packageN) {
        this.image = image;
        this.title = title;
        this.packageN = packageN;
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
