package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

public class CallbackQuestionsActivity extends AppCompatActivity {

    ListView CallbackQuestionsListView;
    String[] ListOfQuestions;
    String[] ListOfDepartments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_questions);
        CallbackHelper.InitializeServerMediator(CallbackQuestionsActivity.this);

        Resources resources = getResources();
        CallbackQuestionsListView = findViewById(R.id.CallbackQuestionsListView);
        ListOfQuestions = resources.getStringArray(R.array.ListOfQuestions);
        ListOfDepartments = resources.getStringArray(R.array.ListOfDepartments);

        CallbackQuestionsItemAdapter callbackQuestionsItemAdapter = new CallbackQuestionsItemAdapter(this, ListOfQuestions,ListOfDepartments);
        CallbackQuestionsListView.setAdapter(callbackQuestionsItemAdapter);

        CallbackQuestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Find out if there is a wait or not. This will determine which screen is chosen next.
                if(CallbackHelper.GetCallbackServerMediator().GetEstimatedTimeRemaining(CallbackHelper.GetDepartmentName(position, CallbackQuestionsActivity.this)) > 0){
                    Intent callbackActivity = new Intent(getApplicationContext(), CallbackActivity.class);
                    callbackActivity.putExtra("KeyBank.CallbackActivity.ITEM_INDEX", position);
                    startActivity(callbackActivity);
                }else{
                    Intent callbackActivity = new Intent(getApplicationContext(), CallbackActivityNoWait.class);
                    callbackActivity.putExtra("KeyBank.CallbackActivity.ITEM_INDEX", position);
                    startActivity(callbackActivity);
                }
            }
        });
    }
}
