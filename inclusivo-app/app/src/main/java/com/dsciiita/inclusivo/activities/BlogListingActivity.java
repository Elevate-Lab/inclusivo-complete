package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dsciiita.inclusivo.adapters.DashboardBlogsRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityBlogListingBinding;
import com.dsciiita.inclusivo.models.Blog;
import com.dsciiita.inclusivo.responses.BlogsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogListingActivity extends AppCompatActivity {

    ActivityBlogListingBinding binding;

    private DashboardBlogsRVAdapter rvAdapter;
    private List<Blog> list;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlogListingBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        token = "token "+ SharedPrefManager.getInstance(this).getToken();

        list = new ArrayList<>();
        rvAdapter = new DashboardBlogsRVAdapter(this, list, storyListener);
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

        getBlogs();
    }

    private void getBlogs() {
        list.clear();

        Call<BlogsResponse> userRequestCall = ApiClient.getUserService().getBlogs(token);
        userRequestCall.enqueue(new Callback<BlogsResponse>() {
            @Override
            public void onResponse(Call<BlogsResponse> call, Response<BlogsResponse> response) {
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
            public void onFailure(Call<BlogsResponse> call, Throwable t) {
                binding.videosRv.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                Snackbar.make(binding.shimmerViewContainer, "Something went wrong", BaseTransientBottomBar.LENGTH_SHORT).show();
                binding.shimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }


    DashboardBlogsRVAdapter.OnclickListener storyListener = new DashboardBlogsRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(BlogListingActivity.this, BlogInfoActivity.class).putExtra("id", list.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };

}
