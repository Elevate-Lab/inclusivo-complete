package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.databinding.ActivitySettingsBinding;
import com.dsciiita.inclusivo.storage.SharedPrefManager;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v->finish());

        binding.rate.setOnClickListener(view -> {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        });

        binding.notifications.setOnClickListener(v->startActivity(new Intent(this, NotificationPreferencesActivity.class)));

        binding.share.setOnClickListener(v->{
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "\nDownload Inclusivo: Jobs for all from Google Play Store\nhttps://play.google.com/store/apps/details?id=com.dsciiita.inclusivo";
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });

        binding.privacy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://inclusivo.netlify.app/legal"));
            startActivity(intent);
        });


        binding.logout.setOnClickListener(v->{
            new AlertDialog.Builder(this)
                    .setMessage("Sure you want to logout?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        SharedPrefManager.getInstance(this).clear();
                        Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SettingsActivity.this, EmailActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

    }
}