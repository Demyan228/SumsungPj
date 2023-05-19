package com.example.sumsungproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.HashMap;

public class AcountActivity extends AppCompatActivity {
    ListView punktList;
    int user_id;
    public String USER_PH0NE_KEY= "USER_PHONE";
    public String PUNKT_NAME_KEY = "PUNKT_NAME";
    String PUNKT_DB_KEY = "Punkt";
    DatabaseReference punktRef;
    ArrayList<Punkt> punkts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount);
        user_id = getIntent().getIntExtra(USER_PH0NE_KEY, 0);
        punktList = findViewById(R.id.list_view);
        punktRef = FirebaseDatabase.getInstance().getReference(PUNKT_DB_KEY);
        getDataFromDB();
        punktList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AcountActivity.this, PunktActivity.class);
                intent.putExtra(USER_PH0NE_KEY, user_id);
                intent.putExtra(PUNKT_NAME_KEY, punkts.get(position).address);
                startActivity(intent);
            }
        });

    }

    public void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Punkt punkt = ds.getValue(Punkt.class);
                    punkts.add(punkt);
                }
                String[] keyArray = {"address", "day1", "day2", "day3"};
                int [] idArray = {R.id.address, R.id.day1, R.id.day2, R.id.day3};

                ArrayList<HashMap<String, Object>> listForAdapter = new ArrayList<>();
                for (int i = 0; i < punkts.size(); i++) {
                    HashMap<String, Object> bookMap = new HashMap<>();
                    bookMap.put(keyArray[0], punkts.get(i).address);
                    bookMap.put(keyArray[1], R.drawable.free);
                    bookMap.put(keyArray[2], R.drawable.free);
                    bookMap.put(keyArray[3], R.drawable.free);
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