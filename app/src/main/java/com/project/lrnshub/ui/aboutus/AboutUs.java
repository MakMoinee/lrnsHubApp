package com.project.lrnshub.ui.aboutus;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.lrnshub.databinding.ActivityAboutusBinding;
import com.project.lrnshub.databinding.ActivityAlarmBinding;
import com.project.lrnshub.ui.alarm.NotificationPublisher;
import com.project.lrnshub.ui.todo.Notes;

import java.util.Calendar;

public class AboutUs extends AppCompatActivity {

    ActivityAboutusBinding binding;
    Bundle extras;
    Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }


}