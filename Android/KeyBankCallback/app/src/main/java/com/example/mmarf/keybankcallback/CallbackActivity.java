package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallbackActivity extends AppCompatActivity {

    String mDepartment;
    Button ButtonRequestCallback;

    //void CallbackActivity(String department){
    //     this.mDepartment = department;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        this.ButtonRequestCallback = findViewById(R.id.buttonRequestCallback);

        Intent intent = getIntent();
        int index = intent.getIntExtra("KeyBank.CallbackActivity.ITEM_INDEX", -1);
        String[] listOfDepartments = getResources().getStringArray(R.array.ListOfDepartments);
        mDepartment = listOfDepartments[index];

        SetupConnection(mDepartment);

        this.ButtonRequestCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferToConformationActivity(mDepartment);
            }
        });
    }
    void SetupConnection(String department){
        //Get the desired department
        TextView textViewDepartment = findViewById(R.id.textViewDepartment);
        textViewDepartment.setText("Department: " + department);
        //TODO Find the minutes estimated by the department sent in.
    }
    void TransferToConformationActivity(String department){
        Intent callbackScheduleActivity = new Intent(getApplicationContext(), CallbackScheduleActivity.class);
        callbackScheduleActivity.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        startActivity(callbackScheduleActivity);
    }
}
