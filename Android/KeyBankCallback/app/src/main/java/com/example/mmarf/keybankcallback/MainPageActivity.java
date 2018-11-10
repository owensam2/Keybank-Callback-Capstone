package com.example.mmarf.keybankcallback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button buttonCallbackQuestions = findViewById(R.id.buttonCallbackQuestions);
        buttonCallbackQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO figure out if there is a callback that has been scheduled already and take the user to that page.
                CallbackHelper.TransferToQuestionsActivity(MainPageActivity.this);
            }
        });
    }
}
