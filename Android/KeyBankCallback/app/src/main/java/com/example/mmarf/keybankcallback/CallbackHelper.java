package com.example.mmarf.keybankcallback;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

public class CallbackHelper {

    public static void Call(Context context){
        Intent numberToCall = new Intent(Intent.ACTION_CALL);
        //TODO: Update this to the actual phone number
        numberToCall.setData(Uri.parse("tel:" + String.valueOf(CallbackQuestionsActivity.GetCallbackServerMediator().GetPhoneNumberForDepartment("Fraud"))));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
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
        context.startActivity(numberToCall);
    }

    public static void TransferToConformationActivity(Context context, String department, Class<?> cls){
        Intent callbackScheduleActivity = new Intent(context.getApplicationContext(), cls);
        callbackScheduleActivity.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(callbackScheduleActivity);
    }

    public static String GetDepartmentName(int index,Context context){
        String[] listOfDepartments = context.getResources().getStringArray(R.array.ListOfDepartments);
        return listOfDepartments[index];
    }
}
