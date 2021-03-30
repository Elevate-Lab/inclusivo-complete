package com.dsciiita.inclusivo.fragments.JobDescriptionFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.databinding.FragmentJobAboutBinding;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.viewmodel.JobViewModel;

public class JobAboutFragment extends Fragment {
    FragmentJobAboutBinding bindng;

    private JobViewModel viewModel;
    private Job job;

    public JobAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bindng = FragmentJobAboutBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return bindng.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(JobViewModel.class);
        job = viewModel.getJob();
        setValues();
    }

    private void setValues() {
        bindng.roleTxt.setText(job.getJobRole());
        bindng.jobDescriptionTxt.setText(job.getDescription());
    }
}