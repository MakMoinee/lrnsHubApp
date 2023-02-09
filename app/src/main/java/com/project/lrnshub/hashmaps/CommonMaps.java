package com.project.lrnshub.hashmaps;

import com.project.lrnshub.models.LocalApps;

import java.util.HashMap;
import java.util.Map;

public class CommonMaps {

    public static Map<String, Object> convertLocalAppToMap(LocalApps apps) {
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("packages", apps.getPackages());
        myMap.put("rawApp", apps.getRawApp());
        myMap.put("userID", apps.getUserID());
        return myMap;
    }
}
