package com.dsciiita.inclusivo.fragments.ScholarshipDescriptionFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.databinding.FragmentScholarshipSelectionBinding;
import com.dsciiita.inclusivo.models.Degree;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.viewmodel.ScholarshipViewModel;

import java.util.List;

public class ScholarshipSelectionFragment extends Fragment {

    private FragmentScholarshipSelectionBinding binding;
    private ScholarshipViewModel viewModel;
    private Scholarship scholarship;

    public ScholarshipSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScholarshipSelectionBinding.inflate(inflater, container, false);
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
    }
}