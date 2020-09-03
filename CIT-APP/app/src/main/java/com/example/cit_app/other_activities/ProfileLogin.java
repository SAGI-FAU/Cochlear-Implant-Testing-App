package com.example.cit_app.other_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Environment;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cit_app.data_access.CSVFileWriter;
import com.example.cit_app.data_access.PatientDA;
import com.example.cit_app.R;
import com.example.cit_app.data_access.PatientDataService;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileLogin extends AppCompatActivity implements View.OnClickListener {

    private int Record_Audio_permission_Code = 1;
    private int External_Storage_permission_Code = 2;
    private int Camera_permission_Code = 3;
    TextView tv_username;
    TextView et_date;
    EditText et_phone;
    RadioGroup rg_gender, rg_side_of_implant, rg_smoker, rg_implant;
    PatientDA patientData;
    DatePickerDialog datePickerDialog;
    Date birthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle(getResources().getString(R.string.Login)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_date = findViewById(R.id.editTextTextbirthdate);
        tv_username = findViewById(R.id.editTextName);
        et_phone = findViewById(R.id.editTextPhone);
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
        rg_gender = findViewById(R.id.rgsex);
        rg_side_of_implant = findViewById(R.id.rgSideOfImplant);
        rg_implant = findViewById(R.id.rgTypeOfImplant);
        rg_smoker = findViewById(R.id.rgSmoker);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("patientData", 0);
        SharedPreferences.Editor editor = pref.edit();
        String name = tv_username.getText().toString();
        if (name.isEmpty()) {
            return false;
        } else {
            editor.putString("Username", name);
            editor.apply();
        }
        String number = et_phone.getText().toString();
        if (number.isEmpty()) {
            return false;
        } else {
            editor.putString("PhoneNumber", number);
            editor.apply();
        }
        patientData = new PatientDA(name, number);
        int checkedGenderRadioButtonId = rg_gender.getCheckedRadioButtonId();
        if (checkedGenderRadioButtonId == -1) {
            return false;
        } else {
            RadioButton genderRadioButton = findViewById(checkedGenderRadioButtonId);
            editor.putString("Gender", genderRadioButton.getText().toString());
            editor.apply();
            patientData.setGender(genderRadioButton.getText().toString());
        }

        int checkedHandRadioButtonId = rg_side_of_implant.getCheckedRadioButtonId();
        switch (checkedHandRadioButtonId) {
            case R.id.rbRight:
                editor.putString("side_of_implant", "right");
                editor.apply();
                patientData.setSide("right");
                break;
            case R.id.rbLeft:
                editor.putString("side_of_implant", "left");
                editor.apply();
                patientData.setSide("left");
                break;
            case R.id.rbBoth:
                editor.putString("side_of_implant", "both");
                editor.apply();
                patientData.setSide("both");
                break;
            default:
                return false;
        }

        int checkedSmokerRadioButtonId = rg_smoker.getCheckedRadioButtonId();
        switch (checkedSmokerRadioButtonId) {
            case R.id.rbSmoker:
                editor.putBoolean("Smoker", true);
                editor.apply();
                patientData.setSmoker(true);
                break;
            case R.id.rbNoSmoker:
                editor.putBoolean("Smoker", false);
                editor.apply();
                patientData.setSmoker(false);
                break;
            default:
                return false;
        }

        int checkedTypeRadioButtonId = rg_implant.getCheckedRadioButtonId();
        switch (checkedTypeRadioButtonId) {
            case R.id.rbCochlearImplant:
                editor.putString("CochlearImplant", "CochlearImplant");
                editor.apply();
                patientData.setType("CochlearImplant");
                break;
            case R.id.rbHearingAid:
                editor.putString("CochlearImplant", "HearingAid");
                editor.apply();
                patientData.setType("HearingAid");
                break;
            case R.id.rbBothCI:
                editor.putString("CochlearImplant", "BothCI");
                editor.apply();
                patientData.setType("BotCI");
                break;
            default:
                return false;
        }

        String date_string = et_date.getText().toString();
        if (date_string.isEmpty() || date_string.equals("dd/mm/yyyy")) {
            return false;
        } else {
            editor.putString("Birthdate", date_string);
            editor.apply();
            patientData.setBirthday(birthdate);
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
                    SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("UserCreated",13);
                    editor.apply();
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        requestRecordAudioPermission();
                    }
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestExternalStoragePermission();
                    }
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestExternalCameraPermission();
                    }
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        PatientDataService pds = new PatientDataService(getApplicationContext());
                        pds.savePatient(patientData);
                        patientData = pds.getPatient();
                        try {
                        export_profile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(ProfileLogin.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.button_back1:
                onBackPressed();
                break;
            case R.id.editTextTextbirthdate:
                datePickerDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void export_profile()  throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CITA/METADATA/PROFILE/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("Profile", PATH);

        String[] Name={"Name", patientData.getUsername()};
        String[] Birthday={"Birthday", DateFormat.getDateInstance().format(patientData.getBirthday())};
        String[] ID={"Telephone-Number", String.valueOf(patientData.getGovtId())};
        String[] Gender={"Gender", patientData.getGender()};
        String[] Smoker={"Smoker", String.valueOf(patientData.getSmoker())};
        String[] Side={"Side", patientData.getSide()};
        String[] Type={"Type", patientData.getType()};
        mCSVFileWriter.write(Name);
        mCSVFileWriter.write(Birthday);
        mCSVFileWriter.write(ID);
        mCSVFileWriter.write(Gender);
        mCSVFileWriter.write(Smoker);
        mCSVFileWriter.write(Side);
        mCSVFileWriter.write(Type);
        mCSVFileWriter.close();

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

    public void requestExternalCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new androidx.appcompat.app.AlertDialog.Builder(this).setTitle("Permission needed").setMessage("For this application permission to write to your storage is needed").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, Camera_permission_Code);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, Camera_permission_Code);
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
}
