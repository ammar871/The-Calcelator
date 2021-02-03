package com.alltasksnew.thecalclatur.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.databinding.FragmentAddNodsBinding;
import com.alltasksnew.thecalclatur.provuders.AlarmBrodcast;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.ModelRoom;
import com.alltasksnew.thecalclatur.shardeditor.ShardEditor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


public class AddNodsFragment extends Fragment implements View.OnClickListener {

    String alarmDate;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private Chronometer timer;
    private boolean permissionToRecordAccepted = false;
    private int PERMISSION_CODE = 21;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    Animation open, hide, rotaion_left, rotaion_right, rotation_addr, rotation_addl;
    FragmentAddNodsBinding binding;
    ShardEditor shardEditor;
    private static final String LOG_TAG = "AudioRecordTest";
    ImageView imgplay, imgstop, imaglestion;
    String getDate;
    String timeText, getTimeText2;
    String outText;
    private AudioManager mAudioManager;
    MediaPlayer mediaPlayer;
    boolean relase = false;
    boolean isrecording = false;
    AppDatabase databasroom;
    Long date, time;
    private static final String TAG = "AddNodsFragment";
    private static int color = 0;
    boolean selectdate = false;
    private InterstitialAd mInterstitialAd;

    public AddNodsFragment() {

    }

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                releaseMediaPlayer();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener
            = mediaPlayer -> releaseMediaPlayer();


    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shardEditor = new ShardEditor(Objects.requireNonNull(getActivity()));
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_nods, container, false);

        getAdds();


        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


        // offset to add since we're not UTC


        checkPermation();

        databasroom = AppDatabase.getDatabaseInstance(getActivity());
        theCuurentTime();

        binding.cardVoice.setOnClickListener(view -> {
            if (checkPermation()) {
                fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                alrtDialogRecord();
            }


        });
        binding.cardTime.setOnClickListener(view -> {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view1, hourOfDay, minute1) -> {

                timeText = hourOfDay + ":" + minute1;
                getTimeText2 = hourOfDay + ":" + minute1;
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

                Calendar c1 = Calendar.getInstance();
                c1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c1.set(Calendar.MINUTE, minute1);
                time = c1.getTimeInMillis();
                String inputPattern = "HH:mm";
                @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);


                getTimeText2 = DateFormat.getTimeInstance(DateFormat.SHORT).format(c1.getTime());
                try {
                    Date date = inputFormat.parse(getTimeText2);
                    outText = inputFormat.format(date);
                    binding.tvTime.setText(outText + ":" + pmam);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }, hour, minute, false);

            timePickerDialog.show();
            selectdate = true;
        });

        getDatefromCalender();
        getColorBackRound();
        inItView();
        clickColors();
        addData();

        saveData();

        return binding.getRoot();
    }

    private void getAdds() {
        mInterstitialAd = new InterstitialAd(getActivity());
        MobileAds.initialize(getActivity(),"ca-app-pub-5582512134167391~5930186201");
        mInterstitialAd.setAdUnitId("ca-app-pub-5582512134167391/2923737156");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(getActivity(), initializationStatus -> {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
          //  mInterstitialAd.show();

        });
        AdRequest adRequest = new AdRequest.Builder()

                .build();

        binding.adView.loadAd(adRequest);
        binding.adView2.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {

                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private boolean checkPermation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkWriteExternalPermission()) {

                return true;

            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

            }


        } else {
            return false;

        }
        return false;
    }

    private void theCuurentTime() {
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time = dateFormat.format(new Date());
        binding.tvTime.setText(time);
    }

    private void saveData() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectdate) {
                    Toast.makeText(getActivity(), R.string.select_date, Toast.LENGTH_SHORT).show();
                } else {
                    if (mInterstitialAd.isLoaded())
                        mInterstitialAd.show();

                    Log.d("datad", getDate + timeText + fileName);
                    if (getDate != null && timeText != null) {
                        if (fileName != null) {

                            ModelRoom modelRoom = new ModelRoom(0, binding.edTyb.getText().toString(),
                                    time, date, timeText, getDate, fileName, "notFound", color);


                            databasroom.userDao().insertUser(modelRoom);
                            setAlarm(binding.edTyb.getText().toString(), getDate, timeText);

                            Log.d("calculat", getDate + timeText);
                            fileName = null;
                            Toast.makeText(getActivity(), R.string.save, Toast.LENGTH_SHORT).show();
                            playAudio(R.raw.savenod);
                            binding.edTyb.setText("");
                        } else {
                            ModelRoom modelRoom = new ModelRoom(0, binding.edTyb.getText().toString(),
                                    time, date, timeText, getDate, "notFound", "notFound", color);

                            databasroom.userDao().insertUser(modelRoom);
                            setAlarm(binding.edTyb.getText().toString(), getDate, timeText);

                            Toast.makeText(getActivity(), R.string.save, Toast.LENGTH_SHORT).show();
                            playAudio(R.raw.savenod);
                            binding.edTyb.setText("");
                        }
                        Log.d("calculat", getDate + timeText);

                        binding.tvTime.setText(outText);
                        binding.cardTime.setVisibility(View.GONE);
                        binding.cardDate.setVisibility(View.GONE);
                        binding.cardVoice.setVisibility(View.GONE);
                        binding.btnSave.setVisibility(View.GONE);
                        binding.adView.setVisibility(View.VISIBLE);
                        binding.btnSave.setAnimation(hide);
                        binding.cardTime.setAnimation(hide);
                        binding.cardDate.setAnimation(hide);
                        binding.cardVoice.setAnimation(hide);
                        Toast.makeText(getActivity(), R.string.save, Toast.LENGTH_SHORT).show();
                        playAudio(R.raw.savenod);
                    } else {
                        if (fileName != null) {

                            ModelRoom modelRoom = new ModelRoom(0, binding.edTyb.getText().toString(),
                                    time, date, timeText, getDate, fileName, "notFound", color);


                            databasroom.userDao().insertUser(modelRoom);


                            Log.d("calculat", getDate + timeText);
                            fileName = null;
                            Toast.makeText(getActivity(), R.string.save, Toast.LENGTH_SHORT).show();
                            playAudio(R.raw.savenod);
                            binding.edTyb.setText("");
                        } else {
                            ModelRoom modelRoom = new ModelRoom(0, binding.edTyb.getText().toString(),
                                    time, date, timeText, getDate, "notFound", "notFound", color);

                            databasroom.userDao().insertUser(modelRoom);


                            Toast.makeText(getActivity(), R.string.save, Toast.LENGTH_SHORT).show();
                            playAudio(R.raw.savenod);
                            binding.edTyb.setText("");
                        }
                        Log.d("calculat", getDate + timeText);

                        binding.tvTime.setText(R.string.select_time);
                        binding.cardTime.setVisibility(View.GONE);
                        binding.cardDate.setVisibility(View.GONE);
                        binding.cardVoice.setVisibility(View.GONE);
                        binding.btnSave.setVisibility(View.GONE);
                        binding.adView.setVisibility(View.VISIBLE);
                        binding.btnSave.setAnimation(hide);
                        binding.cardTime.setAnimation(hide);
                        binding.cardDate.setAnimation(hide);
                        binding.cardVoice.setAnimation(hide);
                        Toast.makeText(getActivity(), R.string.save, Toast.LENGTH_SHORT).show();
                        playAudio(R.raw.savenod);
                    }

                }

            }
        });

    }

    private void playAudio(int savenod) {


        releaseMediaPlayer();

        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


            mediaPlayer = MediaPlayer.create(getActivity(), savenod);


            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mCompletionListener);


        }
    }

    private void getDatefromCalender() {


        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint({"WrongConstant", "ResourceAsColor"})
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                binding.calendarView.setSelectedWeekBackgroundColor(R.color.violet);
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, i2);
                c.set(Calendar.MONTH, i1 + 1);
                c.set(Calendar.DAY_OF_MONTH, i);
                date = c.getTimeInMillis();

                Log.d(TAG, "onSelectedDayChange: " + date + "\n" + Calendar.getInstance().getTimeInMillis());
                getDate = i2 + "-" + (i1 + 1) + "-" + i;
                alarmDate = i2 + "-" + (i1 + 1) + "-" + i;
                selectdate = false;
            }
        });
    }

    private void addData() {
        binding.imgAdd.setOnClickListener(view -> {
            hidekeybeard();
            if (binding.cardTime.getVisibility() == View.VISIBLE && binding.cardDate.getVisibility() == View.VISIBLE
                    & binding.cardVoice.getVisibility() == View.VISIBLE) {

                binding.cardTime.setVisibility(View.GONE);
                binding.cardDate.setVisibility(View.GONE);
                binding.cardVoice.setVisibility(View.GONE);
                binding.btnSave.setVisibility(View.GONE);
                binding.adView.setVisibility(View.VISIBLE);


                getAnimation(binding.cardTime, hide);
                getAnimation(binding.cardDate, hide);
                getAnimation(binding.cardVoice, hide);
                getAnimation(binding.btnSave, hide);


            } else {

                binding.cardTime.setVisibility(View.VISIBLE);
                binding.cardDate.setVisibility(View.VISIBLE);
                binding.cardVoice.setVisibility(View.VISIBLE);
                binding.btnSave.setVisibility(View.VISIBLE);
                binding.adView.setVisibility(View.GONE);
                getAnimation(binding.btnSave, open);
                getAnimation(binding.cardVoice, open);
                getAnimation(binding.cardTime, open);
                getAnimation(binding.cardDate, open);


            }

        });
    }

    private void clickColors() {
        binding.imgColors.setOnClickListener(view -> {

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
                color = 2;
                hideButtons();
                break;
            case R.id.fab_violet:
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bluelighit));
                shardEditor.saveData(1);
                color = 1;
                hideButtons();
                break;
            case R.id.fabyallow:
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yallow));
                shardEditor.saveData(3);
                color = 3;
                hideButtons();
                break;
            case R.id.fab_app:
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.violet));
                shardEditor.saveData(0);
                color = 0;
                hideButtons();
                break;
            default:


        }

    }

    private void hideButtons() {
        binding.fabyallow.setVisibility(View.GONE);
        binding.fabViolet.setVisibility(View.GONE);
        binding.fabBrown.setVisibility(View.GONE);
        binding.fabApp.setVisibility(View.GONE);
        binding.adView.setVisibility(View.VISIBLE);

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
        timer = dialogView.findViewById(R.id.record_timer);


        setupRecorder();

//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });

        alert.setNegativeButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (isrecording){
                    stopRecording();
                }
                if (fileName != null) {
                    Log.d("filn", fileName);

                    Toast.makeText(getActivity(), R.string.save, Toast.LENGTH_SHORT).show();

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
                if (checkPermation()){

                    startRecording();
                    imgplay.setEnabled(false);
                    imgstop.setEnabled(true);
                    imaglestion.setEnabled(false);
                }


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
//
//    private void startRecording() {
//
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setOutputFile(fileName);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            recorder.prepare();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "prepare() failed");
//        }
//        recorder.start();
//        tv_recording.setText(R.string.recorced);
//
//    }

/*
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        tv_recording.setText("");
    }
*/

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

    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity().getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), id, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeText;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void releaseMediaPlayer() {

        if (mediaPlayer != null) {

            mediaPlayer.release();


            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            relase = true;
        }
    }
    private void stopRecording() {
        //Stop Timer, very obvious
        timer.stop();

        //Change text on page to file saved


        //Stop media recorder and set it to null for further use to record new audio
        recorder.stop();
        recorder.release();
        recorder = null;
        isrecording=false;
    }

    private void startRecording() {
        //Start timer from 0
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();





        //Setup Media Recorder for recording
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Start Recording
        recorder.start();
         isrecording = true;
    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }
    void hidekeybeard() {
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.layout.getWindowToken(), 0);
    }
}
