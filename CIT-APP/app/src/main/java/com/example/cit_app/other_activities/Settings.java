package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cit_app.R;

import java.util.Random;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    Spinner SpinnerNotify;
    int TimeNotification;
    SharedPreferences sharedPref;
    Random rand = new Random();
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(getResources().getString(R.string.Settings)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListeners();
        dialog = new Dialog(this);
        ImageView help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });
    }

    private void showPopUp(View v) {

        dialog.setContentView(R.layout.popup_help);
        TextView text = dialog.findViewById(R.id.close);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setListeners() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_restart_session:
                restart_session();
        }

    }

    private void restart_session(){
        int choose = rand.nextInt(2);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("MEM2", choose);
        edit.apply();
        SharedPreferences exercise = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
        SharedPreferences.Editor editor1 = exercise.edit();
        editor1.putBoolean("Finished", false);
        editor1.apply();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
