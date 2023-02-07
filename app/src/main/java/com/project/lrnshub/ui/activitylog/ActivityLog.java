package com.project.lrnshub.ui.activitylog;


import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.lrnshub.R;
import com.project.lrnshub.databinding.ActivityLogBinding;
import com.project.lrnshub.ui.home.App;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityLog extends AppCompatActivity {

    UsageStatsManager usageStatsManager;
    List<UsageStats> usageStatsDailyList;
    List<UsageStats> usageStatsMonthlyList;
    List<UsageStats> usageStatsWeeklyList;
    ActivityLogBinding binding;
    ActivityLogAdapter activityLogAdapter;
    List<ItemActivity> itemList;
    List<App> appList;
    int state = 0;
    Boolean isAlphabetical = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        Calendar cal1 = Calendar.getInstance();
        long endTime1 = cal1.getTimeInMillis();
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        long startTime1 = cal1.getTimeInMillis();

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();

//        Calendar cal2 = Calendar.getInstance();
//        long endTimeWeek = cal2.getTimeInMillis();
//        cal2.add(Calendar.WEEK_OF_YEAR, -1);
//        long startTimeWeek = cal2.getTimeInMillis();

        Calendar cal2 = Calendar.getInstance();
        long endTimeWeek = cal2.getTimeInMillis();
        cal2.set(Calendar.DAY_OF_WEEK, cal2.getFirstDayOfWeek());
        long startTimeWeek = cal2.getTimeInMillis();

        Calendar cal3 = Calendar.getInstance();
        long endTimeMonth = cal3.getTimeInMillis();
        cal3.add(Calendar.DAY_OF_MONTH, 1);
        long startTimeMonth = cal3.getTimeInMillis();


        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        usageStatsDailyList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime1, endTime1);
        usageStatsMonthlyList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY, startTime, endTime);
        usageStatsWeeklyList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, startTime, endTime);


        binding.dailyButton.setOnClickListener(v ->
        {
            binding.dailyButton.setBackgroundResource(R.drawable.customlinebutton_pressed);
            binding.dailyButton.setTextColor(Color.WHITE);
            binding.monthlyButton.setBackgroundResource(R.drawable.customlinebutton2);
            binding.monthlyButton.setTextColor(ContextCompat.getColor(this, R.color.primary));
            binding.weeklyButton.setBackgroundResource(R.drawable.customlinebutton2);
            binding.weeklyButton.setTextColor(ContextCompat.getColor(this, R.color.primary));
            state = 1;
            dailyStats(usageStatsDailyList);
            if (isAlphabetical) {
                itemList.sort(Comparator.comparing(ItemActivity::getTitle));
            } else {
                itemList.sort(Comparator.comparingDouble(ItemActivity::getSeconds));
                Collections.reverse(itemList);
            }
            activityLogAdapter.updateList(itemList);
        });

        binding.weeklyButton.setOnClickListener(v ->
        {
            state = 2;
            binding.weeklyButton.setBackgroundResource(R.drawable.customlinebutton_pressed);
            binding.weeklyButton.setTextColor(Color.WHITE);
            binding.dailyButton.setBackgroundResource(R.drawable.customlinebutton2);
            binding.dailyButton.setTextColor(ContextCompat.getColor(this, R.color.primary));
            binding.monthlyButton.setBackgroundResource(R.drawable.customlinebutton2);
            binding.monthlyButton.setTextColor(ContextCompat.getColor(this, R.color.primary));
            dailyStats(usageStatsWeeklyList);
            if (isAlphabetical) {
                itemList.sort(Comparator.comparing(ItemActivity::getTitle));
            } else {
                itemList.sort(Comparator.comparingDouble(ItemActivity::getSeconds));
                Collections.reverse(itemList);
            }
            activityLogAdapter.updateList(itemList);
        });

        binding.monthlyButton.setOnClickListener(v -> {
                    state = 3;
                    binding.monthlyButton.setBackgroundResource(R.drawable.customlinebutton_pressed);
                    binding.monthlyButton.setTextColor(Color.WHITE);
                    binding.dailyButton.setBackgroundResource(R.drawable.customlinebutton2);
                    binding.dailyButton.setTextColor(ContextCompat.getColor(this, R.color.primary));
                    binding.weeklyButton.setBackgroundResource(R.drawable.customlinebutton2);
                    binding.weeklyButton.setTextColor(ContextCompat.getColor(this, R.color.primary));
                    dailyStats(usageStatsMonthlyList);
                    if (isAlphabetical) {
                        itemList.sort(Comparator.comparing(ItemActivity::getTitle));
                    } else {
                        itemList.sort(Comparator.comparingDouble(ItemActivity::getSeconds));
                        Collections.reverse(itemList);
                    }
                    activityLogAdapter.updateList(itemList);
                }

        );

        binding.alphabeticalSort.setOnClickListener(v -> {
            itemList.sort(Comparator.comparing(ItemActivity::getTitle));
            activityLogAdapter.updateList(itemList);
            isAlphabetical = true;
            binding.timeSort.setVisibility(View.VISIBLE);
            binding.alphabeticalSort.setVisibility(View.INVISIBLE);
        });

        binding.timeSort.setOnClickListener(v -> {
            itemList.sort(Comparator.comparingDouble(ItemActivity::getSeconds));
            Collections.reverse(itemList);
            activityLogAdapter.updateList(itemList);
            isAlphabetical = false;
            binding.timeSort.setVisibility(View.INVISIBLE);
            binding.alphabeticalSort.setVisibility(View.VISIBLE);
        });

        initializeList();
        state = 1;
        dailyStats(usageStatsDailyList);
        activityLogAdapter = new ActivityLogAdapter(this, itemList);

        binding.dailyButton.callOnClick();

        binding.recyclerViewInstalled.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerViewInstalled.setAdapter(activityLogAdapter);
        binding.recyclerViewInstalled.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }


    private void dailyStats(List<UsageStats> list) {
        itemList = new ArrayList<>();
        for (UsageStats usageStats : list) {
            for (App app : appList) {
                call(app, usageStats);
            }
        }
    }

    private void call(App app, UsageStats usageStats) {
        if (usageStats.getPackageName().equals(app.getPackageN())) {
            long duration = usageStats.getTotalTimeInForeground();
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            if(state == 1){
                double percentage = (duration / 86400000.0) * 100;
                formatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
                String durationFormat = formatter.format(new Date(duration));
                itemList.add(new ItemActivity(app.getImage(), app.getTitle(), app.getPackageN(), durationFormat, percentage));
            } else if(state ==  2) {
                double percentage = (duration / 604800000.0) * 100;
                formatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
                String durationFormat = formatter.format(new Date(duration));
                itemList.add(new ItemActivity(app.getImage(), app.getTitle(), app.getPackageN(), durationFormat, percentage));
            } else {
                double percentage = (duration / 2592000000.0) * 100;
                formatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
                String durationFormat = formatter.format(new Date(duration));
                itemList.add(new ItemActivity(app.getImage(), app.getTitle(), app.getPackageN(), durationFormat, percentage));
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
        appList.add(new App(ContextCompat.getDrawable(this, drawable), name, packageN));
    }

}