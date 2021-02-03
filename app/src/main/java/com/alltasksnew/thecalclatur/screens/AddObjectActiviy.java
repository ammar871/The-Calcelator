package com.alltasksnew.thecalclatur.screens;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.work.OneTimeWorkRequest;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.ActivityAddObjectActiviyBinding;
import com.alltasksnew.thecalclatur.fragments.TheCalculateFragment;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.ModleObjct;
import com.alltasksnew.thecalclatur.servec.MyServec;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import top.defaults.colorpicker.ColorPickerPopup;
import yuku.ambilwarna.AmbilWarnaDialog;

public class AddObjectActiviy extends AppCompatActivity implements View.OnClickListener {
    ActivityAddObjectActiviyBinding binding;
    Animation open, hide, rotaion_left, rotaion_right, rotation_addr, rotation_addl;
    Uri uri;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static final int KITKAT_VALUE = 1002;

    public static String fileName = null;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    String contentObj;
    ImageView imgplay, imgstop, imaglestion;
    TextView tv_recording;
    private AppDatabase database;
    Button btnSave;
    ModleObjct modleObjct;
    private AudioManager mAudioManager;
    private static final String TAG = "AddObjectActiviy";
    private boolean isRecording = false;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions =
            {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    OneTimeWorkRequest request;
    //color
    int mDefaultColor;
    int color = 0xffffff00;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Commen.setlang(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_object_activiy);
        getAdds();

        mDefaultColor = ContextCompat.getColor(AddObjectActiviy.this, R.color.colorPrimary);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        database = AppDatabase.getDatabaseInstance(this);
        request = new OneTimeWorkRequest.Builder(MyServec.class).build();

        inItView();
        clickColors();
        setUpEditor();
        if (getIntent() != null) {
            modleObjct = getIntent().getParcelableExtra("content");
            if (modleObjct != null) {
                binding.edtObject.setText(modleObjct.getNameObj());
                binding.editor.render(modleObjct.getContentObj());
                fileName = modleObjct.getRecordObj();
            }


        }


//        binding.editor.setEditorImageLayout(R.layout.tmpl_image_view);
        binding.editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {


            }

            @Override
            public void onUpload(Bitmap image, String uuid) {

                binding.editor.onImageUploadComplete(uri.toString(), uuid);

            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }


        });


    }

    private void getAdds() {
        //adds
        mInterstitialAd = new InterstitialAd(this);
        MobileAds.initialize(this, "ca-app-pub-5582512134167391~5930186201");
        mInterstitialAd.setAdUnitId("ca-app-pub-5582512134167391/6540167085");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            //    mInterstitialAd.show();


            }
        });

        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {

                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void clickColors() {
        binding.imgOpen.setOnClickListener(view -> {

            if (binding.fabPhoto.getVisibility() == View.VISIBLE &&
                    binding.imgCalculate.getVisibility() == View.VISIBLE &&
                    binding.imgRecord.getVisibility() == View.VISIBLE

            ) {

                binding.imgCalculate.setVisibility(View.GONE);
                binding.imgRecord.setVisibility(View.GONE);
                binding.fabPhoto.setVisibility(View.GONE);

                getAnimation(binding.imgCalculate, hide);
                getAnimation(binding.imgRecord, hide);
                getAnimation(binding.fabPhoto, hide);


            } else {

                binding.fabPhoto.setVisibility(View.VISIBLE);
                binding.imgRecord.setVisibility(View.VISIBLE);
                binding.imgCalculate.setVisibility(View.VISIBLE);

                getAnimation(binding.fabPhoto, open);
                getAnimation(binding.imgRecord, open);
                getAnimation(binding.imgCalculate, open);

            }

        });

        binding.addItem.setOnClickListener(view -> {
            contentObj = binding.editor.getContentAsHTML();
            if (isRecording) {
                stopRecording();
                isRecored();
            }

            if (modleObjct != null) {

                if (contentObj != null && !contentObj.equalsIgnoreCase("") &&
                        binding.edtObject.getText().toString() != null
                        && !binding.edtObject.getText().toString().equalsIgnoreCase("")) {


                    if (fileName != null) {
                        ModleObjct modleObjct2 = new ModleObjct(modleObjct.getId(), binding.edtObject.getText().toString()
                                , contentObj, fileName, java.text.DateFormat.getDateTimeInstance().format(new Date()));
                        database.obejectDao().updateUser(modleObjct2);
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();

                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");

                        }

                        Toast.makeText(AddObjectActiviy.this, R.string.update, Toast.LENGTH_SHORT).show();

                        onBackPressed();
                        finish();

                    } else {
                        ModleObjct modleObjct2 = new ModleObjct(modleObjct.getId(), binding.edtObject.getText().toString()
                                , contentObj, "notFound", java.text.DateFormat.getDateTimeInstance().format(new Date()));
                        database.obejectDao().updateUser(modleObjct2);
                        Toast.makeText(AddObjectActiviy.this, R.string.update, Toast.LENGTH_SHORT).show();
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();

                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");

                        }
                        onBackPressed();
                        finish();
                    }
                } else {
                    Toast.makeText(AddObjectActiviy.this, R.string.file_details, Toast.LENGTH_SHORT).show();
                }
            } else {
                if (contentObj != null && !contentObj.equalsIgnoreCase("") && !binding.edtObject.getText().toString().equalsIgnoreCase("")) {
                    if (fileName != null) {
                        ModleObjct modleObjct = new ModleObjct(0, binding.edtObject.getText().toString()
                                , contentObj, fileName, java.text.DateFormat.getDateTimeInstance().format(new Date()));
                        database.obejectDao().insertUser(modleObjct);
                        playAudio(R.raw.saveobejct);
                        Toast.makeText(AddObjectActiviy.this, R.string.save, Toast.LENGTH_SHORT).show();
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();

                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");

                        }
                        onBackPressed();
                        //  releaseMediaPlayer();
                        finish();

                    } else {
                        ModleObjct modleObjct = new ModleObjct(0, binding.edtObject.getText().toString()
                                , contentObj, "notFound", java.text.DateFormat.getDateTimeInstance().format(new Date()));
                        database.obejectDao().insertUser(modleObjct);
                        playAudio(R.raw.saveobejct);
                        Toast.makeText(AddObjectActiviy.this, R.string.save, Toast.LENGTH_SHORT).show();
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();

                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");

                        }
                        onBackPressed();
                        //  releaseMediaPlayer();
                        finish();
                    }
                } else {
                    Toast.makeText(AddObjectActiviy.this, R.string.file_details, Toast.LENGTH_SHORT).show();
                }
            }

        });
        binding.imgClose.setOnClickListener(view -> {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

                finish();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
                finish();
            }

            if (recorder != null)
                recorder.release();
            recorder = null;

            onBackPressed();
        });

    }

    private void inItView() {
        binding.imgRecord.setOnClickListener(this);
        binding.fabPhoto.setOnClickListener(this);
        binding.imgCalculate.setOnClickListener(this);


        //animation
        open = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        hide = AnimationUtils.loadAnimation(this, R.anim.hide);
        rotaion_left = AnimationUtils.loadAnimation(this, R.anim.rotation_to_left);
        rotaion_right = AnimationUtils.loadAnimation(this, R.anim.rotation_right);
        rotation_addr = AnimationUtils.loadAnimation(this, R.anim.rotation_addright);
        rotation_addl = AnimationUtils.loadAnimation(this, R.anim.rottion_addleft);
    }

    private void hidButtns() {

        binding.fabPhoto.setVisibility(View.GONE);
        binding.imgCalculate.setVisibility(View.GONE);
        binding.imgRecord.setVisibility(View.GONE);

        getAnimation(binding.fabPhoto, hide);
        getAnimation(binding.imgCalculate, hide);
        getAnimation(binding.imgRecord, hide);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_calculate:

                TheCalculateFragment cart = new TheCalculateFragment();

                cart.show(getSupportFragmentManager(), cart.getClass().getSimpleName());

                hidButtns();

                break;
            case R.id.img_record:
                if (isRecording) {
                    Toast.makeText(this, R.string.isRecording, Toast.LENGTH_SHORT).show();

                } else {

                    if (checkPermation()) {

                        fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                        startRecording();
                        isRecored();

                    }
                }


                hidButtns();

                break;
            case R.id.fab_photo:

                Intent intent;

                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, KITKAT_VALUE);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    startActivityForResult(intent, KITKAT_VALUE);
                }
                hidButtns();
                break;


        }
    }

    private void isRecored() {
        if (recorder != null) {
            binding.lener.setVisibility(View.VISIBLE);
        } else {
            binding.lener.setVisibility(View.GONE);
        }
    }

    private void getAnimation(View view, Animation animation) {
        view.clearAnimation();
        view.setAnimation(animation);
        view.getAnimation().start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KITKAT_VALUE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {

                binding.editor.insertImage(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        }

    }

    private void setUpEditor() {
        binding.actionBlockquote.setOnClickListener(v -> binding.editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE));

        binding.actionH1.setOnClickListener(v -> binding.editor.updateTextStyle(EditorTextStyle.H1));

        binding.actionH2.setOnClickListener(v -> binding.editor.updateTextStyle(EditorTextStyle.H2));

        binding.actionH3.setOnClickListener(v -> binding.editor.updateTextStyle(EditorTextStyle.H3));

        binding.actionBold.setOnClickListener(v -> binding.editor.updateTextStyle(EditorTextStyle.BOLD));

        binding.actionItalic.setOnClickListener(v -> binding.editor.updateTextStyle(EditorTextStyle.ITALIC));

        binding.actionBulleted.setOnClickListener(v -> binding.editor.insertList(false));

        binding.actionUnorderedNumbered.setOnClickListener(v -> binding.editor.insertList(true));

        binding.actionHr.setOnClickListener(v -> binding.editor.insertDivider());

        binding.actionColor.setOnClickListener(view -> {


            //        openDialog();
            new ColorPickerPopup.Builder(AddObjectActiviy.this)
                    .initialColor(R.color.violet) // Set initial color
                    .enableAlpha(true) // Enable alpha slider or not
                    .okTitle(getString(R.string.Selecte))
                    .cancelTitle(getString(R.string.back))
                    .showIndicator(true)
                    .showValue(true)
                    .build()
                    .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                        @Override
                        public void onColorPicked(int color) {
                            //Toast.makeText(AddObjectActiviy.this, "picked" + colorHex(color), Toast.LENGTH_LONG).show();
                            binding.editor.updateTextColor(colorHex(color));
                        }

                        @Override
                        public void onColor(int color, boolean fromUser) {

                        }
                    });
        });

        binding.actionInsertLink.setOnClickListener(v -> binding.editor.insertLink());

        findViewById(R.id.action_erase).setOnClickListener(v -> binding.editor.clearAllContents());

    }

    void openDialog() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(AddObjectActiviy.this, color, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                binding.editor.updateTextColor(colorHex(color));
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
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
        //  tv_recording = dialogView.findViewById(R.id.);


        setupRecorder();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileName != null)
                    Log.d("filn", fileName);
                Toast.makeText(AddObjectActiviy.this, R.string.save, Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
            finish();
        }


    }

    private void setupRecorder() {
        imgplay.setEnabled(true);
        imgstop.setEnabled(false);
        imaglestion.setEnabled(false);

        imgplay.setOnClickListener(view -> {
            startRecording();
            imgplay.setEnabled(false);
            imgstop.setEnabled(true);
            imaglestion.setEnabled(false);

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
            Log.e("LOG_TAG", "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.lener.setOnClickListener(view -> {
            stopRecording();
            if (fileName != null)
                Log.d("filn", fileName);
            Toast.makeText(AddObjectActiviy.this, R.string.save, Toast.LENGTH_SHORT).show();
            binding.lener.setVisibility(View.GONE);
        });
    }

 /*   private void startRecording() {


        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        recorder.start();
        //tv_recording.setText("اتكلم الأن ...");

    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        //  tv_recording.setText("");
    }*/

    // Record to the external cache directory for visibility

    @Override
    public void onStop() {
        super.onStop();

//            recorder.release();
//            recorder = null;


    }

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = focusChange -> {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            player.pause();
            player.seekTo(0);
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

            player.start();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

            releaseMediaPlayer();
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener
            = mediaPlayer -> releaseMediaPlayer();

    private void playAudio(int savenod) {


        releaseMediaPlayer();
        player = MediaPlayer.create(getApplicationContext(), savenod);
//        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
//                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//
//
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


        player.start();
        player.setOnCompletionListener(mCompletionListener);


        //  }
    }

    private void releaseMediaPlayer() {

        if (player != null) {

            player.release();


            player = null;
            //   mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private void stopRecording() {
        //Stop Timer, very obvious
        binding.recordTimer.stop();

        //Change text on page to file saved


        //Stop media recorder and set it to null for further use to record new audio
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording = false;
    }

    private void startRecording() {
        //Start timer from 0

        binding.recordTimer.setBase(SystemClock.elapsedRealtime());
        binding.recordTimer.start();


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
        isRecording = true;
    }

}