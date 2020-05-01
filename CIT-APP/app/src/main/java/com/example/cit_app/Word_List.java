package com.example.cit_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Word_List extends AppCompatActivity {

    private int Record_Audio_permission_Code = 1;
    private int External_Storage_permission_Code = 2;
    TextView word;
    Button record;
    Random rand = new Random();
    int counter = 1;
    ArrayList<Integer> usedInts = new ArrayList<>();
    String[] wordListPlosive, wordListFricative, wordListNasal, wordListSibilant, wordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word__list);
        wordList = new String[10];
        wordListFricative = getResources().getStringArray(R.array.Word_List_Fricatives);
        wordListPlosive = getResources().getStringArray(R.array.Word_List_Plosives);
        wordListNasal = getResources().getStringArray(R.array.Word_List_Nasal);
        wordListSibilant = getResources().getStringArray(R.array.Word_List_Sibilant);
        word = (TextView) findViewById(R.id.word);
        record = (Button) findViewById(R.id.recordButtonWordList);
        for(int i = 0; i < 10; i++) {
            int tmp = -1;
            if(i < 2) {
                while((tmp == -1) || usedInts.contains(tmp)) {
                    tmp = rand.nextInt(63);
                }
                wordList[i] = wordListPlosive[tmp];
                usedInts.add(tmp);
            } else {
                if(i < 4) {
                    while((tmp == -1) || usedInts.contains(tmp)) {
                        tmp = rand.nextInt(10);
                    }
                    wordList[i] = wordListNasal[tmp];
                    usedInts.add(tmp);
                } else {
                    if(i < 6) {
                        while((tmp == -1) || usedInts.contains(tmp)) {
                            tmp = rand.nextInt(10);
                        }
                        wordList[i] = wordListSibilant[tmp];
                        usedInts.add(tmp);
                    } else {
                        while((tmp == -1) || usedInts.contains(tmp)) {
                            tmp = rand.nextInt(14);
                        }
                        wordList[i] = wordListFricative[tmp];
                        usedInts.add(tmp);
                    }
                }
            }
        }

        word.setText(wordList[0]);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestRecordAudioPermission();
                } else {
                    if(ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestExternalStoragePermission();
                    } else {
                        //TODO record sound
                        if(counter < 10) {
                            word.setText(wordList[counter]);
                            counter++;
                        } else {
                            Intent intent = new Intent(v.getContext(), GeneralRepetitionFinished.class);
                            intent.putExtra("exercise", "Word_List");
                            v.getContext().startActivity(intent);
                        }
                    }
                    }
            }
        });

    }

    public void requestRecordAudioPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("For this exercise permission to record your voice and write to your storage is needed").setPositiveButton("ok", new DialogInterface.OnClickListener() {
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

    public void requestExternalStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("For this exercise permission to record your voice and write to your storage is needed").setPositiveButton("ok", new DialogInterface.OnClickListener() {
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
}
