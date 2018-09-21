package com.example.mmarf.keybankcallback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CallbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);

        Button buttonInitiateConnection = findViewById(R.id.buttonInitiateConnection);
        buttonInitiateConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Test of GIT Hub pushing
            }
        });
    }


}
