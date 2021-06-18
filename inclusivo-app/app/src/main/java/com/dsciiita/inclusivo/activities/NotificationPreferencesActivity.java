package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.databinding.ActivityNotificationPreferencesBinding;

public class NotificationPreferencesActivity extends AppCompatActivity {

    ActivityNotificationPreferencesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationPreferencesBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v->finish());


    }
}