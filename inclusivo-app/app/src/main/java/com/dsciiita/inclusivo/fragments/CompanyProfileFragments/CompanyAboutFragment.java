package com.dsciiita.inclusivo.fragments.CompanyProfileFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.CompanyProfileActivity;
import com.dsciiita.inclusivo.databinding.FragmentCompanyAboutBinding;
import com.dsciiita.inclusivo.databinding.FragmentUpdateProfileBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.viewmodel.CompanyViewModel;

public class CompanyAboutFragment extends Fragment {

    private CompanyViewModel viewModel;
    private Company company;
    private FragmentCompanyAboutBinding binding;

    public CompanyAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompanyAboutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(CompanyViewModel.class);

        viewModel.getText().observe(getActivity(), company -> {
            if(company!=null)
                binding.about.setText(company.getDescription());
        });

    }
}