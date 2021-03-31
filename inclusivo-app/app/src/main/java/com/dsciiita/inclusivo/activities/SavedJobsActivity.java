package com.dsciiita.inclusivo.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dsciiita.inclusivo.adapters.JobRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivitySavedJobsBinding;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.LikedJobs;
import com.dsciiita.inclusivo.responses.LikedJobsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedJobsActivity extends AppCompatActivity implements JobRVAdapter.onJobListener {

    ActivitySavedJobsBinding binding;
    private JobRVAdapter jobAdapter;
    private List<Job> jobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedJobsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(view -> finish());
        binding.refreshLayout.setOnRefreshListener(this::getData);

        jobsList = new ArrayList<>();
        jobAdapter = new JobRVAdapter(this, jobsList, this);
        binding.savedJobsRvRemovable.setAdapter(jobAdapter);

        getData();
    }

    private void getData() {
        binding.errorView.setVisibility(View.GONE);
        binding.emptyResult.setVisibility(View.GONE);
        binding.savedJobsRvRemovable.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        getSavedJobs();
    }


    private void getSavedJobs() {
        jobsList.clear();

        String token = "token "+SharedPrefManager.getInstance(this).getToken();
        Call<LikedJobsResponse> userRequestCall = ApiClient.getUserService().getLikedJobs(token);
        userRequestCall.enqueue(new Callback<LikedJobsResponse>() {
            @Override
            public void onResponse(Call<LikedJobsResponse> call, Response<LikedJobsResponse> response) {
                if(response.isSuccessful()) {
                    List<LikedJobs> likedJobsResponse = response.body().getData();
                    for (LikedJobs object : likedJobsResponse) {
                        jobsList.add(object.getJobPost());
                    }
                    displayJobs();
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.savedJobsRvRemovable.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<LikedJobsResponse> call, Throwable t) {
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onJobClick(int position, View v) {
        startActivityForResult(new Intent(this, JobDescriptionActivity.class)
                        .putExtra("id", jobsList.get(position).getJobId()),
                1);
    }

    @Override
    public void onJobLongClick(int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            getData();
        }
    }

    private void displayJobs() {
        if(jobsList.isEmpty()) {
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        }
        Collections.sort(jobsList, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));
        jobAdapter.updateAdapter(jobsList);
    }
}