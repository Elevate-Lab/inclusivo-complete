package com.dsciiita.inclusivo.fragments.ScholarshipDescriptionFragments;

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
import com.dsciiita.inclusivo.activities.ScholarshipDescriptionActivity;
import com.dsciiita.inclusivo.databinding.FragmentScholarshipOverviewBinding;
import com.dsciiita.inclusivo.models.Degree;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.viewmodel.ScholarshipViewModel;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ScholarshipOverviewFragment extends Fragment {

    private FragmentScholarshipOverviewBinding binding;
    private ScholarshipViewModel viewModel;
    private Scholarship scholarship;

    public ScholarshipOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScholarshipOverviewBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        binding.descriptionReadMoreTxt.setOnClickListener(this::onClick);
        binding.selectionReadMoreTxt.setOnClickListener(this::onClick);
        binding.aboutCompanyReadMoreTxt.setOnClickListener(this::onClick);
        return binding.getRoot();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.description_read_more_txt) {
            ((ScholarshipDescriptionActivity) getActivity()).gotoFragment(1);
        } else if (view.getId() == R.id.selection_read_more_txt) {
            ((ScholarshipDescriptionActivity) getActivity()).gotoFragment(2);
        } else if (view.getId() == R.id.about_company_read_more_txt) {
            startActivity(new Intent(getActivity(), CompanyProfileActivity.class).putExtra("companyID", scholarship.getCompany().getId()));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ScholarshipViewModel.class);
        scholarship = viewModel.getScholarship();
        setValues();
    }

    private void setValues() {
        binding.scholarshipDescriptionTxt.setText(scholarship.getDescription());

        StringBuilder degreesTxt = new StringBuilder();
        List<Degree> degrees = scholarship.getAcceptedDegrees();
        for (int i = 0; i < degrees.size(); i++) {
            Degree degree = degrees.get(i);
            String name;
            if (i != degrees.size() - 1)
                name = degree.getName() + ", " + degree.getType() + "\n";
            else
                name = degree.getName() + ", " + degree.getType();
            degreesTxt.append(name);
        }
        binding.degreesTxtValue.setText(degreesTxt);

        binding.selectionProcessValue.setText(scholarship.getSelectionProcess());

        for (Diversity diversity : scholarship.getTags()) {
            String text = diversity.getName();
            addTags(text);
        }

        binding.lastDateTxtValue.setText(scholarship.getLastDate());
        binding.scholarshipVacancy.setText(Integer.toString(scholarship.getVacancies()));

        if(scholarship.getCompany()!=null) {
            binding.companyDescription.setText(scholarship.getCompany().getDescription());
            binding.contactNumTxt.setText(scholarship.getCompany().getPhoneNumber());
            binding.emailTxt.setText(scholarship.getCompany().getEmail());
        } else {
            binding.compamyLayout.setVisibility(View.GONE);
        }

    }

    private void addTags(String selected) {
        Chip chip = (Chip) LayoutInflater.from(getActivity())
                .inflate(R.layout.tag_chip_layout, binding.scholarshipTagChipGrp, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        binding.scholarshipTagChipGrp.addView(chip);
    }
}