package com.example.mmarf.keybankcallback;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.telecom.Call;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

class CallbackServerMediator {
    private String mConnectionURL;
    private boolean isConnected = false;
    private boolean mOfflineMode = true;
    private boolean mOfflineModeCallbackAdded = false;
    private String mCurrentDepartment;
    private Date mCallbackDate;
    private Resources mResources;
    private static String mUserID;
    private static final int mTimeoutMs = 1500;
    private static final int mMaxServerTries = 10;
    private static String mCannotFindCallbackTime;

    String GetDepartment(){return mCurrentDepartment;}

    CallbackServerMediator(String serverInfo, Resources resources){
        mConnectionURL = serverInfo;
        mResources = resources;
        mUserID = mResources.getString(R.string.android_user);
        mCannotFindCallbackTime = mResources.getString(R.string.server_cannot_find_callback_time);
        //Try to get something from the server.
        if(!mOfflineMode){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_next_queue_time));
            if(response != null){
                isConnected = true;
            }
        }
    }

    private String SendCommandReceiveResponse(String command){
        int tries = 0;
        boolean done = false;
        String returnItem = null;
        do{
            AsyncTask task = new AsyncConnectToServer().execute(command);
            try {
                returnItem = (String)task.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(returnItem != null)
                done = true;
            tries += 1;
        }while (!done && tries < mMaxServerTries);
        return returnItem;
    }

    private String ConnectToServer(String connectionURL){
        String returnItem = null;
        try {
            HttpURLConnection connection = null;
            URL url = null;
            url = new URL(connectionURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(mTimeoutMs);
            connection.connect();
            try {
                isConnected = true;
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                returnItem = stringBuilder.toString();
            } finally {
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnItem;
    }

    Date GetNextAvailableTime(String department){
        Date returnDate = null;
        mCurrentDepartment = department;
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_next_queue_time));
            returnDate = ConvertStringToDate(TrimString(response));
        }
        else{
            //Fake out next available time by adding an hour.
            Date nextAvailable = GetOfflineNextAvailableTime(department);
            nextAvailable.setHours(nextAvailable.getHours() + 1);
            return nextAvailable;
        }
        return returnDate;
    }

    Date GetServerTime(String department){
        mCurrentDepartment = department;
        Date returnDate;
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_time));
            returnDate = ConvertStringToDate(TrimString(response));
        }
        else{
            returnDate = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone())).getTime();
        }
        return returnDate;
    }

    int GetEstimatedMinutesOfQueue(String department){
        mCurrentDepartment = department;
        int queueTime;
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_queue_time));
            queueTime = Integer.valueOf(TrimString(response));
        }
        else {
            queueTime = GetOfflineTime(department);
        }
        return queueTime;
    }

    String GetPhoneNumberForDepartment(String department){
        mCurrentDepartment = department;
        //TODO Get phone number
        return  GetOfflinePhoneNumber();
    }

    void SetUserToNextAvailableCallback(String department){
        mCurrentDepartment = department;
        SetCallbackTime(GetNextAvailableTime(department), department);
    }

    int GetEstimatedMinutesOfScheduledCallback(String department){
        mCurrentDepartment = department;
        Date queueTime;
        int returnMinutes;
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_callback_time_server_add_id)  + "&id=" + mUserID);
            queueTime = ConvertStringToDate(TrimString(response));
            returnMinutes = CallbackHelper.GetDateDiffInMinutes(queueTime, GetServerTime(department));
        }
        else {
            returnMinutes = GetOfflineTime(department);
        }
        return returnMinutes;
    }

    void SetCallbackTime(Date date, String department){
        mCurrentDepartment = department;
        if(isConnected){
            String timeBuilder = "&id=" + mUserID + "&day=" + String.valueOf(date.getDay()) + "&hour=" + String.valueOf(date.getHours()) + "&min=" + String.valueOf(date.getMinutes());
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_schedule_callback_id_day_hour_minute)+timeBuilder);
            //TODO: update this if getting a 1 back due to slot already filled?

            try {
                mCallbackDate = ConvertStringToDate(TrimString(response));
            }
            catch (java.lang.NumberFormatException e){
                mCallbackDate = date;
            }
        }
        else
            mOfflineModeCallbackAdded = true;
            mCallbackDate = date;
    }

    Date GetCallbackTime(){
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_callback_time_server_add_id) + "&id=" + mUserID);
            //Cannot find user's time
            if(response == mCannotFindCallbackTime)
                return null;
            try {
                return ConvertStringToDate(TrimString(response));
            }
            catch (java.lang.NumberFormatException e){
                return  mCallbackDate;
            }
        }
        else
            if(mOfflineModeCallbackAdded){
                return  mCallbackDate;
            }
            else
                return null;
    }

    void CancelCallback(){
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_callback_remove_add_id) + "&id=" + mUserID);
        }
        else
            mOfflineModeCallbackAdded = false;
            mCallbackDate = null;
        //TODO Do something with the response?
    }

    void AddToDemoQueue(){
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_add_queue_add_id) + "&id=" + mUserID);
        }
    }

    private Date ConvertStringToDate(String stringDate){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        if(stringDate == null || stringDate.length() == 0){
            return cal.getTime();
        }
        String stringDateArray[] = stringDate.split(" ");
        Date date = new Date();
        if(stringDateArray.length < 3){
            return cal.getTime();
        }
        String minute = stringDateArray[2];
        String hour = stringDateArray[1];
        String day = stringDateArray[0];
        date.setHours(Integer.valueOf(hour));
        date.setMinutes(Integer.valueOf(minute));
        date.setSeconds(0);
        int dayFromString = Integer.valueOf(stringDateArray[0]);
        //If it's Saturday and the next available day is not Saturday, normalize the day
        if(date.getDay() == 6 && dayFromString != 6)
            dayFromString += 6;

        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, (dayFromString - date.getDay()) * 24);
        return cal.getTime();
    }

    private class AsyncConnectToServer extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            String returnItem = ConnectToServer(strings[0]);
            return returnItem;
        }
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

    private String TrimString(String value){
        if(value == null){
            return value;
        }
        value = value.replaceAll("\n","");
        return value;
    }
}
