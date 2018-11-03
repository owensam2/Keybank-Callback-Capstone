package com.example.mmarf.keybankcallback;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class CallbackServerMediator {
    private String mServerInfo;
    private Date mCallbackDate;
    private String mCallbackDepartment;

    CallbackServerMediator(String serverInfo){
        mServerInfo = serverInfo;
        ConnectToServer();
    }

    private void ConnectToServer(){
        //TODO Connect

    }

    Date GetNextAvailableTime(String department){
        //TODO Get date of next available from server
        return GetOfflineNextAvailableTime(department);
    }

    int GetEstimatedTimeRemaining(String department){
        //TODO Get time
        return GetOfflineTime(department);
    }

    String GetPhoneNumberForDepartment(String department){
        //TODO Get phone number
        return  GetOfflinePhoneNumber();
    }

    void SetCallbackTime(Date date, String department){
        //TODO: Send callback time to server
        mCallbackDepartment = department;
        mCallbackDate = date;
    }

    Date GetCallbackTime(){
        //TODO: Get callback time from server
        return mCallbackDate;
    }

    static void CancelCallback(){
        //TODO: Send the server a cancellation notification
    }

    private Date GetOfflineNextAvailableTime(String department){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        int hour = cal.getTime().getHours();
        //If it's after closing, add a day. If it's the weekend, make sure it's Monday.
        int day = cal.getTime().getDay();
        //Sunday is closed, Saturday is partial.
        if(hour > 17 || day == 0){
            Date date = new Date();
            date.setHours(8);
            date.setMinutes(30);
            date.setSeconds(0);
            cal.setTime(date);
            //If it's saturday, add 48 hours, else, it will just be the next day.
            if (day == 6){
                cal.add(Calendar.HOUR_OF_DAY, 48);
            }
            else{
                cal.add(Calendar.HOUR_OF_DAY, 24);
            }
        }
        return cal.getTime();
    }

    private int GetOfflineTime(String department){
        if(department.contentEquals("Fraudulent Team"))
            return 0;
        else
            return 10;
    }
    private String GetOfflinePhoneNumber(){
        return "8005392968";
    }
}
