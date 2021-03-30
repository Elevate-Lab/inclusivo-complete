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
import com.dsciiita.inclusivo.databinding.FragmentScholarshipMoreInfoBinding;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.viewmodel.ScholarshipViewModel;
import com.google.android.material.chip.Chip;

public class ScholarshipMoreInfoFragment extends Fragment {

    private FragmentScholarshipMoreInfoBinding binding;

    private ScholarshipViewModel viewModel;
    private Scholarship scholarship;

    public ScholarshipMoreInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScholarshipMoreInfoBinding.inflate(inflater, container, false);
        binding.readMoreTxt.setOnClickListener(this::onClick);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ScholarshipViewModel.class);
        scholarship = viewModel.getScholarship();
        setValues();
    }

    private void setValues() {
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

    public void onClick(View view) {
        if (view.getId() == R.id.read_more_txt) {
            startActivity(new Intent(getActivity(), CompanyProfileActivity.class).putExtra("companyID", scholarship.getCompany().getId()));
        }
    }
}