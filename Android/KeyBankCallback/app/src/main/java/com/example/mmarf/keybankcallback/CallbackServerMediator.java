package com.example.mmarf.keybankcallback;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

class CallbackServerMediator {
    private String mServerInfo;
    private Date mCallbackDate;
    private boolean mWaitForReponse = false;
    private int mTimoutMs = 1000; //Timeout to wait for response
    private static final String UserID = "AndroidUser1";

    CallbackServerMediator(String serverInfo){
        mServerInfo = serverInfo;
        new AsyncConnectToServer().execute("http://ceclnx01.cec.miamioh.edu:2020/QUEUE_TIME");
    }

    public String GetServerResponse(){
        String returnItem = null;
        Date startTime = Calendar.getInstance().getTime();
        long diffInMs;
        try {
            do{
                Date currentTime = Calendar.getInstance().getTime();
                diffInMs = startTime.getTime() - currentTime.getTime();
                
            }while (diffInMs < mTimoutMs);
        }
        finally {
            mWaitForReponse = false;
        }
        return returnItem;
    }

    private String ConnectToServer(String connectionURL){
        String returnItem = "";
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

    public class AsyncConnectToServer extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            String returnItem;
            returnItem = ConnectToServer(strings[0]);
            return returnItem;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
