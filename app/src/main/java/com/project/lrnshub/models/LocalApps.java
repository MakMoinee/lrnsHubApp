package com.project.lrnshub.models;

import android.graphics.drawable.Drawable;

import com.project.lrnshub.ui.home.App;

import java.util.List;

import lombok.Data;

@Data
public class LocalApps {
    String userID;
    List<String> packages;
    List<String> originalPackages;
    String rawApp;
}
