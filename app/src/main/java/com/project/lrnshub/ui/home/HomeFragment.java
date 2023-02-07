package com.project.lrnshub.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.lrnshub.FragmentToActivity;
import com.project.lrnshub.R;
import com.project.lrnshub.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int REQUEST_CODE = 0;
    HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    AppAdapter appAdapter1, appAdapter2;
    List<App> appList;
    List<App> installedApps;
    List<App> notInstalledApps;
    private FragmentToActivity mCallback;
    Boolean isFocusMode = false, onClickApp = false;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;

        initializeList();
        sortList();
        installedApps.sort(Comparator.comparing(App::getTitle));
        notInstalledApps.sort(Comparator.comparing(App::getTitle));
        appAdapter1 = new AppAdapter(requireContext(), installedApps);
        appAdapter2 = new AppAdapter(requireContext(), notInstalledApps);

        appAdapter1.setOnItemClickListener((item, v) ->
                openApplication(getContext(), item.getPackageN()));

        appAdapter2.setOnItemClickListener((item, v) ->
                openApplication(getContext(), item.getPackageN()));

        binding.hideButton.setOnClickListener(v -> {
                    if (binding.recyclerViewNotInstalled.getVisibility() == View.VISIBLE) {
                        TransitionManager.beginDelayedTransition(binding.recyclerViewNotInstalled);
                        binding.recyclerViewNotInstalled.setVisibility(View.INVISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition(binding.recyclerViewNotInstalled);
                        binding.recyclerViewNotInstalled.setVisibility(View.VISIBLE);
                    }
                }
        );

        binding.recyclerViewInstalled.setLayoutManager(new GridLayoutManager(requireContext(), 4));

        binding.recyclerViewInstalled.setAdapter(appAdapter1);

        binding.recyclerViewNotInstalled.setLayoutManager(new GridLayoutManager(requireContext(), 4));

        binding.recyclerViewNotInstalled.setAdapter(appAdapter2);

        binding.recyclerViewNotInstalled.setVerticalScrollBarEnabled(true);

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString().toLowerCase());
            }
        });

        return root;
    }

    private void sortList() {
        installedApps = new ArrayList<>();
        notInstalledApps = new ArrayList<>();
        for (App item : appList) {
            if (isPackageInstalled(item.getPackageN())) {
                installedApps.add(item);
            } else {
                notInstalledApps.add(item);
            }
        }

    }

    private void initializeList() {
        appList = new ArrayList<>();
        addItem(appList, R.drawable.icon1, "Merriam webster", "com.merriamwebster");
        addItem(appList, R.drawable.icon2, "Google Docs", "com.google.android.apps.docs.editors.docs");
        addItem(appList, R.drawable.icon3, "Google Excel", "com.google.android.apps.docs.editors.sheets");
        addItem(appList, R.drawable.icon4, "Evernote", "com.evernote");
        addItem(appList, R.drawable.icon5, "Google Classroom", "com.google.android.apps.classroom");
        addItem(appList, R.drawable.icon6, "Photomath", "com.microblink.photomath");
        addItem(appList, R.drawable.icon7, "WPS", "cn.wps.moffice_eng");
        addItem(appList, R.drawable.icon9, "Google Drive", "com.google.android.apps.docs");
        addItem(appList, R.drawable.brainly, "Brainly", "co.brainly");
        addItem(appList, R.drawable.canvas, "Canvas", "com.instructure.candroid");
        addItem(appList, R.drawable.classcraft, "Classcraft", "com.classcraft.android");
        addItem(appList, R.drawable.classdojo, "ClassDojo", "com.classdojo.android");
        addItem(appList, R.drawable.dropbox, "Dropbox", "com.dropbox.android");
        addItem(appList, R.drawable.duolingo, "Duolingo", "com.duolingo");
        addItem(appList, R.drawable.edpuzzle, "Edpuzzle", "com.edpuzzle.student");
        addItem(appList, R.drawable.facebook, "Facebook", "com.facebook.katana");
        addItem(appList, R.drawable.falou, "Falou", "com.moymer.falou");
        addItem(appList, R.drawable.hangouts, "Google Chats", "com.google.android.apps.dynamite");
        addItem(appList, R.drawable.kahoot, "Kahoot", "no.mobitroll.kahoot.android");
        addItem(appList, R.drawable.learnkorea, "Learnkorea", "com.breboucas.coreanoparaviaja");
        addItem(appList, R.drawable.lingomate, "Lingomate", "com.deer.cc");
        addItem(appList, R.drawable.mathpid, "Mathpid", "com.wjthinkbig.mathpid");
        addItem(appList, R.drawable.mentalmath, "MentalMath", "com.monsterschool.colorcal");
        addItem(appList, R.drawable.messenger, "Messenger", "com.facebook.orca");
        addItem(appList, R.drawable.peak, "Peak", "com.brainbow.peak.app");
        addItem(appList, R.drawable.plickers, "Plickers", "com.plickers.client.android");
        addItem(appList, R.drawable.prodigy, "Prodigy", "com.prodigygame.prodigy");
        addItem(appList, R.drawable.quizalize, "Quazalize", "com.zzish.quizalizescan");
        addItem(appList, R.drawable.quizizz, "Quizizz", "com.quizizz_mobile");
        addItem(appList, R.drawable.quizlet, "Quizlet", "com.quizlet.quizletandroid");
        addItem(appList, R.drawable.seesaw, "Seesaw", "seesaw.shadowpuppet.co.classroom");
        addItem(appList, R.drawable.trello, "Trello", "com.trello");
        addItem(appList, R.drawable.yeaetalk, "Yeaetalk", "com.imback.yeetalk");
    }


    private void addItem(List<App> appList, Integer drawable, String name, String packageN) {
        appList.add(new App(ContextCompat.getDrawable(getActivity(), drawable), name, packageN));
    }

    public void openApplication(Context context, String packageN) {
        onClickApp = true;
        mCallback.checkApp(onClickApp);
        onClickApp = false;

        Intent i = context.getPackageManager().getLaunchIntentForPackage(packageN);
        if (i != null) {
            startActivityForResult(i, REQUEST_CODE);
        } else {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageN)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageN)));
            }
        }
    }

    private boolean isPackageInstalled(String packageName) {
        try {
            getContext().getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void filter(String text) {
        List<App> temp1 = new ArrayList();
        List<App> temp2 = new ArrayList();
        for (App d : appList) {
            if (d.getTitle().toLowerCase().contains(text)) {
                if (isPackageInstalled(d.getPackageN())) {
                    temp1.add(d);
                } else {
                    temp2.add(d);
                }
            }

        }
        //update recyclerview
        appAdapter1.updateList(temp1);
        appAdapter2.updateList(temp2);
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


}
