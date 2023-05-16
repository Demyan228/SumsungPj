package com.example.sumsungproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AcountActivity extends AppCompatActivity {
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount);
        user_id = getIntent().getIntExtra("USER_ID", 0);
    }
}