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
import com.dsciiita.inclusivo.adapters.ScholarshipRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityScholarshipListingBinding;
import com.dsciiita.inclusivo.models.JobFilterBody;
import com.dsciiita.inclusivo.models.JobFilterObject;
import com.dsciiita.inclusivo.models.JobFilterSearch;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.responses.FilterScholarshipsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dsciiita.inclusivo.storage.Constants.FILTER_PAGE_SIZE;

public class ScholarshipListing extends AppCompatActivity {

    ActivityScholarshipListingBinding binding;

    private ScholarshipRVAdapter rvAdapter;
    private List<Scholarship> list;
    private String token;
    private int id;
    private String filter;
    private ArrayList<JobFilterSearch> filterSearches;

    private int total_count, curr_page = 0;
    private final int FILTER_CODE = 2;

    private String[] searchFields;
    private String category = "title";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScholarshipListingBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view -> finish());

        binding.shimmerViewContainer.startShimmer();

        token = "token "+ SharedPrefManager.getInstance(this).getToken();

        filterSearches = new ArrayList<>();
        list = new ArrayList<>();

        Intent intent = getIntent();

        Set<String> keys =  intent.getExtras().keySet();
        JobFilterSearch filterSearch;
        for(String searchField : keys) {
            filterSearch = new JobFilterSearch(intent.getStringExtra(searchField), searchField,"");
            if(searchField.equals("no_filter"))
                filterSearches.clear();
            else
                filterSearches.add(filterSearch);
            getFilteredData();
        }


        rvAdapter = new ScholarshipRVAdapter(this, R.layout.scholarship_rv_item, list, allListener);
        binding.scholarshipsRv.setAdapter(rvAdapter);

        binding.filterTxt.setOnClickListener(view -> startActivityForResult(new Intent(this, FilterScholarshipActivity.class), FILTER_CODE));

        binding.animationView.setOnClickListener(view -> binding.animationView.playAnimation());

        binding.scholarshipsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState != RecyclerView.SCROLL_STATE_IDLE)
                    return;

                if (!recyclerView.canScrollVertically(1) ) {
                    if(list.size()<total_count)
                        getNextChunk();
                    else {
                        binding.scholarshipsRv.setPadding(0, 0, 0, 16);
                    }
                }
            }
        });


        searchFields = new String[]{"title", "company_name", "tags"};
        binding.search.setOnClickListener(view -> {
            binding.searchBar.setVisibility(View.VISIBLE);
            binding.searchText.requestFocus();
        });
        binding.searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH && !binding.searchText.getText().toString().isEmpty()) {
                filterSearches.clear();
                JobFilterSearch jobFilterSearch = new JobFilterSearch(binding.searchText.getText().toString(), category, "");
                filterSearches.add(jobFilterSearch);
                getFilteredData();
                return true;
            }
            return false;
        });
        binding.category.setOnItemSelectedListener(myListener);
        binding.cancelSearch.setOnClickListener(view-> {
            binding.searchBar.setVisibility(View.GONE);
            binding.searchText.setText("");
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (FILTER_CODE) : {
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
                    getFilteredData();
                }
                break;
            }
        }
    }


    private void getFilteredData() {
        list.clear();
        binding.emptyResult.setVisibility(View.GONE);
        binding.scholarshipsRv.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        curr_page = 0;

        JobFilterObject jobFilterObject = new JobFilterObject("id", "desc", filterSearches, curr_page, FILTER_PAGE_SIZE);
        JobFilterBody jobFilterBody = new JobFilterBody(jobFilterObject);


        Call<FilterScholarshipsResponse> userRequestCall = ApiClient.getUserService().getScholarships(jobFilterBody, token);
        userRequestCall.enqueue(new Callback<FilterScholarshipsResponse>() {
            @Override
            public void onResponse(Call<FilterScholarshipsResponse> call, Response<FilterScholarshipsResponse> response) {
                if(response.isSuccessful()) {
                    list = response.body().getData().getScholarships();
                    displayList();
                    total_count = response.body().getData().getTotalCount();
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.scholarshipsRv.setVisibility(View.VISIBLE);
                binding.shimmerViewContainer.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<FilterScholarshipsResponse> call, Throwable t) {
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getNextChunk() {
        binding.nextLoading.setVisibility(View.VISIBLE);
        JobFilterObject jobFilterObject = new JobFilterObject("posted_on", "desc", filterSearches, ++curr_page, FILTER_PAGE_SIZE);
        JobFilterBody jobFilterBody = new JobFilterBody(jobFilterObject);

        Call<FilterScholarshipsResponse> userRequestCall = ApiClient.getUserService().getScholarships(jobFilterBody, token);
        userRequestCall.enqueue(new Callback<FilterScholarshipsResponse>() {
            @Override
            public void onResponse(Call<FilterScholarshipsResponse> call, Response<FilterScholarshipsResponse> response) {
                if(response.isSuccessful()) {
                    int start = list.size();
                    list.addAll(response.body().getData().getScholarships());
                    rvAdapter.notifyItemRangeInserted(start, response.body().getData().getScholarships().size());
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.nextLoading.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<FilterScholarshipsResponse> call, Throwable t) {
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    ScholarshipRVAdapter.OnclickListener allListener = new ScholarshipRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(ScholarshipListing.this, ScholarshipDescriptionActivity.class).putExtra("id", list.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };

    private void displayList() {
        if(list.isEmpty()) {
            binding.emptyResult.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        }
        rvAdapter.updateAdapter(list);
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