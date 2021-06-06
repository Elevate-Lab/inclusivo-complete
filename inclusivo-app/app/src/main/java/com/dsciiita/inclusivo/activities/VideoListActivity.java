package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dsciiita.inclusivo.adapters.DashboardStoryRVAdapter;
import com.dsciiita.inclusivo.adapters.VideoRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityVideoListBinding;
import com.dsciiita.inclusivo.models.Video;
import com.dsciiita.inclusivo.responses.VideosAllResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListActivity extends AppCompatActivity {

    ActivityVideoListBinding binding;

    private VideoRVAdapter rvAdapter;
    private List<Video> list;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoListBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        token = "token "+ SharedPrefManager.getInstance(this).getToken();

        list = new ArrayList<>();
        rvAdapter = new VideoRVAdapter(this, list, storyListener);
        binding.videosRv.setAdapter(rvAdapter);

        binding.refreshLayout.setOnRefreshListener(this::getData);

        getData();
    }

    private void getData() {

        list.clear();
        binding.emptyResult.setVisibility(View.GONE);
        binding.videosRv.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        getStories();
    }

    private void getStories() {
        list.clear();

        Call<VideosAllResponse> userRequestCall = ApiClient.getUserService().getVideos(token);
        userRequestCall.enqueue(new Callback<VideosAllResponse>() {
            @Override
            public void onResponse(Call<VideosAllResponse> call, Response<VideosAllResponse> response) {
                if(response.isSuccessful()) {
                    list = response.body().getData();
                    Collections.reverse(list);
                    rvAdapter.updateAdapter(list);
                } else {
                    binding.emptyResult.setVisibility(View.GONE);
                    binding.animationView.playAnimation();
                }
                binding.videosRv.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<VideosAllResponse> call, Throwable t) {
                binding.videosRv.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                Snackbar.make(binding.shimmerViewContainer, "Something went wrong", BaseTransientBottomBar.LENGTH_SHORT).show();
                binding.shimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }


    VideoRVAdapter.OnClickListener storyListener = new VideoRVAdapter.OnClickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(VideoListActivity.this, VideoInfoActivity.class).putExtra("id", list.get(position).getId()));
        }

        @Override
        public void OnLongClick(int position) {

        }
    };
}