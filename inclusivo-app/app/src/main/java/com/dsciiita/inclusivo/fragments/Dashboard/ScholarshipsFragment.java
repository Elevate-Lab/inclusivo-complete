package com.dsciiita.inclusivo.fragments.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.SavedScholarshipActivity;
import com.dsciiita.inclusivo.activities.ScholarshipDescriptionActivity;
import com.dsciiita.inclusivo.activities.ScholarshipListing;
import com.dsciiita.inclusivo.adapters.DashboardCircleTagsRVAdapter;
import com.dsciiita.inclusivo.adapters.ScholarshipRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentScholarshipsBinding;
import com.dsciiita.inclusivo.models.JobFilterBody;
import com.dsciiita.inclusivo.models.JobFilterObject;
import com.dsciiita.inclusivo.models.JobFilterSearch;
import com.dsciiita.inclusivo.models.LikedScholarships;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.models.TitleLinkObject;
import com.dsciiita.inclusivo.responses.FilterScholarshipsResponse;
import com.dsciiita.inclusivo.responses.LikedScholarshipResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dsciiita.inclusivo.storage.Constants.FEMALE_URL;
import static com.dsciiita.inclusivo.storage.Constants.LGBTQI_URL;
import static com.dsciiita.inclusivo.storage.Constants.SPECIALLY_ABLED_URL;
import static com.dsciiita.inclusivo.storage.Constants.VETERAN_URL;
import static com.dsciiita.inclusivo.storage.Constants.WORKING_MOTHER_URL;

public class ScholarshipsFragment extends Fragment {

    FragmentScholarshipsBinding binding;


    private DashboardCircleTagsRVAdapter tagAdapter;
    private ScholarshipRVAdapter savedScholarshipAdapter, recentAdapter;
    private List<Scholarship> savedList, allScholarshipList;
    private List<TitleLinkObject> tagList;
    private String token;

    public ScholarshipsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScholarshipsBinding.inflate(inflater);
        setupAdapters();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void setupAdapters() {
        binding.viewAllSavedJobs.setOnClickListener(this::onClick);
        binding.btnScholarships.setOnClickListener(this::onClick);

        token = "token " + SharedPrefManager.getInstance(getActivity()).getToken();

        tagList = new ArrayList<>();
        tagAdapter = new DashboardCircleTagsRVAdapter(getContext(), R.layout.job_tags_rv_item, tagList, tagListener);
        binding.dashboardTagsRv.setAdapter(tagAdapter);

        savedList = new ArrayList<>();
        savedScholarshipAdapter = new ScholarshipRVAdapter(getContext(), R.layout.scholarship_rv_item, savedList, savedListener);
        binding.savedScholarshipsRv.setAdapter(savedScholarshipAdapter);

        allScholarshipList = new ArrayList<>();
        recentAdapter = new ScholarshipRVAdapter(getContext(), R.layout.scholarship_rv_item, allScholarshipList, allListener);
        binding.prioritySclpRv.setAdapter(recentAdapter);

        binding.refreshLayout.setOnRefreshListener(this::getData);

    }

    private void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.view_all_saved_jobs:
                startActivity(new Intent(getActivity(), SavedScholarshipActivity.class));
                break;
            case R.id.btnScholarships:
                startActivity(new Intent(getActivity(), ScholarshipListing.class).putExtra("no_filter",  ""));
        }
    }

    private void getData() {

        int index = (int) (Math.random() * (9));
        binding.quote.setText((getResources().getStringArray(R.array.diversity_quotes))[index]);
        binding.progressLayout.setVisibility(View.VISIBLE);
        binding.progressBar.setFrame(30);
        binding.prent.setVisibility(View.GONE);
        binding.prent.setAlpha(0);
        binding.errorView.setVisibility(View.GONE);

        getTags();
        if(!SharedPrefManager.getInstance(getActivity()).isEmployer()) {
            getLikedScholarships();
            binding.savedScholarshipsLayout.setVisibility(View.VISIBLE);
        } else{
            binding.savedScholarshipsLayout.setVisibility(View.GONE);
        }
        getScholarships();

    }

    private void getLikedScholarships() {
        savedList = new ArrayList<>();
        savedList.clear();
        binding.progress.setVisibility(View.VISIBLE);

        Call<LikedScholarshipResponse> userRequestCall = ApiClient.getUserService().getLikedScholarships(token);
        userRequestCall.enqueue(new Callback<LikedScholarshipResponse>() {
            @Override
            public void onResponse(Call<LikedScholarshipResponse> call, Response<LikedScholarshipResponse> response) {
                if(response.isSuccessful()) {
                    List<LikedScholarships> likedJobsResponse = response.body().getData();
                    for(LikedScholarships object: likedJobsResponse){
                        savedList.add(object.getScholarship());
                    }
                    if(savedList.isEmpty())
                        binding.savedScholarshipsLayout.setVisibility(View.GONE);
                    else {
                        binding.progress.setVisibility(View.GONE);
                        Collections.sort(savedList, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));
                        savedScholarshipAdapter.updateAdapter(savedList);
                    }
                } else {
                    Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LikedScholarshipResponse> call, Throwable t) {
                Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getTags() {
        tagList.clear();

        tagList.add(new TitleLinkObject("Female", FEMALE_URL));
        tagList.add(new TitleLinkObject("LGBTQI",LGBTQI_URL));
        tagList.add(new TitleLinkObject("Veteran",VETERAN_URL));
        tagList.add(new TitleLinkObject("Working Mother",WORKING_MOTHER_URL));
        tagList.add(new TitleLinkObject("Specially Abled",SPECIALLY_ABLED_URL));

        tagAdapter.updateAdapter(tagList);
    }



    private void getScholarships() {
        allScholarshipList.clear();

        ArrayList<JobFilterSearch> filterSearches = new ArrayList<>();

        JobFilterObject jobFilterObject = new JobFilterObject("posted_on", "desc", filterSearches, 0, 5);
        JobFilterBody jobFilterBody = new JobFilterBody(jobFilterObject);

        Call<FilterScholarshipsResponse> userRequestCall = ApiClient.getUserService().getScholarships(jobFilterBody, token);
        userRequestCall.enqueue(new Callback<FilterScholarshipsResponse>() {
            @Override
            public void onResponse(Call<FilterScholarshipsResponse> call, Response<FilterScholarshipsResponse> response) {
                if(response.isSuccessful()) {
                    allScholarshipList = response.body().getData().getScholarships();
                    recentAdapter.updateAdapter(allScholarshipList);

                    binding.prent.setVisibility(View.VISIBLE);
                    binding.prent.animate().alpha(1).setDuration(300);
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.progressLayout.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<FilterScholarshipsResponse> call, Throwable t) {
                binding.progressLayout.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
            }
        });
    }

    DashboardCircleTagsRVAdapter.onCompanyListener tagListener = new DashboardCircleTagsRVAdapter.onCompanyListener() {
        @Override
        public void onCompanyClick(int position, View v) {
            startActivity(new Intent(getActivity(), ScholarshipListing.class).putExtra("tags", tagList.get(position).getTitle()));
        }

        @Override
        public void onCompanyLongClick(int position) {

        }
    };


    ScholarshipRVAdapter.OnclickListener savedListener = new ScholarshipRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(getContext(), ScholarshipDescriptionActivity.class).putExtra("id", savedList.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };

    ScholarshipRVAdapter.OnclickListener allListener = new ScholarshipRVAdapter.OnclickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(getContext(), ScholarshipDescriptionActivity.class).putExtra("id", allScholarshipList.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };
}