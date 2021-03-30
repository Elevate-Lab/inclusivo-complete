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

import com.dsciiita.inclusivo.activities.JobDescriptionActivity;
import com.dsciiita.inclusivo.adapters.CompanyJobRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.responses.CompanyJobsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.dsciiita.inclusivo.viewmodel.CompanyViewModel;
import com.dsciiita.inclusivo.databinding.FragmentCompanyJobsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyJobsFragment extends Fragment implements CompanyJobRVAdapter.onJobListener {


    private CompanyJobRVAdapter jobAdapter;
    private List<Job> jobsList;

    private FragmentCompanyJobsBinding binding;

    private CompanyViewModel viewModel;

    public CompanyJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompanyJobsBinding.inflate(inflater, container, false);

        binding.animationView.setOnClickListener(view-> binding.animationView.playAnimation());
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(CompanyViewModel.class);
        viewModel.getJobList().observe(getActivity(), jobs -> {
            jobsList = jobs;
        });

        if(jobsList!=null && jobsList.size()==0){
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        } else {
            jobAdapter = new CompanyJobRVAdapter(getContext(), jobsList, this);
            binding.jobsTabRv.setAdapter(jobAdapter);
            binding.emptyResult.setVisibility(View.GONE);
        }

    }



    @Override
    public void onJobClick(int position, View v) {
        startActivity(new Intent(getActivity(), JobDescriptionActivity.class).putExtra("id", jobsList.get(position).getJobId()));
    }

    @Override
    public void onJobLongClick(int position) {

    }

}