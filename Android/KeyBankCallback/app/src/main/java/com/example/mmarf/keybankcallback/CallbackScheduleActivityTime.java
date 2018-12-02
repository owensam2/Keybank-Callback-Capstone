package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CallbackScheduleActivityTime extends AppCompatActivity {
    TextView mTextViewScheduleTodayTomorrow;
    Spinner mSpinnerTime;
    String mDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_schedule_time);
        Intent intent = getIntent();
        mDepartment = intent.getStringExtra("KeyBank.CallbackConformationActivity.DEPARTMENT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Get the day that we are starting from.
        this.mTextViewScheduleTodayTomorrow = findViewById(R.id.textViewScheduleTodayTomorrow);
        this.mSpinnerTime = findViewById(R.id.spinnerTime);
        SetupLabel();
        AddTimesToSpinner();
        Button buttonConfirmSchedule = findViewById(R.id.buttonConfirmSchedule);
        buttonConfirmSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send what is present in the spinner boxes to the scheduler.
                String time = (String)mSpinnerTime.getSelectedItem();
                Date requestedDate = CallbackHelper.UpdateCustomDate(time);
                CallbackHelper.GetCallbackServerMediator().SetCallbackTime(requestedDate, mDepartment);
                CallbackHelper.TransferToConformationTimeActivity(CallbackScheduleActivityTime.this, mDepartment);
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

    private void SetupLabel(){
        mTextViewScheduleTodayTomorrow.setText(CallbackHelper.GetDayStringFromDate(CallbackHelper.GetCustomStartingDate()));
    }

    private void AddTimesToSpinner(){
        boolean forceIncrement = false;
        Date runningDate = CallbackHelper.RoundToNextQuarterOfHour(CallbackHelper.GetCustomStartingDate(), forceIncrement);
        Boolean done = false;
        List<String> listOfTimes = new ArrayList<>();
        do{
            listOfTimes.add((String) android.text.format.DateFormat.format("hh:mm a", runningDate));
            runningDate = CallbackHelper.RoundToNextQuarterOfHour(runningDate, forceIncrement);
            forceIncrement = true;
            if(runningDate.getHours() >= 17){
                done = true;
            }
        }while(!done);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listOfTimes);
        mSpinnerTime.setAdapter(adapter);
    }


}
