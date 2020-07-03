package com.example.cit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    TextView tv_username;
    TextView et_date;
    RadioGroup rg_gender, rg_side_of_implant, rg_smoker, rg_implant;
    PatientDA patientData;
    DatePickerDialog datePickerDialog;
    Date birthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        et_date = findViewById(R.id.age_create);
        tv_username = findViewById(R.id.username_create);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("patientData", 0);
        SharedPreferences.Editor editor = pref.edit();

        if (!pref.getString("Username", "noname").equals("noname"))
        {
            tv_username.setText(pref.getString("Username", "noname"));
        }

        et_date.setText("dd/mm/yyyy");
        et_date.setOnClickListener(this);
        datePickerDialog = new DatePickerDialog(this, AlertDialog.BUTTON_NEUTRAL, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(mYear, mMonth, mDay);
                birthdate = calendar.getTime();
                et_date.setText(DateFormat.getDateInstance().format(birthdate));
            }
        }, 1970, 1, 1);

        findViewById(R.id.button_continue).setOnClickListener(this);
        findViewById(R.id.button_back1).setOnClickListener(this);
    }

    private boolean parseData() {
        rg_gender = findViewById(R.id.gender_radio);
        rg_side_of_implant = findViewById(R.id.rdSide_of_implant);
        rg_implant = findViewById(R.id.rdImplant);
        rg_smoker = findViewById(R.id.smoker_radio);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("patientData", 0);
        SharedPreferences.Editor editor = pref.edit();

        int checkedGenderRadioButtonId = rg_gender.getCheckedRadioButtonId();
        if (checkedGenderRadioButtonId == -1) {
            return false;
        } else {
            RadioButton genderRadioButton = findViewById(checkedGenderRadioButtonId);
            editor.putString("Gender", genderRadioButton.getText().toString());
            editor.apply();
        }

        int checkedHandRadioButtonId = rg_side_of_implant.getCheckedRadioButtonId();
        switch (checkedHandRadioButtonId) {
            case R.id.rbRight:
                editor.putString("side_of_implant", "right");
                editor.apply();
                break;
            case R.id.rbLeft:
                editor.putString("side_of_implant", "left");
                editor.apply();
                break;
            case R.id.rbBeides:
                editor.putString("side_of_implant", "both");
                editor.apply();
                break;
            default:
                return false;
        }

        int checkedSmokerRadioButtonId = rg_smoker.getCheckedRadioButtonId();
        switch (checkedSmokerRadioButtonId) {
            case R.id.rbYess:
                editor.putBoolean("Smoker", true);
                editor.apply();
                break;
            case R.id.rbNos:
                editor.putBoolean("Smoker", false);
                editor.apply();
                break;
            default:
                return false;
        }

        String date_string = et_date.getText().toString();
        if (date_string.isEmpty() || date_string.equals("dd/mm/yyyy")) {
            return false;
        } else {
            editor.putString("Birthdate", birthdate.toString());
            editor.apply();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_continue:
                boolean parseSuccess = parseData();
                if (!parseSuccess) {
                    Toast.makeText(getApplicationContext(), "Some information is still missing!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Profile.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.button_back1:
                onBackPressed();
                break;
            case R.id.age_create:
                datePickerDialog.show();
        }
    }

    private void export_profile()  throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CIT/METADATA/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("Profile", PATH);

        String[] Name={"Name", patientData.getUsername()};
        String[] Birthday={"Birthday", DateFormat.getDateInstance().format(patientData.getBirthday())};
        String[] ID={"Telephone-Number", String.valueOf(patientData.getGovtId())};
        String[] Gender={"Gender", patientData.getGender()};
        String[] Smoker={"Smoker", String.valueOf(patientData.getSmoker())};
        mCSVFileWriter.write(Name);
        mCSVFileWriter.write(Birthday);
        mCSVFileWriter.write(ID);
        mCSVFileWriter.write(Gender);
        mCSVFileWriter.write(Smoker);
        mCSVFileWriter.close();

    }
}
