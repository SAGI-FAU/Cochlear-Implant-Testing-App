package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cit_app.R;
import com.example.cit_app.tools.NotificationReceiver;

import java.util.Calendar;
import java.util.Random;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    Spinner SpinnerNotify;
    int TimeNotification;
    SharedPreferences sharedPref;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_update_profile).setOnClickListener(this);
        findViewById(R.id.button_restart_session).setOnClickListener(this);

        SpinnerNotify = findViewById(R.id.SpinnerNotifications);
        String[] hours = new String[]{"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        ArrayAdapter<String> notify_adapter =  new ArrayAdapter<> (this,android.R.layout.simple_spinner_item,hours);
        notify_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerNotify.setAdapter(notify_adapter);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        TimeNotification=sharedPref.getInt("Notification Time", 9);


        if (TimeNotification>=0){
            SpinnerNotify.setSelection(TimeNotification);
        }

        SpinnerNotify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TimeNotification = i;

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("Notification Time", TimeNotification);
                editor.apply();
                Calendar c = Calendar.getInstance();
                SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                int hour = p.getInt("Notification Time", 11);
                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MINUTE, 0);
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent activity = new Intent(view.getContext(), NotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, activity, 0);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_update_profile:
                open_profile();
                break;
            case R.id.button_restart_session:

                restart_session();
        }

    }

    private void open_profile() {
        Intent intent_update =new Intent(this, ProfileLogin.class);
        startActivity(intent_update);
    }



    private void restart_session(){
        int choose = rand.nextInt(2);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("MEM2", choose);
        edit.apply();
        SharedPreferences exercise = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
        SharedPreferences.Editor editor1 = exercise.edit();
        editor1.putBoolean("Word_List", false);
        editor1.apply();
        editor1.putBoolean("SyllableRepetition", false);
        editor1.apply();
        editor1.putBoolean("ReadingOfSentences", false);
        editor1.apply();
        editor1.putBoolean("Picture_Description", false);
        editor1.apply();
        editor1.putBoolean("MinimalPairs", false);
        editor1.apply();
        editor1.putBoolean("MinimalPairs2", false);
        editor1.apply();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
