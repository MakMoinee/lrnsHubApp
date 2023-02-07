package com.project.lrnshub.ui.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.project.lrnshub.databinding.ActivityAlarmBinding;
import com.project.lrnshub.databinding.FragmentHomeBinding;
import com.project.lrnshub.ui.todo.Notes;

import java.util.Calendar;


public class AlarmFragment extends DialogFragment {

    Notes notes;
    ActivityAlarmBinding binding;

    public AlarmFragment() {
        // Required empty public constructor
    }

    public static AlarmFragment newInstance() {
      AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ActivityAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle mArgs = getArguments();
        notes = (Notes) mArgs.getSerializable("list");


        binding.buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        binding.buttonSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
        binding.buttonScheduleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleNotification();
            }
        });

        return root;

    }


    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = (month + 1) + "/" + dayOfMonth + "/" + year;
                        binding.textViewDate.setText(date);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        binding.textViewTime.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void scheduleNotification() {
        String notificationText = binding.editTextNotification.getText().toString();
        String dateText = binding.textViewDate.getText().toString();
        String timeText = binding.textViewTime.getText().toString();

        if (notificationText.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter a notification message", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dateText.isEmpty() || timeText.isEmpty()) {
            Toast.makeText(requireActivity(), "Select a date and time", Toast.LENGTH_SHORT).show();
            return;
        }


        String[] dateParts = dateText.split("/");
        if (dateParts.length != 3) {
            Toast.makeText(requireActivity(), "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] timeParts = timeText.split(":");
        if (timeParts.length != 2) {
            Toast.makeText(requireActivity(), "Invalid time format", Toast.LENGTH_SHORT).show();
            return;
        }

        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long notificationTime = calendar.getTimeInMillis();

        showNotifs(notificationTime, notificationText);

    }

    private void showNotifs(long notificationTime, String notificationText) {
        Intent notificationIntent = new Intent(requireActivity(), NotificationPublisher.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notes.getCode());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_TEXT, notificationText);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), notes.getCode(), notificationIntent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(requireActivity().ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
        Toast.makeText(requireActivity(), "Notification scheduled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

}
