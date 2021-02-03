package com.alltasksnew.thecalclatur.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.adpters.AdpterMunths;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.FragmentOutlayManthBinding;
import com.alltasksnew.thecalclatur.roomdatabase.AppDatabase;
import com.alltasksnew.thecalclatur.roomdatabase.DaysModle;
import com.alltasksnew.thecalclatur.roomdatabase.Modle_itemMunth;
import com.alltasksnew.thecalclatur.shardeditor.ShardEditor;

import java.util.ArrayList;


public class OutlayManthFragment extends Fragment {
    private AppDatabase database;

    public OutlayManthFragment() {
        // Required empty public constructor
    }

    ArrayList<Modle_itemMunth> list;
    ArrayList<DaysModle> listdays;
    FragmentOutlayManthBinding binding;
    AdpterMunths adpter;
    private String solary="0.0" ;
    private double allOutaly = 0;
    ShardEditor shardEditor;
    private MediaPlayer player = null;
    private AudioManager mAudioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Commen.setlang(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outlay_manth, container, false);
        player = new MediaPlayer();
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        shardEditor = new ShardEditor(getActivity());
        database = AppDatabase.getDatabaseInstance(getActivity());

        datMaunth();
        deleteAllOyaly();
        if (!solary.isEmpty()){
            minusSalary();
        }

        return binding.getRoot();
    }

    private void deleteAllOyaly() {
        binding.cardDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.delete)
                        .setMessage(R.string.are_you_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                database.daysDao().nukeTable();
                                listdays.clear();
                                listdays = (ArrayList<DaysModle>) database.daysDao().getAll();
                                playAudio(R.raw.deleteoutaly);
                                getTotalOfYear();
                                minusSalary();

                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
        binding.cardAddSllary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alrtDialogAdding();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // listdays.clear();
        listdays = (ArrayList<DaysModle>) database.daysDao().getAll();
        getTotalOfYear();
    }

    @Override
    public void onResume() {
        super.onResume();
        listdays.clear();
        listdays = (ArrayList<DaysModle>) database.daysDao().getAll();
        getTotalOfYear();
        minusSalary();

    }

    private void getTotalOfYear() {
        double totalOutaly = 0.0;

        for (DaysModle days : listdays) {
            totalOutaly += days.getOutalymony();
        }
        allOutaly = totalOutaly;
        binding.tvResults.setText(totalOutaly + "");


    }

    private void datMaunth() {
        binding.rvMunths.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.rvMunths.setHasFixedSize(true);
        list = new ArrayList<>();
        database.mounthDao().insertUser(new Modle_itemMunth(1, "one", "1", getString(R.string.jan)));
        database.mounthDao().insertUser(new Modle_itemMunth(2, "two", "2", getString(R.string.feb)));
        database.mounthDao().insertUser(new Modle_itemMunth(3, "vfdcc", "3", getString(R.string.mar)));
        database.mounthDao().insertUser(new Modle_itemMunth(4, "yyyy", "4", getString(R.string.apr)));
        database.mounthDao().insertUser(new Modle_itemMunth(5, "rrrr", "5", getString(R.string.may)));
        database.mounthDao().insertUser(new Modle_itemMunth(6, "jjj", "6", getString(R.string.june)));
        database.mounthDao().insertUser(new Modle_itemMunth(7, "uuuuu", "7", getString(R.string.july)));
        database.mounthDao().insertUser(new Modle_itemMunth(8, "dsdwed", "8", getString(R.string.aug)));
        database.mounthDao().insertUser(new Modle_itemMunth(9, "ytjyh", "9", getString(R.string.sept)));
        database.mounthDao().insertUser(new Modle_itemMunth(10, "hrehsrhsgr", "10", getString(R.string.oct)));
        database.mounthDao().insertUser(new Modle_itemMunth(11, "hsrgrre", "11", getString(R.string.nov)));
        database.mounthDao().insertUser(new Modle_itemMunth(12, "hsrehgh", "12", getString(R.string.dec)));

        list = (ArrayList<Modle_itemMunth>) database.mounthDao().getAll();


        adpter = new AdpterMunths(list, getActivity());
        binding.rvMunths.setAdapter(adpter);
        adpter.notifyDataSetChanged();
        binding.rvMunths.scheduleLayoutAnimation();
    }

    void alrtDialogAdding() {


        final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(getActivity());

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_add_solary, null);


        EditText edt_addSalary = dialogView.findViewById(R.id.add_salary);


        alert.setNegativeButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                solary =edt_addSalary.getText().toString();
                shardEditor.saveSalary(solary);
                minusSalary();
            //    binding.tvSolary.setText(String.valueOf(shardEditor.loadData().get(ShardEditor.KEY_USER_salary)));
                dialog.dismiss();
            }
        }).setView(dialogView);
        alert.create().show();
    }

    private void minusSalary() {
        if (shardEditor.getSalary().get(ShardEditor.KEY_USER_salary)!=null && 
                shardEditor.getSalary().get(ShardEditor.KEY_USER_salary).length() > 0) {
            double getSalary = 0;
           try {
               getSalary=Double.parseDouble(String.valueOf(shardEditor.getSalary().get(ShardEditor.KEY_USER_salary)));
           } catch (Exception e) {
               e.printStackTrace();
           }

          

            if (getSalary > 0.0) {
                Double resulte = getSalary - allOutaly;
                binding.tvSolary.setText(String.valueOf(resulte));


            }
        }

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
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                releaseMediaPlayer();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener
            = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();

        }
    };
    private void playAudio(int savenod) {


        releaseMediaPlayer();
        player = MediaPlayer.create(getActivity(), savenod);
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


            player = MediaPlayer.create(getActivity(), savenod);


            player.start();
            player.setOnCompletionListener(mCompletionListener);


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