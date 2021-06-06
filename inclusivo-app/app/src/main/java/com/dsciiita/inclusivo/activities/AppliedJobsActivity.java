package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.AppliedJobsRV;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityAppliedJobsBinding;
import com.dsciiita.inclusivo.models.JobApplicationByCandidateData;
import com.dsciiita.inclusivo.models.JobFilterSearch;
import com.dsciiita.inclusivo.responses.ApplicationByCandidate;
import com.dsciiita.inclusivo.responses.LikedJobsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppliedJobsActivity extends AppCompatActivity {

    private List<JobApplicationByCandidateData> appliedJobs;
    private AppliedJobsRV appliedJobsAdapter;

    private ActivityAppliedJobsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppliedJobsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.titleToolbar.setText("Applied Jobs");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        binding.refreshLayout.setOnRefreshListener(this::getData);


        appliedJobs = new ArrayList<>();
        appliedJobsAdapter = new AppliedJobsRV(AppliedJobsActivity.this, appliedJobs, appliedJobListener);
        binding.jobListRv.setAdapter(appliedJobsAdapter);

        getData();

    }

    private void getData() {
        binding.errorView.setVisibility(View.GONE);
        binding.emptyResult.setVisibility(View.GONE);
        binding.jobListRv.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        getAppliedJobs();
    }


    private void getAppliedJobs() {
        appliedJobs.clear();

        String token = "token "+ SharedPrefManager.getInstance(this).getToken();
        Call<ApplicationByCandidate> userRequestCall = ApiClient.getUserService().getAppliedJobs(token);
        userRequestCall.enqueue(new Callback<ApplicationByCandidate>() {
            @Override
            public void onResponse(Call<ApplicationByCandidate> call, Response<ApplicationByCandidate> response) {
                if(response.isSuccessful()) {
                    appliedJobs = response.body().getData();
                    displayJobs();
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.jobListRv.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<ApplicationByCandidate> call, Throwable t) {
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 2 :
                if (resultCode == Activity.RESULT_OK) {
                    getData();
                }
        }
    }


    AppliedJobsRV.onJobListener appliedJobListener = new AppliedJobsRV.onJobListener() {
        @Override
        public void onJobClick(int position, View v) {
            startActivityForResult(new Intent(getApplicationContext(), JobDescriptionActivity.class)
                    .putExtra("id", appliedJobs.get(position).getJob().getJobId()), 2);
        }

        @Override
        public void onJobLongClick(int position) {

        }
    };

    private void displayJobs() {
        if(appliedJobs.isEmpty()) {
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        }
        Collections.sort(appliedJobs, (obj1, obj2) -> obj2.getApplicationDate().compareToIgnoreCase(obj1.getApplicationDate()));
        appliedJobsAdapter.updateAdapter(appliedJobs);
    }
}