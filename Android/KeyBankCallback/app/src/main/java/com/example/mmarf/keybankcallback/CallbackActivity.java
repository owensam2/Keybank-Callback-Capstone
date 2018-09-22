package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CallbackActivity extends AppCompatActivity {

    String mDepartment;
    Button ButtonInitiateConnection;

    //void CallbackActivity(String department){
    //     this.mDepartment = department;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        this.ButtonInitiateConnection = findViewById(R.id.buttonInitiateConnection);

        Intent intent = getIntent();
        int index = intent.getIntExtra("KeyBank.CallbackActivity.ITEM_INDEX", -1);
        SetupConnection(index);

        this.ButtonInitiateConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Test of GIT Hub pushing
            }
        });
    }
    void SetupConnection(int departmentIndex){
        //Get the desired department
        String[] listOfDepartments = getResources().getStringArray(R.array.ListOfDepartments);
        this.ButtonInitiateConnection.setText("Connect to " + listOfDepartments[departmentIndex]);
    }

}
