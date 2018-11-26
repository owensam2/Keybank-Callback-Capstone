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
import android.view.Gravity;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CallbackHelper {

    private static ICallbackServerMediator mCallbackServerMediator;
    private static Date mCustomStartingDate;
    private static boolean mDemoMode = true;
    private static boolean mOfflineMode = true;
    private static final int mMaxCallbackMinuteShow = 60;
    private static final String mToday = "Today";
    private static final String mTomorrow = "Tomorrow";

    static void Call(Context context){
        Resources resources = context.getResources();
        Intent numberToCall = new Intent(Intent.ACTION_CALL);
        //TODO: Update this to the actual phone number
        numberToCall.setData(Uri.parse("tel:" + String.valueOf(CallbackHelper.GetCallbackServerMediator().GetPhoneNumberForDepartment("Fraud"))));
        if(mDemoMode){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            GetCallbackServerMediator().AddToDemoQueue();
            builder.setTitle("DEMO MODE")
                    .setMessage("Added to calling queue")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle(resources.getString(R.string.callback_not_supported_title))
                        .setMessage(resources.getString(R.string.callback_not_supported_message))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog dialog = builder.show();
                TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
                messageView.setGravity(Gravity.CENTER);
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
    }

    static void DisplayYesNoDialogForCancelCallback(final Context context, String title, String message){
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        Resources resources = context.getResources();
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(resources.getString(R.string.real_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CallbackHelper.CancelCallback();
                        CallbackHelper.TransferToMainPage(builder.getContext());
                    }
                })
                .setNegativeButton(resources.getString(R.string.real_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing, Do not want to cancel callback.
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    static String GetDepartmentName(int index, Context context){
        String[] listOfDepartments = context.getResources().getStringArray(R.array.ListOfDepartments);
        return listOfDepartments[index];
    }

    static void TransferToQuestionsActivity(Context context){
        Intent callbackQuestionsActivity = new Intent(context.getApplicationContext(), CallbackQuestionsActivity.class);
        context.startActivity(callbackQuestionsActivity);
    }

    static void TransferToMainPage(Context context){
        Intent mainPageActivity = new Intent(context.getApplicationContext(), MainPageActivity.class);
        context.startActivity(mainPageActivity);
    }

    static void TransferToConformationTimeActivity(Context context, String department){
        Intent callbackScheduleActivity = new Intent(context.getApplicationContext(), CallbackConformationTimeActivity.class);
        callbackScheduleActivity.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(callbackScheduleActivity);
    }

    static void TransferToConformationActivity(Context context, String department){
        Intent callbackScheduleActivity = new Intent(context.getApplicationContext(), CallbackConformationActivity.class);
        callbackScheduleActivity.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(callbackScheduleActivity);
    }

    static void TransferToSuggestionScheduler(Context context, String department){
        Intent SuggestionScheduler = new Intent(context.getApplicationContext(), CallbackSuggestionScheduler.class);
        SuggestionScheduler.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(SuggestionScheduler);
    }

    static void TransferToTimeScheduler(Context context, String department){
        Intent CallbackScheduleActivityTime = new Intent(context.getApplicationContext(), CallbackScheduleActivityTime.class);
        CallbackScheduleActivityTime.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(CallbackScheduleActivityTime);
    }

    static void TransferToCustomScheduler(Context context, String department){
        Intent SuggestionScheduler = new Intent(context.getApplicationContext(), CallbackScheduleActivity.class);
        SuggestionScheduler.putExtra("KeyBank.CallbackConformationActivity.DEPARTMENT", department);
        context.startActivity(SuggestionScheduler);
    }

    static int GetDateDiffInMinutes(Date dateOlder, Date dateYounger){
        Calendar queueTimeCal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        Calendar currentTimeCal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        queueTimeCal.setTime(dateOlder);
        currentTimeCal.setTime(dateYounger);
        long milliSecDifference = queueTimeCal.getTimeInMillis() - currentTimeCal.getTimeInMillis();
        return (int)(milliSecDifference / (60*1000));
    }

    static void TransferToCorrectAreaByCallbackTime(Context context){
        if(mCallbackServerMediator == null){
            TransferToQuestionsActivity(context);
        }
        else{
            //See if there is anything scheduled
            Date callbackDate = mCallbackServerMediator.GetCallbackTime();
            if(callbackDate == null){
                TransferToQuestionsActivity(context);
            }
            else{
                //Figure what screen to display. If the call is less than an hour away, display a minute counter.
                if(mCallbackServerMediator.GetEstimatedMinutesOfScheduledCallback(mCallbackServerMediator.GetDepartment()) < mMaxCallbackMinuteShow){
                    TransferToConformationActivity(context, mCallbackServerMediator.GetDepartment());
                }
                else{
                    TransferToConformationTimeActivity(context, mCallbackServerMediator.GetDepartment());
                }
            }
        }
    }

    private static void CancelCallback(){
        //Cancel the callback in the server.
        mCallbackServerMediator.CancelCallback();
    }

    static String GetLocalTimeZone(){
        return "EST";
    }

    static Date GetNextAvailableDayForDepartment(String department){
        //Returns the opening time and day available for the department.
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        Date date = GetNextAvailableTimeForDepartment(department);
        cal.setTime(date);
        int hoursToAdd = GetHoursInDayIncrementsFromDates(cal.getTime(), date);
        if(hoursToAdd > 0){
            cal.add(Calendar.HOUR_OF_DAY, hoursToAdd);
            cal.setTime(ResetToOpeningTime(cal.getTime()));
        }
        return cal.getTime();
    }

    static Date GetNextAvailableTimeForDepartment(String department){
        return mCallbackServerMediator.GetNextAvailableTime(department);
    }

    private static String FormatSuggestedTimeString(String day, int hour, int minute){
        String amPm = "AM";
        if(hour > 12){
            hour = hour - 12;
            amPm = "PM";
        }
        String minuteString;
        minuteString = String.format("%02d", minute);

        return day + " at " + String.valueOf(hour) + ":" + minuteString + " " + amPm;
    }

    static Calendar GetSuggestedCalendar(Context context, int suggestedIndex, String department){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        //This will return the date as if it's set for today.
        Date date = new Date();
        date.setHours(GetSuggestedHour(context, suggestedIndex));
        date.setMinutes(GetSuggestedMinute(context, suggestedIndex));
        date.setSeconds(0);
        cal.setTime(date);
        //Check to see if it's for today or the "next" day. If so, get the next available day from the server
        Date nextAvailableDate = GetCallbackServerMediator().GetNextAvailableTime(department);
        if(cal.getTime().before(nextAvailableDate)){
            cal.add(Calendar.HOUR_OF_DAY, GetHoursInDayIncrementsFromDates(cal.getTime(), nextAvailableDate));
        }
        return cal;
    }

    private static Date ResetToOpeningTime(Date sentDate){
        Date returnDate = sentDate;
        returnDate.setMinutes(0);
        returnDate.setSeconds(0);
        returnDate.setHours(8);
        return returnDate;
    }

    private static int GetHoursInDayIncrementsFromDates(Date dateOfInterest, Date dateNextAvailable){
        //Subtract 1 millisecond so that if the times are identical, then make sure it gets the correct "next day"
        long difference = Math.abs(dateOfInterest.getTime() - dateNextAvailable.getTime()) - 100;
        long differenceDates = difference / (24 * 60 * 60 * 1000);
        int differenceDay = (int)differenceDates;
        return (differenceDay + 1) * 24;
    }

    private static int GetSuggestedHour(Context context, int suggestedIndex){
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
    private static int GetSuggestedMinute(Context context, int suggestedIndex){
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

    static String GetSuggestedTimeString(Date date) {
        return FormatSuggestedTimeString(GetDayStringFromDate(date),date.getHours(), date.getMinutes());
    }

    static String GetTimeStringFromDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("h:mm a");
        return df.format(date.getTime());
    }

    static String GetDayStringFromDate(Date date){
        Date currentTime =  Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone())).getTime();
        //Today
        if(currentTime.getDay() == date.getDay()){
            return mToday;
        }
        //Tomorrow
        else if(currentTime.getDay() == date.getDay() - 1){
            return mTomorrow;
        }
        //Tomorrow, but the current day is end of week Saturday (6), but this would be rare to hit.
        else if(currentTime.getDay() - 7 == date.getDay() - 1){
            return mTomorrow;
        }
        //Return the actual day name
        else{
            return (String) DateFormat.format("EEEE", date);
        }
    }
    static void InitializeServerMediator(Context context){
        Resources resources = context.getResources();
        if(mOfflineMode){
            mCallbackServerMediator = new CallbackServerMediatorOffline(context.getResources());
        }
        else{
            mCallbackServerMediator = new CallbackServerMediator(resources.getString(R.string.server_connection), context.getResources());
        }
    }

    static ICallbackServerMediator GetCallbackServerMediator(){
        return mCallbackServerMediator;
    }

    static void SetupStartingDateForCustomScheduler(Date date){
        mCustomStartingDate = date;
    }

    static Date GetCustomStartingDate(){
        return mCustomStartingDate;
    }

    static Date UpdateCustomDate(String dateString){
        Date date = StringToDate(dateString, "hh:mm a");
        mCustomStartingDate.setHours(date.getHours());
        mCustomStartingDate.setMinutes(date.getMinutes());
        return mCustomStartingDate;
    }

    private static Date StringToDate(String aDate,String aFormat) {
        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;
    }

    static Date RoundToNextQuarterOfHour(Date date, boolean forceIncrement){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int unRoundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unRoundedMinutes % 15;
        if(!forceIncrement && mod == 0){
            mod = 15;
        }
        calendar.add(Calendar.MINUTE, 15-mod);
        return calendar.getTime();
    }
}
