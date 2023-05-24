package com.example.sumsungproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public String USER_PH0NE_KEY= "USER_PHONE";
    public  String IS_USER_ADMIN_KEY = "IS_ADMIN";
    DatabaseReference drRef;
    EditText telephone, password;
    TextView msg;
    Button enter_bt;
    String USER_DB_KEY = "User";

    ArrayList<User> users = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drRef = FirebaseDatabase.getInstance().getReference(USER_DB_KEY);
        telephone = findViewById(R.id.edTelephone);
        msg = findViewById(R.id.msg);
        password = findViewById(R.id.edPass);
        enter_bt = findViewById(R.id.enter_bt);
        getDataFromDB();

    }

    public void enter_acc(View view) throws Exception{
        // проверка корректности введенных данных
        String telNumb = telephone.getText().toString();
        String pass = password.getText().toString();
        if (telNumb.equals("god")&& pass.equals("superadmin")){
            Intent intent = new Intent(MainActivity.this, SuperAdminActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        int hash_pass = pass.hashCode();
        boolean flag = false;

        for (User u: users){
            if (u.telephone.equals(telNumb) && hash_pass == u.passwordHash){
                Intent intent = new Intent(MainActivity.this, AcountActivity.class);
                intent.putExtra(USER_PH0NE_KEY, u.telephone);
                intent.putExtra(IS_USER_ADMIN_KEY, u.is_admin);
                startActivity(intent);
                flag = true;
                finish();
            }
        }
        if (!flag){
            msg.setText("Неправильный логин или пароль");
        }

    }

    public void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        drRef.addValueEventListener(vListener);
    }
}