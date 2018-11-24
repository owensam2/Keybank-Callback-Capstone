package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallbackActivity extends AppCompatActivity {

    final String mMinutesText = " Minutes";
    final String mCalculating = "Calculating...";
    String mDepartment;
    Button mButtonRequestCallback;
    Button mButtonScheduleCallback;
    Button mButtonCallImmediately;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mButtonRequestCallback = findViewById(R.id.buttonRequestCallback);
        this.mButtonScheduleCallback = findViewById(R.id.buttonScheduleCallaback);
        this.mButtonCallImmediately = findViewById(R.id.buttonCallImmidately);

        Intent intent = getIntent();
        int index = intent.getIntExtra("KeyBank.CallbackActivity.ITEM_INDEX", -1);
        mDepartment = CallbackHelper.GetDepartmentName(index, CallbackActivity.this);
        SetupUIItems(mDepartment);

        this.mButtonRequestCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.GetCallbackServerMediator().SetUserToNextAvailableCallback(mDepartment);
                CallbackHelper.TransferToConformationActivity(CallbackActivity.this,mDepartment);
            }
        });
        this.mButtonScheduleCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.TransferToSuggestionScheduler(CallbackActivity.this,mDepartment);
            }
        });
        this.mButtonCallImmediately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.Call(CallbackActivity.this);
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

    void SetupUIItems(String department){
        //Get the desired department
        TextView textViewDepartment = findViewById(R.id.textViewDepartment);
        textViewDepartment.setText(department);
        TextView textViewWaitMinutes =  findViewById(R.id.textViewWaitMinutes);
        textViewWaitMinutes.setText(mCalculating);
        textViewWaitMinutes.setText(String.valueOf(CallbackHelper.GetCallbackServerMediator().GetEstimatedMinutesOfQueue(department)) + mMinutesText);
    }
}
