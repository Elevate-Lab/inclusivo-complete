package com.dsciiita.inclusivo.fragments.CompanyProfileFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.activities.StoryInfoActivity;
import com.dsciiita.inclusivo.adapters.CompanyStoryRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentCompanyStoriesBinding;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.responses.CompanyStoryResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.dsciiita.inclusivo.viewmodel.CompanyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyStoriesFragment extends Fragment implements CompanyStoryRVAdapter.onStoryListener  {

    private CompanyStoryRVAdapter storyAdapter;
    private List<Story> storyList;

    private CompanyViewModel viewModel;
    private FragmentCompanyStoriesBinding binding;

    public CompanyStoriesFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompanyStoriesBinding.inflate(inflater);

        binding.animationView.setOnClickListener(view-> binding.animationView.playAnimation());
        return binding.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(CompanyViewModel.class);
        viewModel.getStories().observe(getActivity(), stories -> {
            storyList = stories;
        });

        if(storyList!=null && storyList.size()==0){
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        } else {
            storyAdapter = new CompanyStoryRVAdapter(getContext(), storyList, this);
            binding.storyTabRv.setAdapter(storyAdapter);
            binding.emptyResult.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStoryClick(int position, View v) {
        startActivity(new Intent(getActivity(), StoryInfoActivity.class).putExtra("id", storyList.get(position).getId()));
    }

    @Override
    public void onStoryLongClick(int position) {

    }
}