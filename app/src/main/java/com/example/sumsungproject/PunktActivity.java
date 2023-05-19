package com.example.sumsungproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class PunktActivity extends AppCompatActivity {

    public String USER_ID_KEY= "USER_ID";
    public String PUNKT_ID_KEY = "PUNKT_ID";

    int punktId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkt);

        Intent intent = getIntent();
        userId =intent.getIntExtra(USER_ID_KEY, 0);
        punktId = intent.getIntExtra(PUNKT_ID_KEY, 0);


    }
}