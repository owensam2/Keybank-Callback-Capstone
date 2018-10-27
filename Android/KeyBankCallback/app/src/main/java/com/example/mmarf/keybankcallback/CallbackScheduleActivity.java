package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;

import java.util.Date;

public class CallbackScheduleActivity extends Activity {
    private Button mButtonFirstDay;
    private Button mButtonSecondDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_schedule);
        Intent intent = getIntent();
        String department = intent.getStringExtra("KeyBank.CallbackConformationActivity.DEPARTMENT");
        this.mButtonFirstDay = findViewById(R.id.buttonCallbackSchedulerFirstDay);
        this.mButtonSecondDay = findViewById(R.id.buttonCallbackSchedulerSecondDay);
        SetupButtons(department);
    }

    private void SetupButtons(String department){
        this.mButtonFirstDay.setText(CallbackHelper.GetDayStringFromDate(CallbackHelper.GetNextAvailableTimeForDepartment(department)));
        this.mButtonSecondDay.setText(CallbackHelper.GetDayStringFromDate(CallbackHelper.GetNextAvailableDayForDepartment(department)));
    }
}
