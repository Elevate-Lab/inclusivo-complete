package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.dsciiita.inclusivo.databinding.ActivitySavedItemsBinding;

public class SavedItemsActivity extends AppCompatActivity {

    ActivitySavedItemsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedItemsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v->finish());

        binding.appliedJobCard.setOnClickListener(v->startActivity(new Intent(this, AppliedJobsActivity.class)));

        binding.saveJobCard.setOnClickListener(v->startActivity(new Intent(this, SavedJobsActivity.class)));

        binding.saveScholarshipCard.setOnClickListener(v->startActivity(new Intent(this, SavedScholarshipActivity.class)));

        binding.subsCompaniesCard.setOnClickListener(v->startActivity(new Intent(this, FollowedCompaniesActivity.class)));
    }
}