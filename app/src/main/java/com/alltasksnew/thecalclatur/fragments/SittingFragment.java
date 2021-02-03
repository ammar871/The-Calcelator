package com.alltasksnew.thecalclatur.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alltasksnew.thecalclatur.BuildConfig;
import com.alltasksnew.thecalclatur.R;
import com.alltasksnew.thecalclatur.commen.Commen;
import com.alltasksnew.thecalclatur.databinding.FragmentSittingBinding;
import com.alltasksnew.thecalclatur.shardeditor.ShardEditor;

import java.util.List;


public class SittingFragment extends Fragment implements View.OnClickListener {

    FragmentSittingBinding binding;
    ShardEditor shardEditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Commen.setlang(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sitting, container, false);
        shardEditor = new ShardEditor(getActivity());
        getColorLang();
        inItView();
        getSelectLang();

        return binding.getRoot();
    }

    private void inItView() {
        binding.imgUpdate.setOnClickListener(this);
        binding.imgFace.setOnClickListener(this);
        binding.imgEmail.setOnClickListener(this);
        binding.imgRate.setOnClickListener(this);
        binding.imgShar.setOnClickListener(this);

    }

    private void getSelectLang() {
        binding.tvAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShardEditor.saveLanguage(getActivity(), "ar");
                shardEditor.setLanguageSelect(getActivity(), true);
                Commen.setlang(getActivity());
                getColorLang();


            }
        });
        binding.tvEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShardEditor.saveLanguage(getActivity(), "en");
                shardEditor.setLanguageSelect(getActivity(), true);

                getColorLang();
                Commen.setlang(getActivity());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Commen.setlang(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        Commen.setlang(getActivity());
    }


    private void getColorLang() {

        if (ShardEditor.getLanguage(getActivity()).equals("en")) {
            binding.tvEng.setTextColor(getResources().getColor(R.color.violet));
            binding.tvAr.setTextColor(getResources().getColor(R.color.white));

        } else if (ShardEditor.getLanguage(getActivity()).equals("ar")) {
            binding.tvAr.setTextColor(getResources().getColor(R.color.violet));
            binding.tvEng.setTextColor(getResources().getColor(R.color.white));
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_update:

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pname:com.alltasksnew.thecalclatur"));
                startActivity(intent);
                break;
            case R.id.img_rate:
                Uri uri = Uri.parse("market://details?id=" + "com.alltasksnew.thecalclatur");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), " unable to find market app", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.img_face:
                PackageManager packageManager = getActivity().getPackageManager();
                if (getOpenFacebookIntent().resolveActivity(packageManager) != null) {
                    startActivity(getOpenFacebookIntent());
                } else {
                    Toast.makeText(getActivity(), "هذه الصفحة غير موجودة", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_email:

                final Intent intentg = new Intent(android.content.Intent.ACTION_SEND);
                intentg.setType("text/plain");
                intentg.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"alltasksapp@gmail.com"});
                final PackageManager pm =getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(intentg, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") ||
                            info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                if (best != null)
                    intentg.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(intentg);


                break;
            case R.id.img_shar:
                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String aboutMessage = "all your tasks in one places" + "\n";


                    String sharApp = aboutMessage + "https://play.google.com/store/apps/details?id="
                            + BuildConfig.APPLICATION_ID + "\n\n";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharApp);
                    startActivity(Intent.createChooser(sharingIntent, "تطبيق All Tasks"));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "فشلت العملية", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;


        }
    }
    public Intent getOpenFacebookIntent() {
        try {
           getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/AllTasksApp/"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/AllTasksApp/"));
        }
    }
}