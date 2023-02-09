package com.project.lrnshub.interfaces;

public interface AdapterListener {
    default void onItemClickListener(int position){
        /**
         * Default Implementation
         */
    }

    default  void onEnableButton(boolean isEnable){
        /**
         * Default Implementation
         */
    }
}
