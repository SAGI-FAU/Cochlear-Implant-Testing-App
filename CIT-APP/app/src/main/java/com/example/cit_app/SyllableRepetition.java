package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SyllableRepetition extends AppCompatActivity{

    //TODO Add recording of voice
    private TextView timer;
    private ProgressBar progress;
    private Button start;
    private CountDownTimer cdt;
    private Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllable_repetition);
        timer = (TextView) findViewById(R.id.timer);
        progress = (ProgressBar) findViewById(R.id.timerProgress);
        start = (Button) findViewById(R.id.startTimer);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    public void startTimer(){
        cdt = new CountDownTimer(11900, 1000) {
            int count = 10;
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText("" + count);
                progress.setProgress(count * 10);
                count --;
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(c, SyllableRepetitionFinished.class);
                c.startActivity(intent);
            }
        }.start();
    }
}
