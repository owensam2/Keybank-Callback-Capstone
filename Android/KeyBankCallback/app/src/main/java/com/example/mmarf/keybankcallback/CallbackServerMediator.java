package com.example.mmarf.keybankcallback;

public class CallbackServerMediator {
    String mServerInfo;

    public CallbackServerMediator(String serverInfo){
        mServerInfo = serverInfo;
        ConnectToServer();
    }

    private void ConnectToServer(){
        //TODO Connect

    }

    public int GetEstimatedTimeRemaining(String department){
        //TODO Get time
        return GetOfflineTime();
    }

    public String GetPhoneNumberForDepartment(String department){
        //TODO Get phone number
        return  GetOfflinePhoneNumber();
    }

    private int GetOfflineTime(){
        return 20;
    }
    private String GetOfflinePhoneNumber(){
        return "8005392968";
    }
}
