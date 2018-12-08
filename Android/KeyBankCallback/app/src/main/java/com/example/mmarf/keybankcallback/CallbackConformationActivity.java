package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallbackConformationActivity extends AppCompatActivity {
    Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_conformation);
        Intent intent = getIntent();
        String department = intent.getStringExtra("KeyBank.CallbackConformationActivity.DEPARTMENT");
        SetupScreen(department);
        mResources = getResources();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button cancelButton = findViewById(R.id.buttonCancelCallback);
        Button mainMenuButton = findViewById(R.id.buttonHome);

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.TransferToMainPage(CallbackConformationActivity.this);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallbackHelper.DisplayYesNoDialogForCancelCallback(CallbackConformationActivity.this, mResources.getString(R.string.cancel_callback), mResources.getString(R.string.wish_to_cancel_callback));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                CallbackHelper.TransferToMainPage(CallbackConformationActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void SetupScreen(String department){
        //Get the desired department
        TextView textViewWaitMinutes2 = findViewById(R.id.textViewWaitMinutes2);
        textViewWaitMinutes2.setText(String.valueOf(CallbackHelper.GetCallbackServerMediator().GetEstimatedMinutesOfScheduledCallback(department)));
    }
}
