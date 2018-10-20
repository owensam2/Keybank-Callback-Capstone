package com.example.mmarf.keybankcallback;

import java.util.Date;

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
