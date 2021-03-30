package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.ScholarshipRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivitySavedScholarshipsBinding;
import com.dsciiita.inclusivo.models.LikedScholarships;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.responses.LikedScholarshipResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedScholarshipActivity extends AppCompatActivity implements ScholarshipRVAdapter.OnclickListener {

    ActivitySavedScholarshipsBinding binding;
    private ScholarshipRVAdapter jobAdapter;
    private List<Scholarship> scholarshipList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedScholarshipsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(view -> finish());
        binding.refreshLayout.setOnRefreshListener(this::getData);

        scholarshipList = new ArrayList<>();
        jobAdapter = new ScholarshipRVAdapter(this, R.layout.scholarship_rv_item, scholarshipList, this);
        binding.savedScholarshipsRv.setAdapter(jobAdapter);

        getData();
    }

    private void getData() {
        binding.errorView.setVisibility(View.GONE);
        binding.emptyResult.setVisibility(View.GONE);
        binding.savedScholarshipsRv.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        getSavedScholarships();
    }


    private void getSavedScholarships() {
        scholarshipList.clear();

        String token = "token "+SharedPrefManager.getInstance(this).getToken();
        Call<LikedScholarshipResponse> userRequestCall = ApiClient.getUserService().getLikedScholarships(token);
        userRequestCall.enqueue(new Callback<LikedScholarshipResponse>() {
            @Override
            public void onResponse(Call<LikedScholarshipResponse> call, Response<LikedScholarshipResponse> response) {
                if(response.isSuccessful()) {
                    List<LikedScholarships> likedJobsResponse = response.body().getData();
                    for (LikedScholarships object : likedJobsResponse) {
                        scholarshipList.add(object.getScholarship());
                    }
                    displayJobs();
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.savedScholarshipsRv.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<LikedScholarshipResponse> call, Throwable t) {
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            getSavedScholarships();
        }
    }

    private void displayJobs() {
        if (scholarshipList.isEmpty()) {
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        }
        Collections.sort(scholarshipList, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));
        jobAdapter.updateAdapter(scholarshipList);
    }

    @Override
    public void onClick(int position, View v) {
        startActivity(new Intent(SavedScholarshipActivity.this, ScholarshipDescriptionActivity.class).putExtra("id", scholarshipList.get(position).getId()));
    }

    @Override
    public void onLongClick(int position) {

    }
}