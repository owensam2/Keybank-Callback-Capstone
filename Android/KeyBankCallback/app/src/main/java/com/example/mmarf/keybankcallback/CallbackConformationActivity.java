package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CallbackConformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_conformation);
        Intent intent = getIntent();
        String department = intent.getStringExtra("KeyBank.CallbackConformationActivity.DEPARTMENT");
        SetupScreen(department);
    }

    private  void SetupScreen(String department){
        //Get the desired department
        TextView textViewWaitMinutes2 = findViewById(R.id.textViewWaitMinutes2);
        textViewWaitMinutes2.setText(String.valueOf(CallbackQuestionsActivity.GetCallbackServerMediator().GetEstimatedTimeRemaining(department)));
    }
}
