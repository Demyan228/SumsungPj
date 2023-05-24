package com.example.sumsungproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SuperAdminActivity extends AppCompatActivity {
    EditText phone, password, punktAddress, nWorkers, is_admin;
    TextView resText;
    DatabaseReference ref;
    String PUNKT_DB_KEY = "Punkt";
    String USER_DB_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);
        phone = findViewById(R.id.editTextPhone);
        password = findViewById(R.id.editTextTextPassword);
        punktAddress = findViewById(R.id.editTextText);
        nWorkers = findViewById(R.id.editTextNumber);
        is_admin = findViewById(R.id.is_admin);
        resText = findViewById(R.id.resText);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    public void addUser(View view) {
        String phone_numb = phone.getText().toString();
        String pass = password.getText().toString();
        String adm_text = is_admin.getText().toString();
        boolean is_adm = false;
        if (adm_text.equals("1")) is_adm = true;
        int hash_pass = pass.hashCode();
        User user = new User(ref.child(USER_DB_KEY).getKey(), phone_numb, is_adm,  hash_pass);
        ref.child(USER_DB_KEY).push().setValue(user);
        phone.setText("");
        password.setText("");
        is_admin.setText("");
        resText.setText("Новый пользователь добавлен");
    }

    public void addPunckt(View view) {
        String addr = punktAddress.getText().toString();
        int works = Integer.valueOf(nWorkers.getText().toString());
        Calendar calendar = Calendar.getInstance();
        Punkt punkt = new Punkt(ref.child(PUNKT_DB_KEY).getKey(), addr, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), works);
        ref.child(PUNKT_DB_KEY).push().setValue(punkt);
        punktAddress.setText("");
        nWorkers.setText("");
        resText.setText("Новый пункт добавлен");
    }
}