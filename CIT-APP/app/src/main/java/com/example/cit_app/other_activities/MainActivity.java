package com.example.cit_app.other_activities;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.cit_app.Model;
import com.example.cit_app.R;
import com.example.cit_app.tools.Adapter;
import com.example.cit_app.tools.NotificationReceiver;
import com.example.cit_app.tools.Notifier;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView profile, settings;
    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        int login = prefs.getInt("UserCreated", 0);
        login = 0;
        if (login == 0) {
            Intent intent = new Intent(this, LoginInfoScreen.class);
            this.startActivity(intent);
        }
        profile = (CircleImageView) findViewById(R.id.profile_pic);
        settings = (CircleImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Settings.class);
                v.getContext().startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileMain.class);
                v.getContext().startActivity(intent);
            }
        });
        File filepath = Environment.getExternalStorageDirectory();// + "/CITA/PROFILE/profile_pic.jpg";
        File dir = new File(filepath.getAbsolutePath() + "/CITA/PROFILE/");
        dir.mkdir();
        File file = new File(dir, "current_profile_pic.jpg");
        if(file.exists()) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(Uri.fromFile(file));
                BitmapFactory.decodeStream(inputStream);
                profile.setImageURI(Uri.fromFile(file));
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Locale.getDefault().getDisplayLanguage().equals("Deutsch")) {
            models = new ArrayList<>();
            models.add(new Model(R.drawable.trainingset_background, "Daily Session", getResources().getString(R.string.trainingsetDesc)));
            models.add(new Model(R.drawable.exercises_background, "Exercise List", getResources().getString(R.string.ExerciseListDesc)));
            models.add(new Model(R.drawable.daily_results_background, "Daily Results", getResources().getString(R.string.dailyResultDesc)));
            models.add(new Model(R.drawable.evaluation_over_time_background, "Course of the year", getResources().getString(R.string.evaluationOverTimeDesc)));
        } else {
            models = new ArrayList<>();
            models.add(new Model(R.drawable.uebungseinheit_background, "Tägliche Einheit", getResources().getString(R.string.trainingsetDesc)));
            models.add(new Model(R.drawable.uebungen_background, "Übungen", getResources().getString(R.string.ExerciseListDesc)));
            models.add(new Model(R.drawable.taeglicher_fortschritt_background, "Ergebnis des Tages", getResources().getString(R.string.dailyResultDesc)));
            models.add(new Model(R.drawable.fortschritt_ueber_die_zeit, "Verlauf des Jahres", getResources().getString(R.string.evaluationOverTimeDesc)));
        }
        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setAlarm();
    }

    public void setAlarm() {
        Calendar c = Calendar.getInstance();
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        int hour = p.getInt("Notification Time", 9);
        Notifier notifier = new Notifier(this);
        notifier.setReminder(this, NotificationReceiver.class, hour, 0);
    }
}