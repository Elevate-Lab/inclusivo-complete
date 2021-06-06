package com.dsciiita.inclusivo.fragments.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.BlogInfoActivity;
import com.dsciiita.inclusivo.activities.BlogListingActivity;
import com.dsciiita.inclusivo.activities.StoryInfoActivity;
import com.dsciiita.inclusivo.activities.StoryListing;
import com.dsciiita.inclusivo.activities.VideoInfoActivity;
import com.dsciiita.inclusivo.activities.VideoListActivity;
import com.dsciiita.inclusivo.adapters.DashboardBlogsRVAdapter;
import com.dsciiita.inclusivo.adapters.DashboardStoryRVAdapter;
import com.dsciiita.inclusivo.adapters.VideoRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentUpskillBinding;
import com.dsciiita.inclusivo.models.Blog;
import com.dsciiita.inclusivo.models.Video;
import com.dsciiita.inclusivo.responses.BlogsResponse;
import com.dsciiita.inclusivo.responses.VideosAllResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpskillFragment extends Fragment {

    private DashboardBlogsRVAdapter adapter;
    private VideoRVAdapter videoAdapter;
    private List<Blog> allBlogs;
    private List<Video> allVideos;
    private String token;

    FragmentUpskillBinding binding;
    public UpskillFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpskillBinding.inflate(inflater);

        binding.refreshLayout.setOnRefreshListener(this::getData);

        token = "token " + SharedPrefManager.getInstance(getActivity()).getToken();

        allBlogs = new ArrayList<>();
        allVideos = new ArrayList<>();

        videoAdapter = new VideoRVAdapter(getContext(), allVideos, videoListener);
        adapter = new DashboardBlogsRVAdapter(getContext(), allBlogs, storyListener);

        binding.videoRv.setAdapter(videoAdapter);
        binding.blogRv.setAdapter(adapter);

        binding.viewAllBlogs.setOnClickListener(this::onClick);
        binding.viewAllVideos.setOnClickListener(this::onClick);

        getData();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.view_all_blogs:
                startActivity(new Intent(getActivity(), BlogListingActivity.class));
                break;
            case R.id.view_all_videos:
                startActivity(new Intent(getActivity(), VideoListActivity.class));
                break;
        }
    }


    private void getData() {

        int index = (int) (Math.random() * (9));
        binding.quote.setText((getResources().getStringArray(R.array.diversity_quotes))[index]);
        binding.progressLayout.setVisibility(View.VISIBLE);
        binding.prent.setVisibility(View.GONE);
        binding.prent.setAlpha(0);
        binding.errorView.setVisibility(View.GONE);

        getBlogs();
        getVideos();
    }


    private void getBlogs() {
        allBlogs.clear();

        Call<BlogsResponse> userRequestCall = ApiClient.getUserService().getBlogs(token);
        userRequestCall.enqueue(new Callback<BlogsResponse>() {
            @Override
            public void onResponse(Call<BlogsResponse> call, Response<BlogsResponse> response) {
                if(response.isSuccessful()) {
                    allBlogs = response.body().getData();
                    Collections.reverse(allBlogs);
                    if(allBlogs.size()>3)
                        allBlogs = allBlogs.subList(0, 3);
                    adapter.updateAdapter(allBlogs);

                    binding.prent.setVisibility(View.VISIBLE);
                    binding.prent.animate().alpha(1).setDuration(300);
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.progressLayout.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<BlogsResponse> call, Throwable t) {
                binding.progressLayout.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
            }
        });
    }



    private void getVideos() {
        allVideos.clear();

        Call<VideosAllResponse> userRequestCall = ApiClient.getUserService().getVideos(token);
        userRequestCall.enqueue(new Callback<VideosAllResponse>() {
            @Override
            public void onResponse(Call<VideosAllResponse> call, Response<VideosAllResponse> response) {
                if(response.isSuccessful()) {
                    allVideos = response.body().getData();
                    Collections.reverse(allVideos);
                    if(allVideos.size()>3)
                        allVideos = allVideos.subList(0, 3);
                    videoAdapter.updateAdapter(allVideos);

                    binding.prent.setVisibility(View.VISIBLE);
                    binding.prent.animate().alpha(1).setDuration(300);
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.progressLayout.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<VideosAllResponse> call, Throwable t) {
                binding.progressLayout.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
            }
        });
    }



    DashboardBlogsRVAdapter.OnclickListener storyListener = new DashboardBlogsRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(getActivity(), BlogInfoActivity.class).putExtra("id", allBlogs.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };

    VideoRVAdapter.OnClickListener videoListener = new VideoRVAdapter.OnClickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(getActivity(), VideoInfoActivity.class).putExtra("id", allVideos.get(position).getId()));
        }

        @Override
        public void OnLongClick(int position) {

        }
    };
}
