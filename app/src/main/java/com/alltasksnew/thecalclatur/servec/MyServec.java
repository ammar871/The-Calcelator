package com.alltasksnew.thecalclatur.servec;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static com.alltasksnew.thecalclatur.screens.AddObjectActiviy.fileName;

public class MyServec extends Worker {
    private MediaRecorder recorder = null;


    static String filename = "";
    public static boolean isRecorder;

    public MyServec(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    private void startRecording() {
        filename = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        NotiyWorkerManger.notyFication(getApplicationContext());
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("LOG_TAG", Objects.requireNonNull(e.getMessage()));
        }
        recorder.start();


    }

    @NonNull
    @Override
    public Result doWork() {
        startRecording();



        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        stopRecording();
        isRecorder = false;
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        //  tv_recording.setText("");
    }
}



