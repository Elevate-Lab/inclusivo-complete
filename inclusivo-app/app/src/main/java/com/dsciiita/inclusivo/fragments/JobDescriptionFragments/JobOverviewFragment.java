package com.dsciiita.inclusivo.fragments.JobDescriptionFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.CompanyProfileActivity;
import com.dsciiita.inclusivo.activities.JobDescriptionActivity;
import com.dsciiita.inclusivo.adapters.JobRVAdapter;
import com.dsciiita.inclusivo.databinding.FragmentJobOverviewBinding;
import com.dsciiita.inclusivo.models.Degree;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.viewmodel.JobViewModel;
import com.google.android.material.chip.Chip;

import java.util.List;

public class JobOverviewFragment extends Fragment implements JobRVAdapter.onJobListener {

    FragmentJobOverviewBinding binding;
    private JobViewModel viewModel;
    private Job job;


    public JobOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJobOverviewBinding.inflate(inflater, container, false);


        binding.descriptionReadMoreTxt.setOnClickListener(this::onClick);
        binding.selectionReadMoreTxt.setOnClickListener(this::onClick);
        binding.readMoreTxt.setOnClickListener(this::onClick);

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
        binding.roleTxt.setText(job.getJobRole());
        binding.jobDescriptionTxt.setText(job.getDescription());

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

        for (Diversity diversity: job.getDiversities()) {
            String text = diversity.getName();
            addTags(text);
        }

        binding.lastDateTxtValue.setText(job.getLastDate());
        if(job.getDisplaySalary())
            binding.jobSalary.setText(job.getMinSal()+"-"+job.getMaxSal());
        else
            binding.jobSalary.setText("---");
        binding.jobVacancy.setText(Integer.toString(job.getVacancies()));

        StringBuilder locationTxt = new StringBuilder();
        List<CityResponse> locations = job.getLocations();
        for(int i=0; i<locations.size(); i++){
            CityResponse location = locations.get(i);
            String name;
            if(i != locations.size()-1)
                name = location.getName() + ", " + location.getState().getCounty().getName() + "\n";
            else
                name = location.getName() + ", " + location.getState().getCounty().getName();
            locationTxt.append(name);
        }
        binding.jobLocation.setText(locationTxt);

        if(job.getCompany()!=null) {
            binding.companyDescription.setText(job.getCompany().getDescription());
            binding.contactNumTxt.setText(job.getCompany().getPhoneNumber());
            binding.emailTxt.setText(job.getCompany().getEmail());
        }

    }


    private void addTags(String selected){
        Chip chip = (Chip) LayoutInflater.from(getActivity())
                .inflate(R.layout.tag_chip_layout, binding.jobTagChipGrp, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        binding.jobTagChipGrp.addView(chip);
    }




    public void onClick(View view){
        if( view.getId() == R.id.description_read_more_txt) {
            ((JobDescriptionActivity)getActivity()).gotoFragment(1);
        } else if( view.getId() == R.id.selection_read_more_txt) {
            ((JobDescriptionActivity)getActivity()).gotoFragment(2);
        } else if( view.getId() == R.id.read_more_txt) {
            startActivity(new Intent(getActivity(), CompanyProfileActivity.class).putExtra("companyID", job.getCompany().getId()));
        }
    }


    @Override
    public void onJobClick(int position, View v) {
        //startActivity(new Intent(getActivity(), JobDescriptionActivity.class).putExtra("id", jobsList.get(position).getJobId()));
    }

    @Override
    public void onJobLongClick(int position) {

    }

}