package com.project.lrnshub.ui.focus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.lrnshub.FragmentToActivity;
import com.project.lrnshub.R;
import com.project.lrnshub.databinding.FragmentFocusBinding;
import com.project.lrnshub.databinding.FragmentHomeBinding;
import com.project.lrnshub.ui.home.App;
import com.project.lrnshub.ui.home.AppAdapter;
import com.project.lrnshub.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FocusFragment extends Fragment {

    private FragmentFocusBinding binding;
    private FragmentToActivity mCallback;
    Boolean isFocusMode = false, onClickApp = false;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFocusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.switchLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendData(isChecked);
            }
        });

        return root;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }


    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    private void sendData(Boolean isFocusMode) {
        mCallback.check(isFocusMode);
    }

}
