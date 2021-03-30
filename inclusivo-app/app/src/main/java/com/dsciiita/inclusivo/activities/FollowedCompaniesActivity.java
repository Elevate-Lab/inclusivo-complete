package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.CompanyRVAdapter;
import com.dsciiita.inclusivo.adapters.CompanyRVAdapter;
import com.dsciiita.inclusivo.adapters.DashboardCompanyRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityFollowedCompaniesBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.FollowedCompanyData;
import com.dsciiita.inclusivo.responses.CompanyJobsResponse;
import com.dsciiita.inclusivo.responses.FollowedCompaniesResponse;
import com.dsciiita.inclusivo.responses.LikedJobsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowedCompaniesActivity extends AppCompatActivity implements DashboardCompanyRVAdapter.OnclickListener {

    ActivityFollowedCompaniesBinding binding;
    private DashboardCompanyRVAdapter companyAdapter;
    private List<Company> companyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowedCompaniesBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.shimmerViewContainer.startShimmer();

        binding.toolbar.setNavigationOnClickListener(view -> finish());
        binding.refreshLayout.setOnRefreshListener(this::getData);

        companyAdapter = new DashboardCompanyRVAdapter(this, companyList, this);
        binding.companyListRv.setAdapter(companyAdapter);

        getData();
    }


    private void getData() {
        binding.errorView.setVisibility(View.GONE);
        binding.emptyResult.setVisibility(View.GONE);
        binding.companyListRv.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        getFollowedCompanies();
    }


    private void getFollowedCompanies() {
        companyList = new ArrayList<>();
        String token  = "token "+ SharedPrefManager.getInstance(this).getToken();
        Call<FollowedCompaniesResponse> userRequestCall = ApiClient.getUserService().getFollowedCompanies(token);
        userRequestCall.enqueue(new Callback<FollowedCompaniesResponse>() {
            @Override
            public void onResponse(Call<FollowedCompaniesResponse> call, Response<FollowedCompaniesResponse> response) {
                if(response.isSuccessful()) {
                    List<FollowedCompanyData> companiesResponse = response.body().getData();
                    for(FollowedCompanyData data: companiesResponse){
                        companyList.add(data.getCompanyModel().getCompany());
                        companyAdapter.notifyDataSetChanged();
                    }
                    displayItems();
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.companyListRv.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<FollowedCompaniesResponse> call, Throwable t) {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
            }
        });
    }

    private void displayItems() {
        if(companyList.isEmpty()) {
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        }
        companyAdapter.updateAdapter(companyList);
    }

    @Override
    public void onClick(int position, View v) {
        startActivity(new Intent(this, CompanyProfileActivity.class).putExtra("companyID", companyList.get(position).getId()));
    }

    @Override
    public void onLongClick(int position) {

    }
}