package com.example.mmarf.keybankcallback;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CallbackHelper {

    private static CallbackServerMediator mCallbackServerMediator;

    public static void Call(Context context){
        Intent numberToCall = new Intent(Intent.ACTION_CALL);
        //TODO: Update this to the actual phone number
        numberToCall.setData(Uri.parse("tel:" + String.valueOf(CallbackHelper.GetCallbackServerMediator().GetPhoneNumberForDepartment("Fraud"))));
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

    public static void TransferToConformationActivity(Context context, String department){
        Intent callbackScheduleActivity = new Intent(context.getApplicationContext(), CallbackConformationTimeActivity.class);
        callbackScheduleActivity.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(callbackScheduleActivity);
    }

    public static String GetDepartmentName(int index,Context context){
        String[] listOfDepartments = context.getResources().getStringArray(R.array.ListOfDepartments);
        return listOfDepartments[index];
    }

    public static void TransferToSuggestionScheduler(Context context, String department){
        Intent SuggestionScheduler = new Intent(context.getApplicationContext(), CallbackSuggestionScheduler.class);
        SuggestionScheduler.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(SuggestionScheduler);
    }

    public static void TransferToCustomScheduler(Context context, String department){
        Intent SuggestionScheduler = new Intent(context.getApplicationContext(), CallbackScheduleActivity.class);
        SuggestionScheduler.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(SuggestionScheduler);
    }

    public static boolean IsTimeAfterCurrentTime(Calendar compareCalendar) {
        //Convert everything to the current timezone. Only comparing time here.
        Calendar cal = Calendar.getInstance();
        long gmtTime = cal.getTime().getTime();

        long timezoneAlteredTime = gmtTime + TimeZone.getTimeZone(GetLocalTimeZone()).getRawOffset();
        Calendar cSchedulerStartCal1 = Calendar.getInstance(TimeZone.getTimeZone(GetLocalTimeZone()));
        cSchedulerStartCal1.setTimeInMillis(timezoneAlteredTime);
        Date date1 = cSchedulerStartCal1.getTime();
        Date date2 = compareCalendar.getTime();

        if(date1.before(date2)) {
            return true;
        } else {

            return false;
        }
    }

    public static String GetLocalTimeZone(){
        return "EST";
    }

    public static Date GetNextAvailableDayForDepartment(String department){
        //Returns the opening time and day available for the department.
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        Date date = GetNextAvailableTimeForDepartment(department);
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, GetHoursInDayIncrementsFromDates(cal.getTime(), date));
        return cal.getTime();
    }

    public static Date GetNextAvailableTimeForDepartment(String department){
        return mCallbackServerMediator.GetNextAvailableTime(department);
    }

    public static String FormatSuggestedTimeString(String day, int hour, int minute){
        String amPm = "AM";
        if(hour > 12){
            hour = hour - 12;
            amPm = "PM";
        }
        String minuteString;
        minuteString = String.format("%02d", minute);

        return String.format(day + " at " + String.valueOf(hour) + ":" + minuteString + " " + amPm);
    }

    public static Calendar GetSuggestedCalendar(Context context, int suggestedIndex, String department){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        //This will return the date as if it's set for today.
        Date date = new Date();
        date.setHours(GetSuggestedHour(context, suggestedIndex));
        date.setMinutes(GetSuggestedMinute(context, suggestedIndex));
        date.setSeconds(0);
        cal.setTime(date);
        //Check to see if it's for today or the "next" day. If so, get the next available day from the server
        if(!IsTimeAfterCurrentTime(cal)){
            Date nextAvailableDate = GetCallbackServerMediator().GetNextAvailableTime(department);
            cal.add(Calendar.HOUR_OF_DAY, GetHoursInDayIncrementsFromDates(cal.getTime(), nextAvailableDate));
        }
        return cal;
    }

    public static int GetHoursInDayIncrementsFromDates(Date dateOfInterest, Date dateNextAvailable){
        //Subtract 1 millisecond so that if the times are identical, then make sure it gets the correct "next day"
        long difference = Math.abs(dateOfInterest.getTime() - dateNextAvailable.getTime()) - 100;
        long differenceDates = difference / (24 * 60 * 60 * 1000);
        return (int)(differenceDates + 1) * 24;
    }

    public static int GetSuggestedHour(Context context, int suggestedIndex){
        int returnItem;
        Resources resources = context.getResources();

        switch (suggestedIndex){
            case 1: returnItem =  resources.getInteger(R.integer.FirstSuggestedTimeHour);
                break;
            case 2: returnItem =  resources.getInteger(R.integer.SecondSuggestedTimeHour);
                break;
            case 3: returnItem =  resources.getInteger(R.integer.ThirdSuggestedTimeHour);
                break;
            default: returnItem = -1;
                break;
        }
        return returnItem;
    }
    public static int GetSuggestedMinute(Context context, int suggestedIndex){
        int returnItem;
        Resources resources = context.getResources();

        switch (suggestedIndex){
            case 1: returnItem =  resources.getInteger(R.integer.FirstSuggestedTimeMinute);
                break;
            case 2: returnItem =  resources.getInteger(R.integer.SecondSuggestedTimeMinute);
                break;
            case 3: returnItem =  resources.getInteger(R.integer.ThirdSuggestedTimeMinute);
                break;
            default: returnItem = -1;
                break;
        }
        return returnItem;
    }

    public static String GetSuggestedTimeString(Date date) {
        return FormatSuggestedTimeString(GetDayStringFromDate(date),date.getHours(), date.getMinutes());
    }

    public static String GetTimeStringFromDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("h:mm a");
        return df.format(date.getTime());
    }

    public  static  String GetDayStringFromDate(Date date){
        //TODO differentiate from today/tomorrow/different day
        return (String) DateFormat.format("EEEE", date);
    }
    public static void InitializeServerMediator(){
        mCallbackServerMediator = new CallbackServerMediator("CallbackServer");
    }

    public static CallbackServerMediator GetCallbackServerMediator(){
        return mCallbackServerMediator;
    }
}
