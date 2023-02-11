package com.project.lrnshub.interfaces;

import com.project.lrnshub.ui.home.App;

public interface AdapterListener {
    default void onItemClickListener(int position) {
        /**
         * Default Implementation
         */
    }

    default void onItemWithSelectClickListener(App app, boolean isSelected) {
        /**
         * Default Implementation
         */
    }

    default void onItemWithSelectClickListener(int position,App app, boolean isSelected) {
        /**
         * Default Implementation
         */
    }

    default void onEnableButton(boolean isEnable) {
        /**
         * Default Implementation
         */
    }
}
