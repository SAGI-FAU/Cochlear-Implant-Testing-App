package com.example.cit_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    Intent intent = getIntent();
    Spinner SpinnerNotify;
    int TimeNotification;
    SharedPreferences sharedPref;
    EditText et_password;
    TextView wrong_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        Intent intent_update =new Intent(this, Profile.class);
        startActivity(intent_update);
    }



    private void restart_session(){

    }
}
