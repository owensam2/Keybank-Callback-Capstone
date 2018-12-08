package com.example.mmarf.keybankcallback;

import java.util.Date;

public interface ICallbackServerMediator {
    Date GetNextAvailableTime(String department);
    Date GetServerTime(String department);
    int GetEstimatedMinutesOfQueue(String department);
    String GetPhoneNumberForDepartment(String department);
    void SetUserToNextAvailableCallback(String department);
    int GetEstimatedMinutesOfScheduledCallback(String department);
    void SetCallbackTime(Date date, String department);
    Date GetCallbackTime();
    boolean CancelCallback();
    String GetDepartment();
    void AddToDemoQueue();
    boolean IsOfficeOpen(String department);
}
