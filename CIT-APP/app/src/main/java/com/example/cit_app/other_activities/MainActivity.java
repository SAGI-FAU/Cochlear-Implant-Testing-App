package com.example.cit_app.other_activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.cit_app.DailyEvaluation;
import com.example.cit_app.ExercisesFragment;
import com.example.cit_app.MainFragment;
import com.example.cit_app.R;
import com.example.cit_app.tools.NotificationReceiver;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("LoginPref", this.MODE_PRIVATE);
        int login = prefs.getInt("UserCreated", 0);
        if (login != 1) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("UserCreated",1);
            editor.commit();
            Intent intent = new Intent(this, Login.class);
            this.startActivity(intent);
        }
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
        Calendar c = Calendar.getInstance();
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        int hour = p.getInt("Notification Time", 9);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent activity = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, activity, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0){
                DailyEvaluation dailyEvaluation = new DailyEvaluation();
                return dailyEvaluation;
            }
            if (position == 2) {
                ExercisesFragment exercises = new ExercisesFragment();
                return exercises;
            }
            return new MainFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "dailyEvaluation";
                case 1:
                    return "profile";
                case 2:
                    return "exercises";
            }
            return null;
        }
    }
}