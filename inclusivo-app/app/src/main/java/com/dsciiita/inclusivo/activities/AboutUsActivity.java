package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dsciiita.inclusivo.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutUsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "Version "+pInfo.versionName;
            binding.versionCode.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            binding.versionCode.setVisibility(View.GONE);
        }
    }
}