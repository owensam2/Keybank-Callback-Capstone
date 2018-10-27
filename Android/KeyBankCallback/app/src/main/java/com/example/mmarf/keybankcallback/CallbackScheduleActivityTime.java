package com.example.mmarf.keybankcallback;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CallbackScheduleActivityTime extends Activity {
    TextView mTextViewScheduleTodayTomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_schedule_time);
        //Get the day that we are starting from.
        this.mTextViewScheduleTodayTomorrow = findViewById(R.id.textViewScheduleTodayTomorrow);
        SetupLabel();
    }

    private void SetupLabel(){
        mTextViewScheduleTodayTomorrow.setText(CallbackHelper.GetDayStringFromDate(CallbackHelper.GetCustomStartingDate()));
    }
}
