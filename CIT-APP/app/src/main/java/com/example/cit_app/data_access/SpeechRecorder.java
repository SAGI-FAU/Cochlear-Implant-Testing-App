/**
 * Created by PKlumpp
 * The SpeechRecorder is implemented as a Singleton to prevent hardware conflicts
 */

package com.example.cit_app.data_access;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.cit_app.R;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SpeechRecorder {
    private static Context CONTEXT;
    private static Handler HANDLER;

    private static SpeechRecorder recorder_instance = null;
    private static AudioRecord AUDIO_RECORD;
    private static int SAMPLING_RATE = 44100;
    private static File AUDIO_FOLDER;
    private static DataOutputStream DATA_OUTPUT_STREAM;
    private static File FILE_PCM;
    private static File FILE_WAV;
    private boolean init = false;

    private int minBufferSize;

    private SpeechRecorder(Context context, Handler handler, String folder){
        CONTEXT = context;
        HANDLER = handler;
        AUDIO_FOLDER = new File(Environment.getExternalStorageDirectory() + File.separator + CONTEXT.getString(R.string.app_name) + File.separator + "AUDIO" + File.separator + folder);
        if(!AUDIO_FOLDER.exists()){
            AUDIO_FOLDER.mkdirs();
        }
        minBufferSize = AudioRecord.getMinBufferSize(SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AUDIO_RECORD = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
        init = true;
    }

    /**
     * Returns an instance of SpeechRecorder
     * @param context The application context
     * @param handler A handler to process volume level updates on the UI thread
     * @return New or existing SpeechRecorder instance
     */
    public static SpeechRecorder getInstance(Context context, Handler handler, String folder) {
        if (recorder_instance == null){
            recorder_instance = new SpeechRecorder(context, handler, folder);
        }
        return recorder_instance;
    }

    /**
     * Prepare the next recording task
     * @param exercise The exercise that is to be recorded
     * @return File name of .wav if successful, error message otherwise
     */
    public String prepare(String exercise){
        String date = getCurrentDateAsString();
        Calendar c = Calendar.getInstance();
        FILE_PCM  = new File(AUDIO_FOLDER.getAbsolutePath() + File.separator + date + "_" + c.getTime() + exercise + ".pcm");
        FILE_WAV  = new File(AUDIO_FOLDER.getAbsolutePath() + File.separator + date + "_" + c.getTime() + exercise + ".wav");
        try {
            FILE_PCM.createNewFile();
        } catch (IOException e) {
            Log.e("SpeechRecorder", e.toString());
            return "ERROR: Could not create file: " + FILE_PCM.getAbsolutePath();
        }
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(FILE_PCM);
        } catch (FileNotFoundException e) {
            Log.e("SpeechRecorder", e.toString());
            return "ERROR: Could not create OutputStream";
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        DATA_OUTPUT_STREAM = new DataOutputStream(bufferedOutputStream);
        return FILE_WAV.getAbsolutePath();
    }

    /**
     * Call record() to start the audio recording
     * @return String whether recording was successful
     */
    public String record(){
        if(init) {
            Thread recordingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    short[] shorts = new short[minBufferSize];
                    AUDIO_RECORD.startRecording();
                    while (AUDIO_RECORD.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                        int size = AUDIO_RECORD.read(shorts, 0, minBufferSize);
                        double max = max(shorts);
                        int maxPercent = (int) (max / Short.MAX_VALUE * 100);
                        Message message = HANDLER.obtainMessage();
                        Bundle data = new Bundle();
                        data.putDouble("Volume", maxPercent);
                        message.setData(data);
                        message.sendToTarget();
                        for (int i = 0; i < size; i++) {
                            try {
                                if (DATA_OUTPUT_STREAM != null)
                                    DATA_OUTPUT_STREAM.writeShort(shorts[i]);
                            } catch (IOException e) {
                                Log.e("SpeechRecorder", e.toString());
                                AUDIO_RECORD.stop();
                                try {
                                    DATA_OUTPUT_STREAM.close();
                                } catch (IOException e1) {
                                    Log.e("SpeechRecorder", e.toString());
                                }
                                return;
                            }
                        }
                    }
                    try {
                        if (DATA_OUTPUT_STREAM != null)
                            DATA_OUTPUT_STREAM.close();
                    } catch (IOException e) {
                        Log.e("SpeechRecorder", e.toString());
                    }
                    WavFileWriter wavFileWriter = new WavFileWriter(SAMPLING_RATE, FILE_PCM, FILE_WAV);
                    wavFileWriter.start();
                    try {
                        wavFileWriter.join();
                    } catch (InterruptedException e) {
                        Log.e("HANDLER ERROR", e.getMessage());
                    }
                    Message message = HANDLER.obtainMessage();
                    Bundle data = new Bundle();
                    data.putString("State", "Finished");
                    if (FILE_WAV != null) {
                        data.putString("File", FILE_WAV.getAbsolutePath());
                        message.setData(data);
                        message.sendToTarget();
                    }
                }
            });
            recordingThread.start();
            return "Recording started";
        }
        return "not started";
    }

    /**
     * Call stopRecording() to stop an ongoing recording process
     * @return String whether stopping was successful
     */
    public String stopRecording(){
        if (AUDIO_RECORD.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING){
            return "Cannot stop recording. Not recording at the moment.";
        }
        AUDIO_RECORD.stop();
        return "Stopped recording";
    }

    public int getSamplingRate() {
        return SAMPLING_RATE;
    }

    public void setSamplingRate(int samplingRate) {
        SAMPLING_RATE = samplingRate;
    }

    public static File getAudioFolder() {
        return AUDIO_FOLDER;
    }

    /**
     * Call release() to clear the SpeechRecorder instance and release the microphone
     */
    public void release(){
        AUDIO_RECORD.release();
        recorder_instance = null;
    }

    private String getCurrentDateAsString() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        return dateFormat.format(date);
    }

    private static double max(short[] m) {
        double max = 0;
        for (int i = 0; i < m.length; i++) {
            if (Math.abs(m[i]) > max) {
                max = Math.abs(m[i]);
            }
        }
        return max;
    }
}
