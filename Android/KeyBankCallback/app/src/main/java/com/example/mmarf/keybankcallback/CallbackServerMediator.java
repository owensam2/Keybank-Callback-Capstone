package com.example.mmarf.keybankcallback;

import android.content.res.Resources;
import android.os.AsyncTask;

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
    private String mConnectionURL = null;
    private boolean isConnected = true;
    private Date mCallbackDate;
    private Resources mResources;
    private static final String UserID = "AndroidUser1";

    CallbackServerMediator(String serverInfo, Resources resources){
        mConnectionURL = serverInfo;
        mResources = resources;
        //String returnItem = SendCommandReceiveResponse(serverInfo);
        String returnItem = SendCommandReceiveResponse("http://ceclnx01.cec.miamioh.edu:2020/QUEUE_TIME");
        if(returnItem != null){
            isConnected = true;
        }
    }

    private String SendCommandReceiveResponse(String command){
        String returnItem = null;
        AsyncTask task = new AsyncConnectToServer().execute(command);
        try {
            returnItem = (String)task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnItem;
    }

    private String ConnectToServer(String connectionURL){
        String returnItem = null;
        try {
            HttpURLConnection connection = null;
            URL url = null;
            url = new URL(connectionURL);
            connection = (HttpURLConnection) url.openConnection();
            try {
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
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_next_queue_time));
            returnDate = ConvertIntToDate(ConvertStringToInt(response));
        }
        else{
            returnDate = GetOfflineNextAvailableTime(department);
        }
        return returnDate;
    }

    int GetEstimatedTimeRemaining(String department){
        int queueTime;
        if(isConnected){
            String response = SendCommandReceiveResponse(mConnectionURL + mResources.getString(R.string.server_queue_time));
            queueTime = ConvertStringToInt(response);
        }
        else {
            queueTime = GetOfflineTime(department);
        }
        return queueTime;
    }

    String GetPhoneNumberForDepartment(String department){
        //TODO Get phone number
        return  GetOfflinePhoneNumber();
    }

    void SetCallbackTime(Date date, String department){
        //TODO: Send callback time to server
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

    private int ConvertStringToInt(String value){
        if(value == null){
            return 0;
        }
        value = value.replaceAll("\\D+","");
        return Integer.valueOf(value);
    }

    private Date ConvertIntToDate(int integerDate){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(CallbackHelper.GetLocalTimeZone()));
        if(integerDate == 0){
            return cal.getTime();
        }
        Date date = new Date();
        String dateString = String.valueOf(integerDate);
        if(dateString.length() < 3){
            return cal.getTime();
        }
        String minute = dateString.substring(dateString.length()-2, dateString.length());
        String hour = dateString.replace(minute, "");
        date.setHours(Integer.valueOf(hour));
        date.setMinutes(Integer.valueOf(minute));
        date.setSeconds(0);
        cal.setTime(date);
        return cal.getTime();
    }

    private class AsyncConnectToServer extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            String returnItem = ConnectToServer(strings[0]);
            return returnItem;
        }
    }
}
