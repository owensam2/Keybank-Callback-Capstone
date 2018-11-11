package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CallbackScheduleActivity extends AppCompatActivity {
    private Button mButtonFirstDay;
    private Button mButtonSecondDay;
    private String mDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_schedule);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDepartment = intent.getStringExtra("KeyBank.CallbackConformationActivity.DEPARTMENT");
        this.mButtonFirstDay = findViewById(R.id.buttonCallbackSchedulerFirstDay);
        this.mButtonSecondDay = findViewById(R.id.buttonCallbackSchedulerSecondDay);
        SetupButtons(mDepartment);
        this.mButtonFirstDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the starting date in the helper
                CallbackHelper.SetupStartingDateForCustomScheduler(CallbackHelper.GetNextAvailableTimeForDepartment(mDepartment));
                CallbackHelper.TransferToTimeScheduler(CallbackScheduleActivity.this, mDepartment);
            }
        });
        this.mButtonSecondDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the starting date in the helper
                CallbackHelper.SetupStartingDateForCustomScheduler(CallbackHelper.GetNextAvailableDayForDepartment(mDepartment));
                CallbackHelper.TransferToTimeScheduler(CallbackScheduleActivity.this, mDepartment);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SetupButtons(String department){
        this.mButtonFirstDay.setText(CallbackHelper.GetDayStringFromDate(CallbackHelper.GetNextAvailableTimeForDepartment(department)));
        this.mButtonSecondDay.setText(CallbackHelper.GetDayStringFromDate(CallbackHelper.GetNextAvailableDayForDepartment(department)));
    }
}
