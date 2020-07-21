package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cit_app.data_access.PatientDA;
import com.example.cit_app.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private int MY_PERMISSIONS_REQUEST_RECORD_AUDIO, MY_PERMISSIONS_REQUEST_WRITE_STORAGE;
    private int Record_Audio_permission_Code = 1;
    private int External_Storage_permission_Code = 2;
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
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        requestRecordAudioPermission();
                    }
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestExternalStoragePermission();
                    }
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        open_profile1();
                    }
                }
                break;

        }
    }

    public void requestExternalStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this).setTitle("Permission needed").setMessage("For this application permission to write to your storage is needed").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, External_Storage_permission_Code);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, External_Storage_permission_Code);
        }
    }

    private void requestRecordAudioPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this).setTitle("Permission needed").setMessage("For this application permission to record your voice is needed").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Record_Audio_permission_Code);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Record_Audio_permission_Code);
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