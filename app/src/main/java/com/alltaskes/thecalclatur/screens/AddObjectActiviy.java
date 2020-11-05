package com.alltaskes.thecalclatur.screens;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.alltaskes.thecalclatur.R;
import com.alltaskes.thecalclatur.databinding.ActivityAddObjectActiviyBinding;
import com.alltaskes.thecalclatur.fragments.TheCalculateFragment;
import com.alltaskes.thecalclatur.roomdatabase.AppDatabase;
import com.alltaskes.thecalclatur.roomdatabase.ModleObjct;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddObjectActiviy extends AppCompatActivity implements View.OnClickListener {
    ActivityAddObjectActiviyBinding binding;
    Animation open, hide, rotaion_left, rotaion_right, rotation_addr, rotation_addl;
    Uri uri;
    String text;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static final int KITKAT_VALUE = 1002;

    public static String fileName = null;
    Calendar c;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    String contentObj;
    ImageView imgplay, imgstop, imaglestion;
    TextView tv_recording;
    private AppDatabase database;
    Button btnSave, btnFinsh;
    ModleObjct modleObjct;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //color
    int mDefaultColor;
    int color = 0xffffff00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_object_activiy);
        mDefaultColor = ContextCompat.getColor(AddObjectActiviy.this, R.color.colorPrimary);

        database = AppDatabase.getDatabaseInstance(this);
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

             //   Toast.makeText(AddObjectActiviy.this, uuid, Toast.LENGTH_LONG).show();

                binding.editor.onImageUploadComplete(uri.toString(), uuid);

            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }


        });

        binding.imgIsrecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
                if (fileName != null)
                    Log.d("filn", fileName);
                Toast.makeText(AddObjectActiviy.this, "تم الحفظ ", Toast.LENGTH_SHORT).show();
                binding.imgIsrecorder.setVisibility(View.GONE);
            }
        });
    }

    private void clickColors() {
        binding.imgOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            }
        });

        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentObj = binding.editor.getContentAsHTML();
                if (modleObjct != null) {
                    if (contentObj != null && !contentObj.equalsIgnoreCase("") &&
                            binding.edtObject.getText().toString() != null
                            && !binding.edtObject.getText().toString().equalsIgnoreCase("")) {
                        if (fileName != null) {
                            ModleObjct modleObjct2 = new ModleObjct(modleObjct.getId(), binding.edtObject.getText().toString()
                                    , contentObj, fileName,java.text.DateFormat.getDateTimeInstance().format(new Date()));
                            database.obejectDao().updateUser(modleObjct2);
                            Toast.makeText(AddObjectActiviy.this, "Updating", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();

                        } else {
                            ModleObjct modleObjct2 = new ModleObjct(modleObjct.getId(), binding.edtObject.getText().toString()
                                    , contentObj, "notFound",java.text.DateFormat.getDateTimeInstance().format(new Date()));
                            database.obejectDao().updateUser(modleObjct2);
                            Toast.makeText(AddObjectActiviy.this, "تم التعديل", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();
                        }
                    } else {
                        Toast.makeText(AddObjectActiviy.this, "please Fill Your Deatials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (contentObj != null && !contentObj.equalsIgnoreCase("") &&
                            binding.edtObject.getText().toString() != null
                            && !binding.edtObject.getText().toString().equalsIgnoreCase("")) {
                        if (fileName != null) {
                            ModleObjct modleObjct = new ModleObjct(0, binding.edtObject.getText().toString()
                                    , contentObj, fileName,java.text.DateFormat.getDateTimeInstance().format(new Date()));
                            database.obejectDao().insertUser(modleObjct);
                            Toast.makeText(AddObjectActiviy.this, "Saving", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();

                        } else {
                            ModleObjct modleObjct = new ModleObjct(0, binding.edtObject.getText().toString()
                                    , contentObj, "notFound",java.text.DateFormat.getDateTimeInstance().format(new Date()));
                            database.obejectDao().insertUser(modleObjct);
                            Toast.makeText(AddObjectActiviy.this, "تم الحفظ", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();
                        }
                    } else {
                        Toast.makeText(AddObjectActiviy.this, "please Fill Your Deatials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                onBackPressed();
            }
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
                if (checkPermation()) {
                    fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    startRecording();
                    isRecored();

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
            binding.imgIsrecorder.setVisibility(View.VISIBLE);
        } else {
            binding.imgIsrecorder.setVisibility(View.GONE);
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
        binding.actionH1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        binding.actionH2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        binding.actionH3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        binding.actionBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        binding.actionItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });


        binding.actionBulleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.insertList(false);
            }
        });

        binding.actionUnorderedNumbered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.insertList(true);
            }
        });

        binding.actionHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.insertDivider();
            }
        });


        binding.actionColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openDialog();

           /*     new ColorPickerPopup.Builder(AddObjectActiviy.this)
                        .initialColor(R.color.violet) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("اختيار")
                        .cancelTitle("رجوع")
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
*/
            }
        });

        binding.actionInsertLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.insertLink();
            }
        });


        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editor.clearAllContents();
            }
        });


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
        tv_recording = dialogView.findViewById(R.id.tv_playing);


        setupRecorder();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileName != null)
                    Log.d("filn", fileName);
                Toast.makeText(AddObjectActiviy.this, "تم الحفظ ", Toast.LENGTH_SHORT).show();
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
            finish();
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
            Log.e("LOG_TAG", "prepare() failed");
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