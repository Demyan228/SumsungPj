package com.example.sumsungproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PunktActivity extends AppCompatActivity {
    TextView d1, d2, d3, d4, d5, d6, d7;
    TextView punktName;
    LinearLayout sched;

    public String USER_PH0NE_KEY= "USER_PHONE";
    public String PUNKT_NAME_KEY = "PUNKT_NAME";

    String punkt_name, user_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punkt);
        Intent intent = getIntent();
        user_phone =intent.getStringExtra(USER_PH0NE_KEY);
        punkt_name = intent.getStringExtra(PUNKT_NAME_KEY);
        punktName = findViewById(R.id.punkt_name);
        sched =  findViewById(R.id.shed);

        punktName.setText(punkt_name);
        set_dates();

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

    public void change_data(View view) {
        TableLayout tableLayout = new TableLayout( this);
        TableRow tableRow = new TableRow(this);
        TextView date = new TextView(this);
        date.setText("время\\места");
        date.setGravity(Gravity.CENTER);
        tableRow.addView(date);
        for (int i=1; i <= 3; i++){
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
            for (int i=1; i <=3; i++){
                ImageButton is_free = new ImageButton(this);
                Bitmap img = BitmapFactory.decodeResource(this.getResources(), R.drawable.free);
                img = Bitmap.createScaledBitmap(img, 70, 70, false);
                is_free.setImageBitmap(img);
                is_free.setBackgroundColor(255);
                is_free.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageButton i = (ImageButton) v;
                        Bitmap img = BitmapFactory.decodeResource(PunktActivity.this.getResources(), R.drawable.mine);
                        img = Bitmap.createScaledBitmap(img, 70, 70, false);
                        is_free.setImageBitmap(img);
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