package com.example.mmarf.keybankcallback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CallbackQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback_questions);

        Button buttonQuestionFradulent = findViewById(R.id.buttonQuestionFradulent);
        buttonQuestionFradulent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent initiateConnectionIntent = new Intent(getApplicationContext(), CallbackActivity.class);
                //Fire off to connection screen
                startActivity(initiateConnectionIntent);
            }
        });

    }
}
