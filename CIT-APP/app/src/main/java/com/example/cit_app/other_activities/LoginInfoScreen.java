package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cit_app.R;

public class LoginInfoScreen extends AppCompatActivity {
    private Button beginLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info_screen);
        beginLogin = (Button) findViewById(R.id.startLogin);
        beginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileLogin.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}