package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CallbackQuestionsActivity extends AppCompatActivity {

    ListView CallbackQuestionsListView;
    String[] ListOfQuestions;
    String[] ListOfDepartments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_questions);
        CallbackHelper.InitializeServerMediator(CallbackQuestionsActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Resources resources = getResources();
        CallbackQuestionsListView = findViewById(R.id.CallbackQuestionsListView);
        ListOfQuestions = resources.getStringArray(R.array.ListOfQuestions);
        ListOfDepartments = resources.getStringArray(R.array.ListOfDepartments);

        CallbackQuestionsItemAdapter callbackQuestionsItemAdapter = new CallbackQuestionsItemAdapter(this, ListOfQuestions,ListOfDepartments);
        CallbackQuestionsListView.setAdapter(callbackQuestionsItemAdapter);

        CallbackQuestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Find out if there is a wait or not. This will determine which screen is chosen next. Make sure it's not closed.
                if(CallbackHelper.GetCallbackServerMediator().GetEstimatedMinutesOfQueue(CallbackHelper.GetDepartmentName(position, CallbackQuestionsActivity.this)) > 0 &&
                  (CallbackHelper.GetCallbackServerMediator().IsOfficeOpen(CallbackHelper.GetDepartmentName(position, CallbackQuestionsActivity.this)))) {
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
