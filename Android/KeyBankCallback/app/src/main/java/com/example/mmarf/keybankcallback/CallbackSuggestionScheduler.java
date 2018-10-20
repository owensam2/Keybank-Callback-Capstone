package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
                CallbackQuestionsActivity.GetCallbackServerMediator().SetCallbackTime(CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, mButtonOrder[0]).getTime(), mDepartment);

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
        if(CallbackHelper.IsTimeAfterCurrentTime(CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, 1))){
            this.mButtonTimeRank1.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,1, true));
            this.mButtonTimeRank2.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,2, true));
            this.mButtonTimeRank3.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,3, true));
            mButtonOrder[0] = 1;
            mButtonOrder[1] = 2;
            mButtonOrder[2] = 3;
        }
        else if(CallbackHelper.IsTimeAfterCurrentTime(CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, 2))){
            this.mButtonTimeRank1.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,2, true));
            this.mButtonTimeRank2.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,3, true));
            this.mButtonTimeRank3.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,1, false));
            mButtonOrder[0] = 2;
            mButtonOrder[1] = 3;
            mButtonOrder[2] = 1;
        }
        else if(CallbackHelper.IsTimeAfterCurrentTime(CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, 3))){
            this.mButtonTimeRank1.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,3, true));
            this.mButtonTimeRank2.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,1, false));
            this.mButtonTimeRank3.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,2, false));
            mButtonOrder[0] = 3;
            mButtonOrder[1] = 1;
            mButtonOrder[2] = 2;
        }
        else{
            this.mButtonTimeRank1.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,1, false));
            this.mButtonTimeRank2.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,2, false));
            this.mButtonTimeRank3.setText(CallbackHelper.GetSuggestedTimeString(CallbackSuggestionScheduler.this,3, false));
            mButtonOrder[0] = 1;
            mButtonOrder[1] = 2;
            mButtonOrder[2] = 3;
        }
    }





}
