package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallbackActivityNoWait extends AppCompatActivity {
    String mDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_no_wait);
        Button callImmediately = findViewById(R.id.buttonCallImmidatelyNoWait);
        Button scheduleCallback = findViewById(R.id.buttonScheduleCallNoWait);
        Intent intent = getIntent();
        int index = intent.getIntExtra("KeyBank.CallbackActivity.ITEM_INDEX", -1);

        mDepartment = CallbackHelper.GetDepartmentName(index, CallbackActivityNoWait.this);
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
                CallbackHelper.TransferToConformationActivity(CallbackActivityNoWait.this,mDepartment, CallbackScheduleActivity.class);
            }
        });
    }
    void SetupUIItems(String department){
        //Get the desired department
        TextView textViewDepartment = findViewById(R.id.textViewDepartmentNoWait);
        textViewDepartment.setText(department);
    }

}
