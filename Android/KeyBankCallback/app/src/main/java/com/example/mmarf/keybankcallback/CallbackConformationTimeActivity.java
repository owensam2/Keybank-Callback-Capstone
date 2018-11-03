package com.example.mmarf.keybankcallback;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Date;

public class CallbackConformationTimeActivity extends AppCompatActivity {
    Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_conformation_time);
        //Intent intent = getIntent();
        //String department = intent.getStringExtra("KeyBank.CallbackConformationActivity.DEPARTMENT");
        SetupScreen();
        mResources = getResources();

        Button cancelButton = findViewById(R.id.buttonCancelCallback);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.DisplayYesNoDialogForCancelCallback(CallbackConformationTimeActivity.this, mResources.getString(R.string.cancel_callback), mResources.getString(R.string.wish_to_cancel_callback));
            }
        });
    }
    private  void SetupScreen(){
        //Get the desired department
        TextView textViewCallbackDay = findViewById(R.id.textViewCallbackDay);
        TextView textViewCallbackTime = findViewById(R.id.textViewCallbackTime);
        Date scheduledDate = CallbackHelper.GetCallbackServerMediator().GetCallbackTime();

        textViewCallbackDay.setText(String.valueOf(CallbackHelper.GetDayStringFromDate(scheduledDate)));
        textViewCallbackTime.setText(CallbackHelper.GetTimeStringFromDate(scheduledDate));
    }
}
