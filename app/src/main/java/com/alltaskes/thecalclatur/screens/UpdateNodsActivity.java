package com.alltaskes.thecalclatur.screens;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.databinding.ActivityUpdateNodsBinding;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.ModelRoom;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class UpdateNodsActivity extends AppCompatActivity {
ActivityUpdateNodsBinding binding;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String LOG_TAG = "AudioRecordTest";
    public static String fileName = null;
    Calendar c;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    ImageView imgplay, imgstop, imaglestion;
    TextView tv_recording;
    Button btnSave, btnFinsh;

    String getDate;
    String timeText;
    String outText;
    AppDatabase databasroom;
    Long date,time;
    static int color=0;
    ModelRoom modelRoomIntent;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_nods);
        if (getIntent()!=null){
            modelRoomIntent=getIntent().getParcelableExtra("modelNods");
            if(modelRoomIntent!=null){

                binding.tvTime.setText(modelRoomIntent.getTimes());
                binding.edTyb.setText(modelRoomIntent.getInput());
                fileName=modelRoomIntent.getAudio();
                getDate=modelRoomIntent.getDates();
                date=modelRoomIntent.getDate();
                time=modelRoomIntent.getTime();
                outText=modelRoomIntent.getTimes();
                color=modelRoomIntent.getColor();
            }


        }

        databasroom = AppDatabase.getDatabaseInstance(this);
        checkPermation();


        binding.cardVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermation()){
                    fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    alrtDialogRecord();
                }


            }
        });
        binding.cardTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateNodsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String pmam = null;
                        if (hourOfDay > 12) {
                            pmam = "pm";
                        } else if (hourOfDay == 12) {
                            pmam = "pm";
                        } else if (hourOfDay < 12) {
                            if (hourOfDay != 0) {
                                pmam = "am";
                            } else {
                                pmam = "am";
                            }
                        }

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        time= c.getTimeInMillis();
                        String inputPattern = "HH:mm";
                        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);


                        timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                        try {
                            Date date = inputFormat.parse(timeText);
                            outText = inputFormat.format(date);
                            binding.tvTime.setText(outText + ":" + pmam);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, hour, minute, false);

                timePickerDialog.show();

            }
        });
        saveData();
    }
    private void saveData() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("datad", getDate + timeText + fileName);
                if (date != null && time != null) {
                    if (fileName != null) {
                        ModelRoom modelRoom = new ModelRoom(modelRoomIntent.getId(),binding.edTyb.getText().toString(),
                                time,date,outText, getDate, fileName, "notFound",color);
                        databasroom.userDao().updateUser(modelRoom);
                        fileName = null;
                    } else {
                        ModelRoom modelRoom = new ModelRoom(modelRoomIntent.getId(),binding.edTyb.getText().toString(),
                                time,date,timeText, getDate, "notFound", "notFound",color);
                        databasroom.userDao().updateUser(modelRoom);
                    }

                    Toast.makeText(UpdateNodsActivity.this, "تم الحفظ بنجاح ", Toast.LENGTH_SHORT).show();
                  startActivity(new Intent(UpdateNodsActivity.this,MainActivity.class));
                  finish();



                } else {
                    Toast.makeText(UpdateNodsActivity.this, " من فضلك املئ البيانات ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean checkPermation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkWriteExternalPermission()) {

                return true;

            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

            }


        } else {
            return false;

        }
        return false;
    }
    void alrtDialogRecord() {


        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_recording, null);


        imaglestion = dialogView.findViewById(R.id.img_lisen);
        imgstop = dialogView.findViewById(R.id.img_stop);
        imgplay = dialogView.findViewById(R.id.img_play);
//        btnSave = dialogView.findViewById(R.id.btn_Save);
        tv_recording = dialogView.findViewById(R.id.tv_playing);


        setupRecorder();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileName != null)
                    Log.d("filn", fileName);
                Toast.makeText(UpdateNodsActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("انهاء", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.setView(dialogView);
        alert.create().show();
    }


    private boolean checkWriteExternalPermission() {
        String permission1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String permission2 = android.Manifest.permission.RECORD_AUDIO;
        int res = Objects.requireNonNull(this).checkCallingOrSelfPermission(permission1);
        int res2 = this.checkCallingOrSelfPermission(permission2);
        return (res == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED);
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
            this.finish();
        }


    }

    private void setupRecorder() {
        imgplay.setEnabled(true);
        imgstop.setEnabled(false);
        imaglestion.setEnabled(false);

        imgplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
                imgplay.setEnabled(false);
                imgstop.setEnabled(true);
                imaglestion.setEnabled(false);

            }
        });
        imgstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
                imgplay.setEnabled(false);
                imgstop.setEnabled(true);

                imaglestion.setEnabled(true);


            }
        });
        imaglestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying();

                imgplay.setEnabled(true);
                imgstop.setEnabled(false);

                imaglestion.setEnabled(true);

            }
        });


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
        tv_recording.setText("اتكلم الأن ...");

    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        tv_recording.setText("");
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
}