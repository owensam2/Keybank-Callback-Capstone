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
        return GetOfflineTime(department);
    }

    public String GetPhoneNumberForDepartment(String department){
        //TODO Get phone number
        return  GetOfflinePhoneNumber();
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
