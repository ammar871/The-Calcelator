package com.alltasksnew.thecalclatur.shardeditor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.databinding.FragmentRecordingBinding;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class RecordingFragment extends AppCompatDialogFragment {
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static String fileName = null;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    onSomeEventListener someEventListener;
    public interface onSomeEventListener {
        public void someEvent(String s);
    }



    public RecordingFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        someEventListener = (onSomeEventListener) context;
    }

    FragmentRecordingBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recording, container, false);

        binding.imgStop.setEnabled(false);
        binding.imgPlay.setEnabled(true);

        binding.imgLisen.setEnabled(false);
        if (checkWriteExternalPermission()){
            fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";

            setupRecorder();

        }else {
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }



        return binding.getRoot();
    }

    private void setupRecorder() {
        binding.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
                binding.imgPlay.setEnabled(false);
                binding.imgStop.setEnabled(true);

                binding.imgLisen.setEnabled(false);

            }
        });
        binding.imgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
                binding.imgStop.setEnabled(false);
                binding.imgPlay.setEnabled(true);

                binding.imgLisen.setEnabled(true);

            }
        });
        binding.imgLisen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying();
                binding.imgStop.setEnabled(false);
                binding.imgPlay.setEnabled(true);

                binding.imgLisen.setEnabled(true);

            }
        });


    }


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {


        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
       // binding.tvPlaying.setText("اتكلم الأن ....");

    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
       // binding.tvPlaying.setText("");
    }


    // Record to the external cache directory for visibility


    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:

                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) {
            getActivity().finish();
        }


    }
    private boolean checkWriteExternalPermission()
    {
        String permission1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String permission2 = android.Manifest.permission.RECORD_AUDIO;
        int res = Objects.requireNonNull(getContext()).checkCallingOrSelfPermission(permission1);
        int res2 = getContext().checkCallingOrSelfPermission(permission2);
        return (res == PackageManager.PERMISSION_GRANTED&&res2 == PackageManager.PERMISSION_GRANTED);
    }

}