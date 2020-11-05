package com.alltaskes.thecalclatur.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.databinding.FragmentAddNodsBinding;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.ModelRoom;
import com.alltaskes.thecalclatur.shardeditor.MyBroadCast;
import com.alltaskes.thecalclatur.shardeditor.RecordingFragment;
import com.alltaskes.thecalclatur.shardeditor.ShardEditor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


public class AddNodsFragment extends Fragment implements View.OnClickListener {
   String alarmDate,alarmTime;


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static String fileName = null;
Calendar c;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    RecordingFragment.onSomeEventListener someEventListener;

    Animation open, hide, rotaion_left, rotaion_right, rotation_addr, rotation_addl;
    FragmentAddNodsBinding binding;

    ShardEditor shardEditor;
    AlertDialog.Builder dialog;
    private static final String LOG_TAG = "AudioRecordTest";

    ImageView imgplay, imgstop, imaglestion;
    TextView tv_recording;
    Button btnSave, btnFinsh;

    String getDate;
    String timeText,getTimeText2;
    String outText;

    AppDatabase databasroom;
    Long date,time;
    private static final String TAG = "AddNodsFragment";
private static int color=0;
    long sinceMidnight;
    public AddNodsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shardEditor = new ShardEditor(getActivity());
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_nods, container, false);
        Calendar rightNow = Calendar.getInstance();

