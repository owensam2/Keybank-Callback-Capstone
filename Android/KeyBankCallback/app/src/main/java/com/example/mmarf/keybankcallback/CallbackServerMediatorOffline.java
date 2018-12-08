package com.example.mmarf.keybankcallback;

import android.content.res.Resources;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CallbackServerMediatorOffline implements ICallbackServerMediator {
    private boolean mOfflineModeCallbackAdded = false;
    private String mCurrentDepartment;
    private Date mCallbackDate;
    private boolean mOfficesOpenOffline = true;

    CallbackServerMediatorOffline(Resources resources){
        //mResources = resources;
    }

    public String GetDepartment(){return mCurrentDepartment;}

    public Date GetNextAvailableTime(String department){
        //Fake out next available time by adding an hour.
        Date nextAvailable = GetOfflineNextAvailableTime(department);
        nextAvailable.setHours(nextAvailable.getHours() + 1);
        return nextAvailable;
    }

    public Date GetServerTime(String department){
        mCurrentDepartment = department;
        Date returnDate;
        returnDate = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone())).getTime();
        return returnDate;
    }

    public int GetEstimatedMinutesOfQueue(String department){
        mCurrentDepartment = department;
        int queueTime;
        queueTime = GetOfflineTime(department);
        return queueTime;
    }

    public String GetPhoneNumberForDepartment(String department){
        mCurrentDepartment = department;
        return  GetOfflinePhoneNumber();
    }

    public void SetUserToNextAvailableCallback(String department){
        mCurrentDepartment = department;
        SetCallbackTime(GetNextAvailableTime(department), department);
    }

    public int GetEstimatedMinutesOfScheduledCallback(String department){
        int returnMinutes;
        returnMinutes = GetOfflineTime(department);
        return returnMinutes;
    }

    public void SetCallbackTime(Date date, String department){
        mOfflineModeCallbackAdded = true;
        mCallbackDate = date;
    }

    public Date GetCallbackTime(){
        if(mOfflineModeCallbackAdded){
            return  mCallbackDate;
        }
        else
            return null;
    }

    public boolean CancelCallback(){
        mOfflineModeCallbackAdded = false;
        mCallbackDate = null;
        return true;
    }

    @Override
    public boolean IsOfficeOpen(String department) {
        return mOfficesOpenOffline;
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
        if(department.contentEquals("Fraudulent Team") && mCallbackDate == null)
            return 0;
        else
        if(mCallbackDate != null){
            return CallbackHelper.GetDateDiffInMinutes(mCallbackDate, GetServerTime(department));
        }
        else{
            return 10;
        }
    }

    private String GetOfflinePhoneNumber(){
        return "8005392968";
    }

    public void AddToDemoQueue(){
        //Do nothing. Only for online demo mode.
    }
}
