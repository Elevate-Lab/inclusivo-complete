package com.dsciiita.inclusivo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.JobRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityJobListingBinding;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.JobFilterBody;
import com.dsciiita.inclusivo.models.JobFilterObject;
import com.dsciiita.inclusivo.models.JobFilterSearch;
import com.dsciiita.inclusivo.responses.CompanyJobsResponse;
import com.dsciiita.inclusivo.responses.FilterJobResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dsciiita.inclusivo.storage.Constants.FILTER_PAGE_SIZE;

public class JobListingActivity extends AppCompatActivity implements JobRVAdapter.onJobListener {

    ActivityJobListingBinding binding;
    private JobRVAdapter jobAdapter;
    private List<Job> jobsList;
    private String token;
    private Intent intent;
    private String filter;
    private ArrayList<JobFilterSearch> filterSearches;
    private int total_count, curr_page = 0;
    private final int FILTER_CODE = 1, LIKE_CODE = 2;
    private String[] searchFields;
    private String category = "title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobListingBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        token = "token "+ SharedPrefManager.getInstance(this).getToken();

        searchFields = new String[]{"title", "company_name", "job_type", "locations", "tags"};

        filterSearches = new ArrayList<>();
        jobsList = new ArrayList<>();
        jobAdapter = new JobRVAdapter(this, jobsList, this);
        binding.jobListRv.setAdapter(jobAdapter);


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view-> finish());

        binding.filterTxt.setOnClickListener(view -> startActivityForResult(new Intent(this, FilterJobActivity.class), FILTER_CODE));
        binding.animationView.setOnClickListener(view -> binding.animationView.playAnimation());
        binding.errorAnim.setOnClickListener(view -> binding.errorAnim.playAnimation());

        binding.search.setOnClickListener(view -> {
            binding.searchBar.setVisibility(View.VISIBLE);
            binding.searchText.requestFocus();
        });


        binding.searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH && !binding.searchText.getText().toString().isEmpty()) {
                filterSearches.clear();
                JobFilterSearch jobFilterSearch = new JobFilterSearch(binding.searchText.getText().toString(), category, "");
                filterSearches.add(jobFilterSearch);
                getFilteredJobs();
                return true;
            }
            return false;
        });


        binding.category.setOnItemSelectedListener(myListener);
        binding.cancelSearch.setOnClickListener(view-> {
            binding.searchBar.setVisibility(View.GONE);
            binding.searchText.setText("");
        });


        intent = getIntent();
        getData(intent);

        if(intent.hasExtra("tags"))
            binding.title.setText("Jobs for "+intent.getStringExtra("tags"));
        else if(intent.hasExtra("locations"))
            binding.title.setText("Jobs in "+intent.getStringExtra("locations"));

        binding.jobListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState != RecyclerView.SCROLL_STATE_IDLE)
                    return;

                if (!recyclerView.canScrollVertically(1) ) {
                    if(jobsList.size()<total_count)
                        getNextChunk();
                    else {
                        binding.jobListRv.setPadding(0, 0, 0, 16);
                    }
                }
            }
        });
    }

    private void getData(Intent intent) {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        if(intent.hasExtra("filter")){
            filter = intent.getStringExtra("filter");
            if(filter.equals("preferred_cities"))
                getJobsByPrefCity();
        } else {
            Set<String> keys =  intent.getExtras().keySet();
            JobFilterSearch filterSearch;
            for(String searchField : keys) {
                filterSearch = new JobFilterSearch(intent.getStringExtra(searchField), searchField,"");
                if(searchField.equals("no_filter"))
                    filterSearches.clear();
                else
                    filterSearches.add(filterSearch);
                getFilteredJobs();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (FILTER_CODE) :
                if (resultCode == Activity.RESULT_OK) {
                    Set<String> keys =  data.getExtras().keySet();
                    filterSearches.clear();
                    for(String searchField : keys) {
                        JobFilterSearch filterSearch;
                        if(data.getStringExtra(searchField).contains("#@#"))
                            filterSearch = new JobFilterSearch(data.getStringExtra(searchField), searchField,"IN");
                        else
                            filterSearch = new JobFilterSearch(data.getStringExtra(searchField), searchField,"");
                        filterSearches.add(filterSearch);
                    }
                    getFilteredJobs();
                }
                break;
            case LIKE_CODE :
                if (resultCode == Activity.RESULT_OK) {
                    getData(intent);
                }
        }
    }


    private void getFilteredJobs() {
        binding.errorView.setVisibility(View.GONE);
        jobsList.clear();
        binding.emptyResult.setVisibility(View.GONE);
        binding.jobListRv.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        curr_page = 0;

        JobFilterObject jobFilterObject = new JobFilterObject("posted_on", "desc", filterSearches, curr_page, FILTER_PAGE_SIZE);
        JobFilterBody jobFilterBody = new JobFilterBody(jobFilterObject);

        String token = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<FilterJobResponse> userRequestCall = ApiClient.getUserService().getJobsByFilter(jobFilterBody, token);
        userRequestCall.enqueue(new Callback<FilterJobResponse>() {
            @Override
            public void onResponse(Call<FilterJobResponse> call, Response<FilterJobResponse> response) {
                if(response.isSuccessful()) {
                    jobsList = response.body().getData().getJobs();
                    displayJobs();
                    total_count = response.body().getData().getTotalCount();
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.jobListRv.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<FilterJobResponse> call, Throwable t) {
                binding.errorView.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getNextChunk() {

        binding.nextLoading.setVisibility(View.VISIBLE);
        JobFilterObject jobFilterObject = new JobFilterObject("posted_on", "desc", filterSearches, ++curr_page, FILTER_PAGE_SIZE);
        JobFilterBody jobFilterBody = new JobFilterBody(jobFilterObject);

        String token = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<FilterJobResponse> userRequestCall = ApiClient.getUserService().getJobsByFilter(jobFilterBody, token);
        userRequestCall.enqueue(new Callback<FilterJobResponse>() {
            @Override
            public void onResponse(Call<FilterJobResponse> call, Response<FilterJobResponse> response) {
                if(response.isSuccessful()) {
                    int start = jobsList.size();
                    jobsList.addAll(response.body().getData().getJobs());
                    jobAdapter.notifyItemRangeInserted(start, response.body().getData().getJobs().size());
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.nextLoading.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<FilterJobResponse> call, Throwable t) {
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void getJobsByPrefCity() {
        binding.errorView.setVisibility(View.GONE);
        jobsList = new ArrayList<>();

        Call<CompanyJobsResponse> userRequestCall = ApiClient.getUserService().getJobsByPrefCity(token);
        userRequestCall.enqueue(new Callback<CompanyJobsResponse>() {
            @Override
            public void onResponse(Call<CompanyJobsResponse> call, Response<CompanyJobsResponse> response) {
                if(response.isSuccessful()) {
                    jobsList = response.body().getData();
                    Collections.sort(jobsList, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));
                    displayJobs();
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.shimmerViewContainer.stopShimmer();
            }
            @Override
            public void onFailure(Call<CompanyJobsResponse> call, Throwable t) {
                binding.errorView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onJobClick(int position, View v) {
        startActivityForResult(new Intent(this, JobDescriptionActivity.class)
                .putExtra("id", jobsList.get(position).getJobId()), LIKE_CODE);
    }

    @Override
    public void onJobLongClick(int position) {
    }

    private void displayJobs() {
        if(jobsList.isEmpty()) {
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        }
        jobAdapter.updateAdapter(jobsList);
    }

    AdapterView.OnItemSelectedListener myListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
            int id = parent.getId();
            if (id == R.id.category) {
                category = searchFields[position];
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

}