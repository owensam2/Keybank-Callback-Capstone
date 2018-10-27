package com.example.mmarf.keybankcallback;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CallbackServerMediator {
    String mServerInfo;
    Date mCallbackDate;
    String mCallbackDepartment;

    public CallbackServerMediator(String serverInfo){
        mServerInfo = serverInfo;
        ConnectToServer();
    }

    private void ConnectToServer(){
        //TODO Connect

    }

    public Date GetNextAvailableTime(String department){
        //TODO Get date of next agailable from server
        return GetOfflineNextAvailableTime(department);
    }

    public int GetEstimatedTimeRemaining(String department){
        //TODO Get time
        return GetOfflineTime(department);
    }

    public String GetPhoneNumberForDepartment(String department){
        //TODO Get phone number
        return  GetOfflinePhoneNumber();
    }

    public void SetCallbackTime(Date date, String department){
        //TODO: Send callback time to server
        mCallbackDepartment = department;
        mCallbackDate = date;
    }

    public Date GetCallbackTime(){
        //TODO: Get callback time from server
        return mCallbackDate;
    }

    private Date GetOfflineNextAvailableTime(String department){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        if (cal.getTime().getDay() > 5){
            cal.add(Calendar.HOUR_OF_DAY, 24);
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
