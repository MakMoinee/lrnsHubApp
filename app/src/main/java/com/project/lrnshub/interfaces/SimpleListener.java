package com.project.lrnshub.interfaces;

import com.project.lrnshub.models.LocalApps;
import com.project.lrnshub.models.Users;

import java.util.List;

public interface SimpleListener {
    default void onSuccess() {
        /**
         * Default implementation
         */
    }

    default void onSuccessUserList(List<Users> usersList) {
        /**
         * Default implementation
         */
    }

    default void onSuccessLocalApps(LocalApps apps){
        /**
         * Default implementation
         */
    }

    default void onError(Exception e) {
        /**
         * Default Implementation
         */
    }



}
