package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallbackActivityNoWait extends AppCompatActivity {
    String mDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //See if the offices are open.
        setContentView(R.layout.activity_callback_no_wait);
        Button callImmediately = findViewById(R.id.buttonCallImmidatelyNoWait);
        Button scheduleCallback = findViewById(R.id.buttonScheduleCallNoWait);
        Intent intent = getIntent();
        int index = intent.getIntExtra("KeyBank.CallbackActivity.ITEM_INDEX", -1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDepartment = CallbackHelper.GetDepartmentName(index, CallbackActivityNoWait.this);
        TextView noWaitText = findViewById(R.id.textViewNoWaitText);
        if(CallbackHelper.GetCallbackServerMediator().IsOfficeOpen(mDepartment)){
            //Set the text appropriately to open offices.
            noWaitText.setText(getResources().getString(R.string.There_is_currently_no_wait));
        }
        else{
            //Set the text appropriately to closed offices.
            noWaitText.setText(getResources().getString(R.string.offices_closed_message));
        }
        SetupUIItems(mDepartment);

        callImmediately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.Call(CallbackActivityNoWait.this);
            }
        });

        scheduleCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.TransferToSuggestionScheduler(CallbackActivityNoWait.this,mDepartment);
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
        TextView textViewDepartment = findViewById(R.id.textViewDepartmentNoWait);
        textViewDepartment.setText(department);
    }
}
