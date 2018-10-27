package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Date;

public class CallbackConformationTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_conformation_time);
        //Intent intent = getIntent();
        //String department = intent.getStringExtra("KeyBank.CallbackConformationActivity.DEPARTMENT");
        SetupScreen();
    }
    private  void SetupScreen(){
        //Get the desired department
        TextView textViewCallbackDay = findViewById(R.id.textViewCallbackDay);
        TextView textViewCallbackTime = findViewById(R.id.textViewCallbackTime);
        Date scheduledDate = CallbackQuestionsActivity.GetCallbackServerMediator().GetCallbackTime();

        textViewCallbackDay.setText(String.valueOf(CallbackHelper.GetDayStringFromDate(scheduledDate)));
        textViewCallbackTime.setText(CallbackHelper.GetTimeStringFromDate(scheduledDate));
    }
}
