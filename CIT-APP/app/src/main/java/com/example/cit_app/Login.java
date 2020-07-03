package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private int MY_PERMISSIONS_REQUEST_RECORD_AUDIO, MY_PERMISSIONS_REQUEST_WRITE_STORAGE;

    TextView tv_username;
    TextView tv_userid;
    Button bt_create;
    String username;
    String userid;
    PatientDA patientData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_username = findViewById(R.id.username);
        tv_userid = findViewById(R.id.userid);
        bt_create = findViewById(R.id.button_create);
        tv_userid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(bt_create);
                    handled = true;
                }
                return handled;
            }
        });
        setListeners();
    }

    private void setListeners() {
        tv_username.setOnClickListener(this);
        tv_userid.setOnClickListener(this);
        bt_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_create:
                username = tv_username.getText().toString();
                userid = tv_userid.getText().toString();
                if (validate_data()) {
                    open_profile1();
                }
                break;

        }
    }

    public void open_profile1() {
        Intent intent_profile1 = new Intent(this, Profile.class);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("patientData", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("Username", patientData.getUsername());
        edit.putString("Userid", patientData.getGovtId());
        edit.apply();
        startActivity(intent_profile1);
    }

    private boolean validate_data() {
        if (username.isEmpty()) {
            Toast.makeText(this, R.string.user_empty, Toast.LENGTH_SHORT).show();
            return false;
        } else if (userid.isEmpty()) {
            Toast.makeText(this, R.string.userid_empty, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            patientData = new PatientDA(username, userid);
            return true;
        }
    }
}