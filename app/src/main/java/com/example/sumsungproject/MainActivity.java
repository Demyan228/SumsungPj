package com.example.sumsungproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    public String USER_ID_KEY= "USER_ID";
    DatabaseReference drRef;
    EditText telephone, password;
    Button enter_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drRef = FirebaseDatabase.getInstance().getReference();
        telephone = findViewById(R.id.edTelephone);
        password = findViewById(R.id.edPass);
        enter_bt = findViewById(R.id.enter_bt);

    }

    public void enter_acc(View view) {
        // проверка корректности введенных данных
        String telNumb = telephone.getText().toString();


        Intent intent = new Intent(MainActivity.this, AcountActivity.class);
        intent.putExtra(USER_ID_KEY, telNumb);
        startActivity(intent);


    }
}