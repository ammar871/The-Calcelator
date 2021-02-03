package com.alltasksnew.thecalclatur.screens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.ActivityShowingObjectBinding;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;

import java.io.IOException;
import java.util.Map;

public class ShowingObjectActivity extends AppCompatActivity {
    private MediaPlayer player = null;
    String filName;
    RelativeLayout relativeLayout;
    private static final String TAG = "ShowingObjectActivity";
    ActivityShowingObjectBinding binding;
    String inputText;
    private AudioManager mAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Commen.setlang(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_showing_object);
        Editor renderer = findViewById(R.id.renderer);

        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

//        renderer.setDividerLayout(R.layout.tmpl_divider_layout);
        renderer.setEditorImageLayout(R.layout.image_render);
        relativeLayout = findViewById(R.id.layout_editor);


        renderer.setListItemLayout(R.layout.tmpl_list_item);
        String serialized = getIntent().getStringExtra("content");
        String name = getIntent().getStringExtra("name");
        filName = getIntent().getStringExtra("filName");
        TextView tvname = findViewById(R.id.tv_name);
        tvname.setText(name);
        binding.addLisen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (filName.equalsIgnoreCase("notFound")) {
                    Toast.makeText(ShowingObjectActivity.this, getString(R.string.not_audio), Toast.LENGTH_SHORT).show();

                } else {
                    startPlaying(filName);
                }
            }
        });
        renderer.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {

            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                renderer.onImageUploadComplete(image.toString(), uuid);

            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> settings, int index) {

                return null;
            }
        });
        binding.renderer.render(serialized);

        binding.imgShare.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    inputText = Html.fromHtml(serialized, Html.FROM_HTML_MODE_COMPACT).toString();
                    shareText(inputText);

                } else {

                    inputText = Html.fromHtml(serialized, Html.FROM_HTML_MODE_COMPACT).toString();
                    shareText(inputText);

                }
            }
        });

    }
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                player.pause();
                player.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                player.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                releaseMediaPlayer();
            }
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener
            = mediaPlayer -> releaseMediaPlayer();
    private void shareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(Intent.createChooser(shareIntent, "Share..."));
    }

    private void startPlaying(String fileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            player.setOnCompletionListener(mCompletionListener);
        } catch (IOException e) {
            Log.e("audio", "prepare() failed");
        }
    }

    @Override
    public void onStop() {
        super.onStop();


        if (player != null) {
            player.release();

        }
    }
    private void releaseMediaPlayer() {

        if (player != null) {

            player.release();


            player = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }
}