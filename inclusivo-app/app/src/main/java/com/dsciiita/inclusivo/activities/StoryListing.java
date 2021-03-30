package com.dsciiita.inclusivo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.DashboardStoryRVAdapter;
import com.dsciiita.inclusivo.adapters.ScholarshipRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityScholarshipListingBinding;
import com.dsciiita.inclusivo.databinding.ActivityStoryListingBinding;
import com.dsciiita.inclusivo.models.JobFilterBody;
import com.dsciiita.inclusivo.models.JobFilterObject;
import com.dsciiita.inclusivo.models.JobFilterSearch;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.responses.CompanyStoryResponse;
import com.dsciiita.inclusivo.responses.FilterScholarshipsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dsciiita.inclusivo.storage.Constants.FILTER_PAGE_SIZE;

public class StoryListing extends AppCompatActivity {

    ActivityStoryListingBinding binding;

    private DashboardStoryRVAdapter rvAdapter;
    private List<Story> list;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryListingBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(view -> finish());

        token = "token "+ SharedPrefManager.getInstance(this).getToken();

        list = new ArrayList<>();
        rvAdapter = new DashboardStoryRVAdapter(this, list, storyListener);
        binding.storiesRv.setAdapter(rvAdapter);

        binding.refreshLayout.setOnRefreshListener(this::getData);

        getData();
    }

    private void getData() {

        list.clear();
        binding.emptyResult.setVisibility(View.GONE);
        binding.storiesRv.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        getStories();
    }

    private void getStories() {
        list.clear();

        Call<CompanyStoryResponse> userRequestCall = ApiClient.getUserService().getAllStories(token);
        userRequestCall.enqueue(new Callback<CompanyStoryResponse>() {
            @Override
            public void onResponse(Call<CompanyStoryResponse> call, Response<CompanyStoryResponse> response) {
                if(response.isSuccessful()) {
                    list = response.body().getData();
                    Collections.reverse(list);
                    rvAdapter.updateAdapter(list);
                } else {
                    binding.emptyResult.setVisibility(View.GONE);
                    binding.animationView.playAnimation();
                }
                binding.storiesRv.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<CompanyStoryResponse> call, Throwable t) {
                binding.storiesRv.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                Snackbar.make(binding.shimmerViewContainer, "Something went wrong", BaseTransientBottomBar.LENGTH_SHORT);
                binding.shimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }


    DashboardStoryRVAdapter.OnclickListener storyListener = new DashboardStoryRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(StoryListing.this, StoryInfoActivity.class).putExtra("id", list.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };


}