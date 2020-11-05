package com.alltaskes.thecalclatur.screens;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alltaskes.thecalclatur.R;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;

import java.io.IOException;
import java.util.Map;

public class ShowingObjectActivity extends AppCompatActivity {
    private MediaPlayer player = null;
    String filName;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_object);
        Editor renderer= findViewById(R.id.renderer);
        ImageView btn_record=findViewById(R.id.add_lisen);
//        renderer.setDividerLayout(R.layout.tmpl_divider_layout);
      renderer.setEditorImageLayout(R.layout.image_render);
      relativeLayout=findViewById(R.id.layout_editor);


        renderer.setListItemLayout(R.layout.tmpl_list_item);
        String serialized= getIntent().getStringExtra("content");
        String name= getIntent().getStringExtra("name");
         filName= getIntent().getStringExtra("filName");

        TextView tvname=findViewById(R.id.tv_name);
        tvname.setText(name);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filName.equalsIgnoreCase("notFound")){
                    Toast.makeText(ShowingObjectActivity.this, "لايوجد مقطع صوتى ", Toast.LENGTH_SHORT).show();

                }else {
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
        renderer.render(serialized);
    }

    private void startPlaying(String fileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("audio", "prepare() failed");
        }
    }
    @Override
    public void onStop() {
        super.onStop();


        if (player != null) {
            player.release();
            player = null;
        }
    }
}