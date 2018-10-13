package com.example.mmarf.keybankcallback;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallbackActivity extends AppCompatActivity {

    final String mMinutesText = " Minutes";
    final String mCalculating = "Calculating...";
    String mDepartment;
    Button mButtonRequestCallback;
    Button mButtonScheduleCallback;
    Button mButtonCallImmediately;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        this.mButtonRequestCallback = findViewById(R.id.buttonRequestCallback);
        this.mButtonScheduleCallback = findViewById(R.id.buttonScheduleCallaback);
        this.mButtonCallImmediately = findViewById(R.id.buttonCallImmidately);

        Intent intent = getIntent();
        int index = intent.getIntExtra("KeyBank.CallbackActivity.ITEM_INDEX", -1);
        String[] listOfDepartments = getResources().getStringArray(R.array.ListOfDepartments);
        mDepartment = listOfDepartments[index];

        SetupConnection(mDepartment);

        this.mButtonRequestCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferToConformationActivity(mDepartment, CallbackConformationActivity.class);
            }
        });
        this.mButtonScheduleCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferToConformationActivity(mDepartment, CallbackScheduleActivity.class);
            }
        });
        this.mButtonCallImmediately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numberToCall = new Intent(Intent.ACTION_CALL);
                numberToCall.setData(Uri.parse("tel:" + String.valueOf(NumberToCallOffLine())));
                if (ActivityCompat.checkSelfPermission(CallbackActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(CallbackActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(CallbackActivity.this);
                    }
                    builder.setTitle("Call out not supported")
                    .setMessage("Go to app settings and allow access to the phone.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return;
                }
                startActivity(numberToCall);
            }
        });
    }
    void SetupConnection(String department){
        //Get the desired department
        TextView textViewDepartment = findViewById(R.id.textViewDepartment);
        textViewDepartment.setText(department);
        TextView textViewWaitMinutes =  findViewById(R.id.textViewWaitMinutes);
        textViewWaitMinutes.setText(mCalculating);
        //TODO Find the minutes estimated by the department sent in.
        textViewWaitMinutes.setText(String.valueOf(EstimatedMinutesOffLine()) + mMinutesText);
    }
    void TransferToConformationActivity(String department, Class<?> cls){
        Intent callbackScheduleActivity = new Intent(getApplicationContext(), cls);
        callbackScheduleActivity.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        startActivity(callbackScheduleActivity);
    }

    final int EstimatedMinutesOffLine(){
        return 20;
    }

    final String NumberToCallOffLine(){
        return "8005392968";
    }

}
