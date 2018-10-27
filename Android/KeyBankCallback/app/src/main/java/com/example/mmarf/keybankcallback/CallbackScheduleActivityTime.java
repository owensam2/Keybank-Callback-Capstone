package com.example.mmarf.keybankcallback;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class CallbackScheduleActivityTime extends Activity {
    TextView mTextViewScheduleTodayTomorrow;
    Spinner mSpinnerHour;
    Spinner mSpinnerMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_schedule_time);
        //Get the day that we are starting from.
        this.mTextViewScheduleTodayTomorrow = findViewById(R.id.textViewScheduleTodayTomorrow);
        this.mSpinnerHour = findViewById(R.id.spinnerHour);
        this.mSpinnerMinute = findViewById(R.id.spinnerMinute);
        SetupLabel();
        AddHoursToSpinner();
        AddMinutesToSpinner();
    }

    private void SetupLabel(){
        mTextViewScheduleTodayTomorrow.setText(CallbackHelper.GetDayStringFromDate(CallbackHelper.GetCustomStartingDate()));
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
