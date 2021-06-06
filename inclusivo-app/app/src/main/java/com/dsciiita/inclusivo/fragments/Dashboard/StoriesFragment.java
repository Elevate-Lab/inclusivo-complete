package com.dsciiita.inclusivo.fragments.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.StoryInfoActivity;
import com.dsciiita.inclusivo.activities.StoryListing;
import com.dsciiita.inclusivo.adapters.DashboardStoryRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentStoriesBinding;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.responses.CompanyStoryResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoriesFragment extends Fragment {

    private DashboardStoryRVAdapter adapter;
    private List<Story> allStories;
    private String token;

    FragmentStoriesBinding binding;
    public StoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStoriesBinding.inflate(inflater);

        binding.btnViewAllStories.setOnClickListener(this::onClick);
        binding.refreshLayout.setOnRefreshListener(this::getData);

        token = "token " + SharedPrefManager.getInstance(getActivity()).getToken();

        allStories = new ArrayList<>();
        adapter = new DashboardStoryRVAdapter(getContext(), allStories, storyListener);
        binding.recentStoriesRv.setAdapter(adapter);
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
            case R.id.btnViewAllStories:
                startActivity(new Intent(getActivity(), StoryListing.class));
        }
    }


    private void getData() {

        int index = (int) (Math.random() * (9));
        binding.quote.setText((getResources().getStringArray(R.array.diversity_quotes))[index]);
        binding.progressLayout.setVisibility(View.VISIBLE);
        binding.prent.setVisibility(View.GONE);
        binding.prent.setAlpha(0);
        binding.errorView.setVisibility(View.GONE);

        getStories();
    }

    private void getStories() {
        allStories.clear();

        Call<CompanyStoryResponse> userRequestCall = ApiClient.getUserService().getAllStories(token);
        userRequestCall.enqueue(new Callback<CompanyStoryResponse>() {
            @Override
            public void onResponse(Call<CompanyStoryResponse> call, Response<CompanyStoryResponse> response) {
                if(response.isSuccessful()) {
                    allStories = response.body().getData();
                    Collections.reverse(allStories);
                    adapter.updateAdapter(allStories);

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
            public void onFailure(Call<CompanyStoryResponse> call, Throwable t) {
                binding.progressLayout.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
            }
        });
    }


    DashboardStoryRVAdapter.OnclickListener storyListener = new DashboardStoryRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(getActivity(), StoryInfoActivity.class).putExtra("id", allStories.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };
}
