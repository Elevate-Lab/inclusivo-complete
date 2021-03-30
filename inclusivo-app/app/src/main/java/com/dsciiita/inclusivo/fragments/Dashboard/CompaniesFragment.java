package com.dsciiita.inclusivo.fragments.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.activities.CompanyListingActivity;
import com.dsciiita.inclusivo.activities.CompanyProfileActivity;
import com.dsciiita.inclusivo.adapters.DashboardCompanyRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentCompaniesBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.JobFilterBody;
import com.dsciiita.inclusivo.models.JobFilterObject;
import com.dsciiita.inclusivo.models.JobFilterSearch;
import com.dsciiita.inclusivo.responses.FilterCompanyResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompaniesFragment extends Fragment {

    FragmentCompaniesBinding binding;

    private DashboardCompanyRVAdapter companyRVAdapter;
    private List<Company> companies;

    public CompaniesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompaniesBinding.inflate(inflater);

        setupAdapters();

        getData();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void setupAdapters() {
        companyRVAdapter = new DashboardCompanyRVAdapter(getActivity(), companies, onclickListener);
        binding.companyRv.setAdapter(companyRVAdapter);

        binding.refreshLayout.setOnRefreshListener(this::getData);

        binding.btnCompany.setOnClickListener(view -> startActivity(new Intent(getActivity(), CompanyListingActivity.class).putExtra("no_filter", "")));
    }

    private void getData() {

        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.prent.setVisibility(View.GONE);
        binding.prent.setAlpha(0);
        binding.errorView.setVisibility(View.GONE);

        getCompanies();
    }

    private void getCompanies() {
        String token = "token "+ SharedPrefManager.getInstance(getActivity()).getToken();

        ArrayList<JobFilterSearch> filterSearches = new ArrayList<>();

        JobFilterObject jobFilterObject = new JobFilterObject("id", "desc", filterSearches, 0, 20);
        JobFilterBody jobFilterBody = new JobFilterBody(jobFilterObject);

        Call<FilterCompanyResponse> userRequestCall = ApiClient.getUserService().getCompanyFilter(jobFilterBody, token);
        userRequestCall.enqueue(new Callback<FilterCompanyResponse>() {
            @Override
            public void onResponse(Call<FilterCompanyResponse> call, Response<FilterCompanyResponse> response) {
                if(response.isSuccessful()) {
                    companies = response.body().getData().getCompanies();
                    companyRVAdapter.updateAdapter(companies);

                    binding.prent.setVisibility(View.VISIBLE);
                    binding.prent.animate().alpha(1).setDuration(300);
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<FilterCompanyResponse> call, Throwable t) {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
            }
        });
    }

    DashboardCompanyRVAdapter.OnclickListener onclickListener = new DashboardCompanyRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(getActivity(), CompanyProfileActivity.class).putExtra("companyID", companies.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };
}