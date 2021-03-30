package com.dsciiita.inclusivo.fragments.JobDescriptionFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.databinding.FragmentJobMoreInfoBinding;
import com.dsciiita.inclusivo.databinding.FragmentJobRecruitmentBinding;
import com.dsciiita.inclusivo.models.Degree;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.viewmodel.JobViewModel;

import java.util.List;

public class JobRecruitmentFragment extends Fragment {

    FragmentJobRecruitmentBinding binding;


    private JobViewModel viewModel;
    private Job job;


    public JobRecruitmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJobRecruitmentBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(JobViewModel.class);
        job = viewModel.getJob();
        setValues();
    }

    private void setValues() {
        StringBuilder degreesTxt = new StringBuilder();
        List<Degree> degrees = job.getDegrees();
        for(int i=0; i<degrees.size(); i++){
            Degree degree = degrees.get(i);
            String name;
            if(i!= degrees.size()-1)
                name = degree.getName()+", "+degree.getType()+"\n";
            else
                name = degree.getName()+", "+degree.getType();
            degreesTxt.append(name);
        }
        binding.degreesTxtValue.setText(degreesTxt);

        String exp = job.getMinExp()+"-"+job.getMaxExp()+" years";
        binding.expTxtValue.setText(exp);

        binding.selectionProcessValue.setText(job.getSelectionProcess());
    }
}