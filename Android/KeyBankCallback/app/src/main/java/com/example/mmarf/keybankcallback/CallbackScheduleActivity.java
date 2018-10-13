package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class CallbackScheduleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_schedule);
        Intent intent = getIntent();
        //int index = intent.getIntExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", -1);

    }
}