// offset to add since we're not UTC


        checkPermation();

        databasroom = AppDatabase.getDatabaseInstance(getActivity());
        theCuurentTime();

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

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        timeText = hourOfDay + ":" + minute;
                        getTimeText2 = hourOfDay + ":" + minute;
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


                        getTimeText2 = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                        try {
                            Date date = inputFormat.parse(getTimeText2);
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

        getDatefromCalender();
        getColorBackRound();
        inItView();
        clickColors();
        addData();

        saveData();

        return binding.getRoot();
    }

    private boolean checkPermation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkWriteExternalPermission()) {

                return true;

            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

            }


        } else {
            return false;

        }
        return false;
    }

    private void theCuurentTime() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time = dateFormat.format(new Date());
        binding.tvTime.setText(time);
    }

    private void saveData() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("datad", getDate + timeText + fileName);
                if (getDate != null && timeText != null) {
                    if (fileName != null) {

                        ModelRoom modelRoom = new ModelRoom(0,binding.edTyb.getText().toString(),
                                time,date,timeText, getDate, fileName, "notFound",color);


                        databasroom.userDao().insertUser(modelRoom);
                        fileName = null;
                        Toast.makeText(getActivity(), "تم الحفظ بنجاح ", Toast.LENGTH_SHORT).show();
                    } else {
                        ModelRoom modelRoom = new ModelRoom(0,binding.edTyb.getText().toString(),
                                time,date,timeText, getDate, "notFound", "notFound",color);

                        databasroom.userDao().insertUser(modelRoom);
                        Toast.makeText(getActivity(), "تم الحفظ بنجاح ", Toast.LENGTH_SHORT).show();
                    }


                    binding.tvTime.setText("اختار الوقت ");
                    binding.cardTime.setVisibility(View.GONE);
                    binding.cardDate.setVisibility(View.GONE);
                    binding.cardVoice.setVisibility(View.GONE);
                    binding.btnSave.setVisibility(View.GONE);
                    binding.btnSave.setAnimation(hide);
                    binding.cardTime.setAnimation(hide);
                    binding.cardDate.setAnimation(hide);
                    binding.cardVoice.setAnimation(hide);
                    Toast.makeText(getActivity(), "تم الحفظ بنجاح ", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), " من فضلك املئ البيانات ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getDatefromCalender() {


        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, i2);
                c.set(Calendar.MONTH, i1+1);
                c.set(Calendar.DAY_OF_MONTH, i);
                date= c.getTimeInMillis();

                Log.d(TAG, "onSelectedDayChange: "+date +"\n"+Calendar.getInstance().getTimeInMillis());
                getDate = i + "-" + (i1 + 1) + "-" + i2;
               alarmDate= i2 + "-" + (i1 + 1) + "-" + i;
            }
        });
    }


    private void addData() {
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.cardTime.getVisibility() == View.VISIBLE && binding.cardDate.getVisibility() == View.VISIBLE
                        & binding.cardVoice.getVisibility() == View.VISIBLE) {

                    binding.cardTime.setVisibility(View.GONE);
                    binding.cardDate.setVisibility(View.GONE);
                    binding.cardVoice.setVisibility(View.GONE);
                    binding.btnSave.setVisibility(View.GONE);


                    getAnimation(binding.cardTime, hide);
                    getAnimation(binding.cardDate, hide);
                    getAnimation(binding.cardVoice, hide);
                    getAnimation(binding.btnSave, hide);


                } else {

                    binding.cardTime.setVisibility(View.VISIBLE);
                    binding.cardDate.setVisibility(View.VISIBLE);
                    binding.cardVoice.setVisibility(View.VISIBLE);
                    binding.btnSave.setVisibility(View.VISIBLE);

                    getAnimation(binding.btnSave, open);
                    getAnimation(binding.cardVoice, open);
                    getAnimation(binding.cardTime, open);
                    getAnimation(binding.cardDate, open);


                }

            }
        });
    }

    private void clickColors() {
        binding.imgColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.fabApp.getVisibility() == View.VISIBLE &&
                        binding.fabyallow.getVisibility() == View.VISIBLE &&
                        binding.fabBrown.getVisibility() == View.VISIBLE &&
                        binding.fabViolet.getVisibility() == View.VISIBLE
                ) {

                    binding.fabyallow.setVisibility(View.GONE);
                    binding.fabViolet.setVisibility(View.GONE);
                    binding.fabBrown.setVisibility(View.GONE);
                    binding.fabApp.setVisibility(View.GONE);
                    getAnimation(binding.fabyallow, hide);
                    getAnimation(binding.fabBrown, hide);
                    getAnimation(binding.fabViolet, hide);
                    getAnimation(binding.fabApp, hide);
                    getAnimation(binding.imgColors, rotaion_right);

                } else {

                    binding.fabApp.setVisibility(View.VISIBLE);
                    binding.fabyallow.setVisibility(View.VISIBLE);
                    binding.fabViolet.setVisibility(View.VISIBLE);
                    binding.fabBrown.setVisibility(View.VISIBLE);
                    getAnimation(binding.fabyallow, open);
                    getAnimation(binding.fabBrown, open);
                    getAnimation(binding.fabViolet, open);
                    getAnimation(binding.fabApp, open);
                    getAnimation(binding.imgColors, rotaion_left);
                }

            }
        });

    }

    private void inItView() {
        binding.fabBrown.setOnClickListener(this);
        binding.fabViolet.setOnClickListener(this);
        binding.fabyallow.setOnClickListener(this);
        binding.fabApp.setOnClickListener(this);

        //animation

        open = AnimationUtils.loadAnimation(getActivity(), R.anim.open);
        hide = AnimationUtils.loadAnimation(getActivity(), R.anim.hide);
        rotaion_left = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation_to_left);
        rotaion_right = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation_right);
        rotation_addr = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation_addright);
        rotation_addl = AnimationUtils.loadAnimation(getActivity(), R.anim.rottion_addleft);
    }

    private void getColorBackRound() {
        if (shardEditor.loadData().get(ShardEditor.KEY_USER_Color) != null &&
                shardEditor.loadData().get(ShardEditor.KEY_USER_Color) == 1) {

            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bluelighit));
        } else if (shardEditor.loadData().get(ShardEditor.KEY_USER_Color) != null &&
                shardEditor.loadData().get(ShardEditor.KEY_USER_Color) == 2) {

            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.brawn));
        } else if (shardEditor.loadData().get(ShardEditor.KEY_USER_Color) != null &&
                shardEditor.loadData().get(ShardEditor.KEY_USER_Color) == 3) {

            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yallow));
        } else if (shardEditor.loadData().get(ShardEditor.KEY_USER_Color) != null &&
                shardEditor.loadData().get(ShardEditor.KEY_USER_Color) == 4) {

            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.violet));
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_brown:
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.brawn));
                shardEditor.saveData(2);
               color=2;
                hidButtns();
                break;
            case R.id.fab_violet:
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bluelighit));
                shardEditor.saveData(1);
                color=1;
                hidButtns();
                break;
            case R.id.fabyallow:
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yallow));
                shardEditor.saveData(3);
                color=3;
                hidButtns();
                break;
            case R.id.fab_app:
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.violet));
                shardEditor.saveData(4);
                color=3;
                hidButtns();
                break;

        }

    }

    private void hidButtns() {
        binding.fabyallow.setVisibility(View.GONE);
        binding.fabViolet.setVisibility(View.GONE);
        binding.fabBrown.setVisibility(View.GONE);
        binding.fabApp.setVisibility(View.GONE);

        getAnimation(binding.fabyallow, hide);
        getAnimation(binding.fabBrown, hide);
        getAnimation(binding.fabViolet, hide);
        getAnimation(binding.fabApp, hide);
        getAnimation(binding.imgColors, rotaion_right);


    }


    private void getAnimation(View view, Animation animation) {
        view.clearAnimation();
        view.setAnimation(animation);
        view.getAnimation().start();
    }

    void alrtDialogRecord() {


        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_recording, null);


        imaglestion = dialogView.findViewById(R.id.img_lisen);
        imgstop = dialogView.findViewById(R.id.img_stop);
        imgplay = dialogView.findViewById(R.id.img_play);
//        btnSave = dialogView.findViewById(R.id.btn_Save);
        tv_recording = dialogView.findViewById(R.id.tv_playing);


        setupRecorder();

//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });

        alert.setNegativeButton("حفظ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (fileName != null){
                    Log.d("filn", fileName);
                    Toast.makeText(getActivity(), "تم الحفظ ", Toast.LENGTH_SHORT).show();

                }

                dialog.dismiss();
            }
        });
        alert.setView(dialogView);
    alert.create().show();
    }


    private boolean checkWriteExternalPermission() {
        String permission1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String permission2 = android.Manifest.permission.RECORD_AUDIO;
        int res = Objects.requireNonNull(getContext()).checkCallingOrSelfPermission(permission1);
        int res2 = getContext().checkCallingOrSelfPermission(permission2);
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
            getActivity().finish();
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


    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }


    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager)getActivity(). getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), MyBroadCast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + time;
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);

            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
