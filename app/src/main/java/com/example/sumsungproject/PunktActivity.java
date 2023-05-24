package com.example.sumsungproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class PunktActivity extends AppCompatActivity {
    TextView d1, d2, d3, d4, d5, d6, d7;
    TextView punktName;
    LinearLayout sched;
    Punkt punkt;
    DatabaseReference punktRef;
    String PUNKT_DB_KEY = "Punkt";

    public String USER_PH0NE_KEY= "USER_PHONE";
    public  String IS_USER_ADMIN_KEY = "IS_ADMIN";
    public String PUNKT_NAME_KEY = "PUNKT_NAME";

    String user_phone, punktKey;
    boolean is_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkt);
        punktRef = FirebaseDatabase.getInstance().getReference(PUNKT_DB_KEY);
        Intent intent = getIntent();
        user_phone =intent.getStringExtra(USER_PH0NE_KEY);
        is_admin = intent.getBooleanExtra(IS_USER_ADMIN_KEY, false);
        punktKey = intent.getStringExtra(PUNKT_NAME_KEY);
        punktName = findViewById(R.id.punkt_name);
        sched = findViewById(R.id.shed);
        punktRef.child(punktKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    punkt = task.getResult().getValue(Punkt.class);
                    punktName.setText(punkt.address);
                    set_dates();
                    change_data(d1);
                }
            }
        });

    }

    public void set_dates(){
        d1 = findViewById(R.id.d1);
        d2 = findViewById(R.id.d2);
        d3 = findViewById(R.id.d3);
        d4 = findViewById(R.id.d4);
        d5 = findViewById(R.id.d5);
        d6 = findViewById(R.id.d6);
        d7 = findViewById(R.id.d7);
        TextView[] textViews = {d1, d2, d3, d4, d5, d6, d7};
        Calendar calendar = Calendar.getInstance();
        for (TextView d: textViews) {
            d.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE));
            calendar.add(Calendar.DATE, 1);
        }
    }

    public void check_dates(){
        Calendar calendar = Calendar.getInstance();
        int d = dateMinus(calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), punkt.last_up_m, punkt.last_up_d);
        for (int i=0; i < d; i++){
            HashMap<String, HashMap<String, String>> tele = new HashMap<>();
            for (int j = 8; j <= 22; j++) {
                HashMap<String, String> tel = new HashMap<>();
                for (int k = 1; k <= punkt.n_workers; k++) {
                    tel.put("i" + k, "0");
                }
                tele.put("i" + j, tel);

            }
            punkt.telephones.put("i" + i, tele);
        }
        punkt.last_up_m = calendar.get(Calendar.MONTH);
        punkt.last_up_d = calendar.get(Calendar.DATE);
        punktRef.child(punktKey).removeValue();
        punktRef.child(punktKey).setValue(punkt);

    }

    public int dateMinus(int m1, int d1, int m2, int d2){
        // the best method name  )
        // and govnocode again (
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.MONTH, m1);
        calendar1.set(Calendar.DATE, d1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.MONTH, m2);
        calendar2.set(Calendar.DATE, d2);
        int d = (int) ((calendar1.getTimeInMillis() - calendar2.getTimeInMillis()) / (24* 60 * 60 * 1000));
        return d;
    }

    public void change_data(View view) {
        // ниже все можно было и поакуратней написать, как и весь проект, но что поделать за 4 дня ток это
        // да и впринципе https://www.youtube.com/watch?v=IJmStQc9sb4

        TextView date_text = (TextView) view;
        String[] m_d;
        m_d = date_text.getText().toString().split("/");
        check_dates();
        int l = dateMinus(Integer.valueOf(m_d[0]) - 1, Integer.valueOf(m_d[1]), punkt.last_up_m, punkt.last_up_d);
        HashMap<String, HashMap<String, String>>  schedule = punkt.telephones.get("i" + l);
        TableLayout tableLayout = new TableLayout( this);
        TableRow tableRow = new TableRow(this);
        TextView date = new TextView(this);
        date.setText("время\\места");
        date.setGravity(Gravity.CENTER);
        tableRow.addView(date);
        for (int i=1; i <= punkt.n_workers; i++){
            TextView textView = new TextView(this);
            textView.setText("работник " + i);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(20, 0, 20, 0);
            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);
        for (int j=8; j <= 22;j++){
            tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            String time = j + ":00";
            date = new TextView(this);
            date.setText(time);
            date.setGravity(Gravity.CENTER);
            tableRow.addView(date);
            for (int i=1; i <=punkt.n_workers; i++){
                int imgr = R.drawable.busy;
                if (schedule.get("i" + j).get("i" + i).equals("0")) imgr = R.drawable.free;
                if (schedule.get("i" + j).get("i" + i).equals(user_phone)) imgr = R.drawable.mine;
                ImageButton is_free = new ImageButton(this);
                Bitmap img = BitmapFactory.decodeResource(this.getResources(), imgr);
                img = Bitmap.createScaledBitmap(img, 70, 70, false);
                is_free.setImageBitmap(img);
                is_free.setBackgroundColor(255);
                int finalJ = j;
                int finalI = i;
                is_free.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (is_admin) return;
                        String tel = punkt.telephones.get("i" + l).get("i" + finalJ).get("i" + finalI);
                        if (tel.equals(user_phone)) {
                            ImageButton im = (ImageButton) v;
                            Bitmap img = BitmapFactory.decodeResource(PunktActivity.this.getResources(), R.drawable.free);
                            img = Bitmap.createScaledBitmap(img, 70, 70, false);
                            im.setImageBitmap(img);
                            punkt.telephones.get("i" + l).get("i" + finalJ).put("i" + finalI, "0");
                            punktRef.child(punktKey).removeValue();
                            punktRef.child(punktKey).setValue(punkt);
                            return;
                        }
                        if (tel.equals("0") && punkt.telephones.get("i" + l).get("i" + finalJ).containsValue(user_phone)){
                            Toast.makeText(PunktActivity.this, "Вы уже зарегистрировались на это время", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (tel.equals("0")) {
                            ImageButton im = (ImageButton) v;
                            Bitmap img = BitmapFactory.decodeResource(PunktActivity.this.getResources(), R.drawable.mine);
                            img = Bitmap.createScaledBitmap(img, 70, 70, false);
                            im.setImageBitmap(img);
                            punkt.telephones.get("i" + l).get("i" + finalJ).put("i" + finalI, user_phone);
                            punktRef.child(punktKey).removeValue();
                            punktRef.child(punktKey).setValue(punkt);
                        }
                        else {
                            Toast.makeText(PunktActivity.this, "Это время занято", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                tableRow.addView(is_free);
            }
            tableLayout.addView(tableRow);
        }
        sched.removeAllViews();
        sched.addView(tableLayout);
    }
}