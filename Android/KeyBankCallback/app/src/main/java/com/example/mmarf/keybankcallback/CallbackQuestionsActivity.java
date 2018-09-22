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

public class CallbackQuestionsActivity extends AppCompatActivity {

    ListView CallbackQuestionsListView;
    String[] ListOfQuestions;
    String[] ListOfDepartments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_questions);

        Resources resources = getResources();
        CallbackQuestionsListView = findViewById(R.id.CallbackQuestionsListView);
        ListOfQuestions = resources.getStringArray(R.array.ListOfQuestions);
        ListOfDepartments = resources.getStringArray(R.array.ListOfDepartments);

        CallbackQuestionsItemAdapter callbackQuestionsItemAdapter = new CallbackQuestionsItemAdapter(this, ListOfQuestions,ListOfDepartments);
        CallbackQuestionsListView.setAdapter(callbackQuestionsItemAdapter);

        CallbackQuestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent callbackActivity = new Intent(getApplicationContext(), CallbackActivity.class);
                callbackActivity.putExtra("KeyBank.CallbackActivity.ITEM_INDEX", ListOfDepartments[position]);
                startActivity(callbackActivity);
                //See 39:12 on the video for continuation.
            }
        });
    }

}
