package com.example.mmarf.keybankcallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

public class CallbackScheduleActivityTime extends AppCompatActivity {
    TextView mTextViewScheduleTodayTomorrow;
    Spinner mSpinnerHour;
    Spinner mSpinnerMinute;
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
        this.mSpinnerHour = findViewById(R.id.spinnerHour);
        this.mSpinnerMinute = findViewById(R.id.spinnerMinute);
        SetupLabel();
        AddHoursToSpinner();
        AddMinutesToSpinner();
        Button buttonConfirmSchedule = findViewById(R.id.buttonConfirmSchedule);
        buttonConfirmSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send what is present in the spinner boxes to the scheduler.
                String hour = (String)mSpinnerHour.getSelectedItem();
                String minute = (String)mSpinnerMinute.getSelectedItem();
                Date dateToModify = CallbackHelper.GetCustomStartingDate();
                dateToModify.setHours(GetProperHourToDisplay(hour));
                dateToModify.setMinutes(Integer.valueOf(minute));
                CallbackHelper.GetCallbackServerMediator().SetCallbackTime(dateToModify, mDepartment);
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

    private int GetProperHourToDisplay(String hour){
        //If it's less than 8, add 12 hours to it.
        int realHour = Integer.valueOf(hour);
        if(realHour < 8)
            realHour = realHour + 12;
        return realHour;
    }


    private void AddHoursToSpinner(){
        String[] arrayAllHours;
        int hour = CallbackHelper.GetCustomStartingDate().getHours();
        if(hour <= 8){
            arrayAllHours = new String[] {
                    "8", "9", "10", "11", "12", "1", "2", "3", "4", "5"
            };
        }
        else if(hour <= 9){
            arrayAllHours = new String[] {
                    "9", "10", "11", "12", "1", "2", "3", "4", "5"
            };
        }
        else if(hour <= 10){
            arrayAllHours = new String[] {
                    "10", "11", "12", "1", "2", "3", "4", "5"
            };
        }
        else if(hour <= 11){
            arrayAllHours = new String[] {
                     "11", "12", "1", "2", "3", "4", "5"
            };
        }
        else if(hour <= 12){
            arrayAllHours = new String[] {
                    "12", "1", "2", "3", "4", "5"
            };
        }
        else if(hour <= 13){
            arrayAllHours = new String[] {
                    "1", "2", "3", "4", "5"
            };
        }
        else if(hour <= 14){
            arrayAllHours = new String[] {
                    "2", "3", "4", "5"
            };
        }
        else if(hour <= 15){
            arrayAllHours = new String[] {
                    "3", "4", "5"
            };
        }
        else if(hour <= 16){
            arrayAllHours = new String[] {
                    "4", "5"
            };
        }
        else if(hour <= 17){
            arrayAllHours = new String[] {
                    "5"
            };
        }
        else{
            arrayAllHours = new String[] {
                    "8", "9", "10", "11", "12", "1", "2", "3", "4", "5"
            };
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayAllHours);
        mSpinnerHour.setAdapter(adapter);
    }

    private void AddMinutesToSpinner(){
        String[] arraySpinner = new String[] {
                "00", "15", "30", "45"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        mSpinnerMinute.setAdapter(adapter);
    }

}
