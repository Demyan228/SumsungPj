package com.example.sumsungproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AcountActivity extends AppCompatActivity {
    ListView punktList;
    String user_id;
    public String USER_PH0NE_KEY= "USER_PHONE";
    public  String IS_USER_ADMIN_KEY = "IS_ADMIN";
    public String PUNKT_NAME_KEY = "PUNKT_NAME";
    String PUNKT_DB_KEY = "Punkt";
    DatabaseReference punktRef;
    ArrayList<Punkt> punkts = new ArrayList<>();
    ArrayList<String> punktsKeys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount);
        user_id = getIntent().getStringExtra(USER_PH0NE_KEY);
        punktList = findViewById(R.id.list_view);
        punktRef = FirebaseDatabase.getInstance().getReference(PUNKT_DB_KEY);
        getDataFromDB();
        punktList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AcountActivity.this, PunktActivity.class);
                intent.putExtra(USER_PH0NE_KEY, user_id);
                intent.putExtra(IS_USER_ADMIN_KEY, getIntent().getBooleanExtra(IS_USER_ADMIN_KEY, false));
                intent.putExtra(PUNKT_NAME_KEY, punktsKeys.get(position));
                startActivity(intent);

            }
        });

    }

    public void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                punkts.clear();
                punktsKeys.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Punkt punkt = ds.getValue(Punkt.class);
                    punktsKeys.add(ds.getKey());
                    punkts.add(punkt);
                }
                String[] keyArray = {"address", "day1", "day2", "day3"};
                int [] idArray = {R.id.address, R.id.day1, R.id.day2, R.id.day3};

                ArrayList<HashMap<String, Object>> listForAdapter = new ArrayList<>();
                for (int i = 0; i < punkts.size(); i++) {
                    HashMap<String, Object> bookMap = new HashMap<>();
                    bookMap.put(keyArray[0], punkts.get(i).address);
                    for (int j=1; j<=3;j++) {
                        boolean flag = true;
                        for (HashMap<String, String> map:punkts.get(i).telephones.get("i" + (j - 1)).values()){
                            if (map.containsValue("0")) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) bookMap.put(keyArray[j], R.drawable.busy);
                        else bookMap.put(keyArray[j], R.drawable.free);
                    }
                    listForAdapter.add(bookMap);
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(AcountActivity.this,
                        listForAdapter, R.layout.punkt_list_el, keyArray, idArray);
                punktList.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        punktRef.addValueEventListener(vListener);
    }
}