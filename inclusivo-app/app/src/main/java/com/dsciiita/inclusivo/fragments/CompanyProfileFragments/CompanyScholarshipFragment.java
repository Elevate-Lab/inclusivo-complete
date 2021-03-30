package com.dsciiita.inclusivo.fragments.CompanyProfileFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.ScholarshipDescriptionActivity;
import com.dsciiita.inclusivo.adapters.ScholarshipRVAdapter;
import com.dsciiita.inclusivo.databinding.FragmentCompanyScholarshipBinding;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.viewmodel.CompanyViewModel;

import java.util.List;

public class CompanyScholarshipFragment extends Fragment implements ScholarshipRVAdapter.OnclickListener {

    FragmentCompanyScholarshipBinding binding;
    private ScholarshipRVAdapter scholarshipRVAdapter;
    private List<Scholarship> scholarshipList;
    private CompanyViewModel viewModel;

    public CompanyScholarshipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompanyScholarshipBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(CompanyViewModel.class);
        viewModel.getScholarships().observe(getActivity(), scholarships -> {
            scholarshipList = scholarships;
        });

        if (scholarshipList != null && scholarshipList.size() == 0) {
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        } else {
            scholarshipRVAdapter = new ScholarshipRVAdapter(getContext(), R.layout.company_overview_scholarship_rv_item, scholarshipList, this);
            binding.scholarshipTabRv.setAdapter(scholarshipRVAdapter);
            binding.emptyResult.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(int position, View v) {
        startActivity(new Intent(getContext(), ScholarshipDescriptionActivity.class).putExtra("id", scholarshipList.get(position).getId()));
    }

    @Override
    public void onLongClick(int position) {

    }
}