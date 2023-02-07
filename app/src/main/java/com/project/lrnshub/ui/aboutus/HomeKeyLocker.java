package com.project.lrnshub.ui.aboutus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.lrnshub.R;
import com.project.lrnshub.databinding.ActivityAlarmBinding;
import com.project.lrnshub.ui.alarm.NotificationPublisher;
import com.project.lrnshub.ui.todo.Notes;

import java.util.Calendar;

public class HomeKeyLocker {
    private OverlayDialog mOverlayDialog;
    public void lock(Activity activity) {
        if (mOverlayDialog == null) {
            mOverlayDialog = new OverlayDialog(activity);
            mOverlayDialog.show();
        }
    }
    public void unlock() {
        if (mOverlayDialog != null) {
            mOverlayDialog.dismiss();
            mOverlayDialog = null;
        }
    }
    private static class OverlayDialog extends AlertDialog {

        public OverlayDialog(Activity activity) {
            super(activity, R.style.OverlayDialog);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.type =  WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            params.dimAmount = 0.0F; // transparent
            params.width = 0;
            params.height = 0;
            params.gravity = Gravity.BOTTOM;
            getWindow().setAttributes(params);
            getWindow().setFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, 0xffffff);
            setOwnerActivity(activity);
            setCancelable(false);
        }

        public final boolean dispatchTouchEvent(MotionEvent motionevent) {
            return true;
        }

        protected final void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            FrameLayout framelayout = new FrameLayout(getContext());
            framelayout.setBackgroundColor(0);
            setContentView(framelayout);
        }
    }
}