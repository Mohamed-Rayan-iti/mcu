package com.example.mcu.LocationOwner.homeData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcu.Ip.And.Ordernum.model.control_ip_model;
import com.example.mcu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class retailer_ip_settingActivity extends AppCompatActivity {

    EditText time, timeLift;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_ip_setting);
        String ip_setting = getIntent().getStringExtra("ip");
        Toast.makeText(this, ip_setting, Toast.LENGTH_SHORT).show();

        time = findViewById(R.id.time);
        timeLift = findViewById(R.id.timeleft);

        try {

        } catch (Exception ex) {

            Log.e("exception", ex.toString());
        }

    }

    public void startTimeCountDown(View view) {
        Integer timeMillisecond = Integer.parseInt(time.getText().toString()) * 3600000;
        // Time is in millisecond so 50sec = 50000 I have used
        // countdown Interveal is 1sec = 1000 I have used
        new CountDownTimer(timeMillisecond, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timeLift.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                timeLift.setText("00:00:00");
            }
        }.start();
    }


    public void setControl(String docmentId, String ip, int order, int speed, int hour, String start_time, String end_time) {

//        List<control_ip_model> control_list = new ArrayList<>();
        control_ip_model control_model = new control_ip_model();

        control_model.setEndTime(end_time);
        control_model.setHour(hour);
        control_model.setId(docmentId);
        control_model.setIp(ip);
        control_model.setOrder(order);
        control_model.setStartTime(start_time);
        control_model.setSpeed(speed);

//        control_list.add(control_model);

        db.collection("control").document(docmentId).set(control_model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });


    }

}