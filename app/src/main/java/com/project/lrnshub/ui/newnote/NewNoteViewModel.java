package com.project.lrnshub.ui.newnote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewNoteViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NewNoteViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}