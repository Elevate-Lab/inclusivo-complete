package com.dsciiita.inclusivo.fragments.CompanyProfileFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.activities.CompanyProfileActivity;
import com.dsciiita.inclusivo.activities.InitiativeInfoActivity;
import com.dsciiita.inclusivo.adapters.CompanyInitiativeRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentCompanyInitiativesBinding;
import com.dsciiita.inclusivo.models.Initiative;
import com.dsciiita.inclusivo.responses.CompanyInitiativesResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.dsciiita.inclusivo.viewmodel.CompanyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyInitiativesFragment extends Fragment implements CompanyInitiativeRVAdapter.onInitiativeListener  {

    private CompanyInitiativeRVAdapter initiativeAdapter;
    private List<Initiative> initiativeList;

    private FragmentCompanyInitiativesBinding binding;

    private CompanyViewModel viewModel;

    public CompanyInitiativesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCompanyInitiativesBinding.inflate(inflater);

        binding.animationView.setOnClickListener(view-> binding.animationView.playAnimation());
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(CompanyViewModel.class);
        viewModel.getInitiatives().observe(getActivity(), initiatives -> {
            initiativeList = initiatives;
        });

        if(initiativeList!=null && initiativeList.size()==0){
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        } else {
            initiativeAdapter = new CompanyInitiativeRVAdapter(getContext(), initiativeList, this);
            binding.initiativeTabRv.setAdapter(initiativeAdapter);
            binding.emptyResult.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1 :
                if (resultCode == Activity.RESULT_OK) {
                    ((CompanyProfileActivity) getActivity()).getData();
                }
        }
    }

    @Override
    public void onInitiativeClick(int position, View v) {
        startActivityForResult(new Intent(getActivity(), InitiativeInfoActivity.class)
                .putExtra("id", initiativeList.get(position).getId()), 1);
    }

    @Override
    public void onInitiativeLongClick(int position) {

    }
}