package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CallbackSuggestionScheduler extends AppCompatActivity {
    Button mButtonTimeRank1;
    Button mButtonTimeRank2;
    Button mButtonTimeRank3;
    Button mButtonTimeRankCustom;
    public static Resources mResources;
    int[] mButtonOrder = new int[3];
    String mDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_suggestion_scheduler);
        Intent intent = getIntent();
        mDepartment = intent.getStringExtra("KeyBank.CallbackConformationActivity.DEPARTMENT");

        this.mButtonTimeRank1 = findViewById(R.id.buttonTimeRank1);
        this.mButtonTimeRank2 = findViewById(R.id.buttonTimeRank2);
        this.mButtonTimeRank3 = findViewById(R.id.buttonTimeRank3);
        this.mButtonTimeRankCustom = findViewById(R.id.buttonCustomSchedule);


        //Find what time it is and populate the button text with the appropriate times
        SetupButtonText();
        this.mButtonTimeRank1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackQuestionsActivity.GetCallbackServerMediator().SetCallbackTime(GetSuggestedCalendar(mButtonOrder[0]).getTime(), mDepartment);
                
            }
        });
        this.mButtonTimeRank2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.mButtonTimeRank3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.mButtonTimeRankCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass off to the custom scheduler.

            }
        });
    }

    void SetupButtonText(){
        //Figure today/tomorrow. Rank according to first suggested.
        if(CallbackHelper.IsTimeAfterCurrentTime(this.GetSuggestedCalendar(1))){
            this.mButtonTimeRank1.setText(GetSuggestedTimeString(1, true));
            this.mButtonTimeRank2.setText(GetSuggestedTimeString(2, true));
            this.mButtonTimeRank3.setText(GetSuggestedTimeString(3, true));
            mButtonOrder[0] = 1;
            mButtonOrder[1] = 2;
            mButtonOrder[2] = 3;
        }
        else if(CallbackHelper.IsTimeAfterCurrentTime(this.GetSuggestedCalendar(2))){
            this.mButtonTimeRank1.setText(GetSuggestedTimeString(2, true));
            this.mButtonTimeRank2.setText(GetSuggestedTimeString(3, true));
            this.mButtonTimeRank3.setText(GetSuggestedTimeString(1, false));
            mButtonOrder[0] = 2;
            mButtonOrder[1] = 3;
            mButtonOrder[2] = 1;
        }
        else if(CallbackHelper.IsTimeAfterCurrentTime(this.GetSuggestedCalendar(3))){
            this.mButtonTimeRank1.setText(GetSuggestedTimeString(3, true));
            this.mButtonTimeRank2.setText(GetSuggestedTimeString(1, false));
            this.mButtonTimeRank3.setText(GetSuggestedTimeString(2, false));
            mButtonOrder[0] = 3;
            mButtonOrder[1] = 1;
            mButtonOrder[2] = 2;
        }
        else{
            this.mButtonTimeRank1.setText(GetSuggestedTimeString(1, false));
            this.mButtonTimeRank2.setText(GetSuggestedTimeString(2, false));
            this.mButtonTimeRank3.setText(GetSuggestedTimeString(3, false));
            mButtonOrder[0] = 1;
            mButtonOrder[1] = 2;
            mButtonOrder[2] = 3;
        }
    }

    public static Calendar GetSuggestedCalendar(int suggestedIndex){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));

        Date date = new Date();
        date.setHours(GetSuggestedHour(suggestedIndex));
        date.setMinutes(GetSuggestedMinute(suggestedIndex));
        date.setSeconds(0);
        cal.setTime(date);
        return cal;
    }

    String GetSuggestedTimeString(int suggestedIndex, boolean isToday) {
        String availableDay;
        //TODO: Get something that may not be tomorrow.
        if(isToday)
            availableDay = mResources.getString(R.string.today);
        else
            availableDay = mResources.getString(R.string.tomorrow);
        return FormatSuggestedTimeString(availableDay,
                GetSuggestedHour(suggestedIndex),
                GetSuggestedMinute(suggestedIndex));
    }

    String FormatSuggestedTimeString(String day, int hour, int minute){
        String amPm = "AM";
        if(hour > 12){
            hour = hour - 12;
            amPm = "PM";
        }
        String minuteString;
        minuteString = String.format("%02d", minute);

        return String.format(day + " at " + String.valueOf(hour) + ":" + minuteString + " " + amPm);
    }

    public static int GetSuggestedHour(int suggestedIndex){
        int returnItem;
        switch (suggestedIndex){
            case 1: returnItem =  mResources.getInteger(R.integer.FirstSuggestedTimeHour);
                break;
            case 2: returnItem =  mResources.getInteger(R.integer.SecondSuggestedTimeHour);
                break;
            case 3: returnItem =  mResources.getInteger(R.integer.ThirdSuggestedTimeHour);
                break;
            default: returnItem = -1;
                break;
        }
        return returnItem;
    }
    public static int GetSuggestedMinute(int suggestedIndex){
        int returnItem;
        switch (suggestedIndex){
            case 1: returnItem =  mResources.getInteger(R.integer.FirstSuggestedTimeMinute);
                break;
            case 2: returnItem =  mResources.getInteger(R.integer.SecondSuggestedTimeMinute);
                break;
            case 3: returnItem =  mResources.getInteger(R.integer.ThirdSuggestedTimeMinute);
                break;
            default: returnItem = -1;
                break;
        }
        return returnItem;
    }

}
