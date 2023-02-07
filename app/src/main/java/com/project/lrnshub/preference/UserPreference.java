package com.project.lrnshub.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.lrnshub.models.Users;

public class UserPreference {
    Context mContext;
    SharedPreferences pref;

    public UserPreference(Context mContext) {
        this.mContext = mContext;
        this.pref = this.mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public void saveLogin(Users users) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", users.getEmail());
        editor.putString("password", users.getPassword());
        editor.putInt("userType", users.getUserType());
        editor.commit();
        editor.apply();
    }

    public Users getUser() {
        Users users = new Users();
        String email = pref.getString("email", "");
        String password = pref.getString("password", "");
        int userType = pref.getInt("userType", 0);
        if (email != "" && password != "" && userType != 0) {
            users.setEmail(email);
            users.setPassword(password);
            return users;
        } else {
            return null;
        }


    }

    public void clearAll() {
        pref.getAll().clear();
    }
}
