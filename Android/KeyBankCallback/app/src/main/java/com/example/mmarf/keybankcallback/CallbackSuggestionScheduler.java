package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class CallbackSuggestionScheduler extends AppCompatActivity {
    Button mButtonTimeRank1;
    Button mButtonTimeRank2;
    Button mButtonTimeRank3;
    Button mButtonTimeRankCustom;
    private static int mNumOfButtons = 3;
    int[] mButtonOrder = new int[mNumOfButtons];
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
                CallbackHelper.GetCallbackServerMediator().SetCallbackTime(CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, mButtonOrder[0], mDepartment).getTime(), mDepartment);
                CallbackHelper.TransferToConformationActivity(CallbackSuggestionScheduler.this, mDepartment);
            }
        });
        this.mButtonTimeRank2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.GetCallbackServerMediator().SetCallbackTime(CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, mButtonOrder[1], mDepartment).getTime(), mDepartment);
                CallbackHelper.TransferToConformationActivity(CallbackSuggestionScheduler.this, mDepartment);
            }
        });
        this.mButtonTimeRank3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.GetCallbackServerMediator().SetCallbackTime(CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, mButtonOrder[2], mDepartment).getTime(), mDepartment);
                CallbackHelper.TransferToConformationActivity(CallbackSuggestionScheduler.this, mDepartment);
            }
        });
        this.mButtonTimeRankCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass off to the custom scheduler.
            CallbackHelper.TransferToCustomScheduler(CallbackSuggestionScheduler.this, mDepartment);
            }
        });
    }

    void SetupButtonText(){
        //Figure today/tomorrow. Rank according to first suggested.
        Calendar[] calendar = new Calendar[mNumOfButtons];

        calendar[0] = CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, 1, mDepartment);
        calendar[1] = CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, 2, mDepartment);
        calendar[2] = CallbackHelper.GetSuggestedCalendar(CallbackSuggestionScheduler.this, 3, mDepartment);

        //Find the lowest date
        if(calendar[0].getTime().before(calendar[1].getTime())){
            mButtonOrder[0] = 1;
            mButtonOrder[1] = 2;
            mButtonOrder[2] = 3;
        }
        else if(calendar[1].getTime().before(calendar[2].getTime())){
            mButtonOrder[0] = 2;
            mButtonOrder[1] = 3;
            mButtonOrder[2] = 1;
        }
        else{
            mButtonOrder[0] = 3;
            mButtonOrder[1] = 1;
            mButtonOrder[2] = 2;
        }

        this.mButtonTimeRank1.setText(CallbackHelper.GetSuggestedTimeString(calendar[mButtonOrder[0]-1].getTime()));
        this.mButtonTimeRank2.setText(CallbackHelper.GetSuggestedTimeString(calendar[mButtonOrder[1]-1].getTime()));
        this.mButtonTimeRank3.setText(CallbackHelper.GetSuggestedTimeString(calendar[mButtonOrder[2]-1].getTime()));
    }
}
