package com.dsciiita.inclusivo.fragments.ScholarshipDescriptionFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.databinding.FragmentScholarshipAboutBinding;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.viewmodel.ScholarshipViewModel;

public class ScholarshipAboutFragment extends Fragment {

    FragmentScholarshipAboutBinding binding;
    private ScholarshipViewModel viewModel;
    private Scholarship scholarship;

    public ScholarshipAboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScholarshipAboutBinding.inflate(inflater, container, false);
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
        binding.scholarshipDescriptionTxt.setText(scholarship.getDescription());
    }
}