package com.dsciiita.inclusivo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.CompanyProfileViewPagerAdapter;
import com.dsciiita.inclusivo.animations.ViewAnimations;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityCompanyProfileBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.Initiative;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.JobFilterSearch;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.responses.CompanyInitiativesResponse;
import com.dsciiita.inclusivo.responses.CompanyJobsResponse;
import com.dsciiita.inclusivo.responses.CompanyResponse;
import com.dsciiita.inclusivo.responses.CompanyScholarshipResponse;
import com.dsciiita.inclusivo.responses.CompanyStoryResponse;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.dsciiita.inclusivo.viewmodel.CompanyViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    CompanyProfileActivity extends AppCompatActivity {

    private ActivityCompanyProfileBinding binding;
    private CompanyViewModel viewModel;
    private Company company;
    private int companyId;
    private boolean isFollowing, isRotate = false;
    private Intent resultIntent;
    private ArrayList<Job> jobsList;
    private ArrayList<Initiative> initiativeList;
    private ArrayList<Story> storyList;
    private ArrayList<Scholarship> scholarshipList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        ViewAnimations.init(binding.fabAddJob);
        ViewAnimations.init(binding.fabAddStory);
        ViewAnimations.init(binding.fabAddScholarship);
        ViewAnimations.init(binding.fabAddInitiative);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view->finish());

        resultIntent = new Intent();

        Intent intent = getIntent();
        if(intent.hasExtra("companyID"))
            companyId = intent.getIntExtra("companyID", 0);


        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);

        binding.btnSubs.setOnClickListener(this::onClick);
        binding.btnUnsubs.setOnClickListener(this::onClick);

        binding.addFab.setOnClickListener(view->{
            isRotate = ViewAnimations.rotateFab(view, !isRotate);
            if(isRotate){
                ViewAnimations.showIn(binding.fabAddJob);
                ViewAnimations.showIn(binding.fabAddScholarship);
                ViewAnimations.showIn(binding.fabAddInitiative);
                ViewAnimations.showIn(binding.fabAddStory);
            }else{
                ViewAnimations.showOut(binding.fabAddJob);
                ViewAnimations.showOut(binding.fabAddScholarship);
                ViewAnimations.showOut(binding.fabAddInitiative);
                ViewAnimations.showOut(binding.fabAddStory);
            }
        });

        binding.fabAddJob.setOnClickListener(v -> {
            binding.addFab.callOnClick();
            startActivityForResult(new Intent(getApplicationContext(), AddEditJobActivity.class), 1);
        });

        binding.fabAddStory.setOnClickListener(v -> {
            binding.addFab.callOnClick();
            startActivityForResult(new Intent(getApplicationContext(), AddStoryActivity.class), 1);
        });

        binding.fabAddScholarship.setOnClickListener(v -> {
            binding.addFab.callOnClick();
            startActivityForResult(new Intent(getApplicationContext(), AddScholarshipActivity.class), 1);
        });

        binding.fabAddInitiative.setOnClickListener(v -> {
            binding.addFab.callOnClick();
            startActivityForResult(new Intent(getApplicationContext(), AddInitiativeActivity.class), 1);
        });

        binding.shareStory.setOnClickListener(view->shareCompany(companyId));

        getData();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1 :
                if (resultCode == Activity.RESULT_OK) {
                    getData();
                }
        }
    }

    public void getData() {
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.parentLayout.setAlpha(0);
        getJobs();
        getInitiatives();
        getStories();
        getCompany();
        getScholarships();
    }



    private void getCompany() {
        binding.errorView.setVisibility(View.GONE);
        String token = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<CompanyResponse> userRequestCall = ApiClient.getUserService().getCompany(companyId, token);
        userRequestCall.enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                if (response.isSuccessful()) {
                    CompanyResponse companyResponse = response.body();
                    company = companyResponse.getData().getCompany();
                    isFollowing = companyResponse.getData().isFollowing();
                    viewModel.setText(company);
                    setValues();
                } else {
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.shimmerViewContainer.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                binding.errorView.setVisibility(View.VISIBLE);
            }
        });
    }



    private void getJobs() {
        jobsList = new ArrayList<>();

        String token  = "token "+ SharedPrefManager.getInstance(CompanyProfileActivity.this).getToken();
        Call<CompanyJobsResponse> userRequestCall = ApiClient.getUserService().getJobsForCompany(companyId, token);
        userRequestCall.enqueue(new Callback<CompanyJobsResponse>() {
            @Override
            public void onResponse(Call<CompanyJobsResponse> call, Response<CompanyJobsResponse> response) {
                if(response.isSuccessful()) {
                    CompanyJobsResponse jobsResponse = response.body();
                    viewModel.setJobList(jobsResponse.getData());
                    jobsList.addAll(jobsResponse.getData());
                    Collections.sort(jobsList, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));

                }
            }
            @Override
            public void onFailure(Call<CompanyJobsResponse> call, Throwable t) {
            }
        });
    }


    private void getInitiatives() {
        initiativeList = new ArrayList<>();

        String token  = "token "+ SharedPrefManager.getInstance(CompanyProfileActivity.this).getToken();
        Call<CompanyInitiativesResponse> userRequestCall = ApiClient.getUserService().getInitiativeByCompany(companyId, token);
        userRequestCall.enqueue(new Callback<CompanyInitiativesResponse>() {
            @Override
            public void onResponse(Call<CompanyInitiativesResponse> call, Response<CompanyInitiativesResponse> response) {
                if(response.isSuccessful()) {
                    CompanyInitiativesResponse initiativesResponse = response.body();
                    viewModel.setInitiatives(initiativesResponse.getData());
                    initiativeList.addAll(initiativesResponse.getData());
                }
            }
            @Override
            public void onFailure(Call<CompanyInitiativesResponse> call, Throwable t) {
            }
        });
    }


    private void getStories() {
        storyList = new ArrayList<>();

        String token  = "token "+ SharedPrefManager.getInstance(CompanyProfileActivity.this).getToken();

        Call<CompanyStoryResponse> userRequestCall = ApiClient.getUserService().getStoryByCompany(companyId, token);
        userRequestCall.enqueue(new Callback<CompanyStoryResponse>() {
            @Override
            public void onResponse(Call<CompanyStoryResponse> call, Response<CompanyStoryResponse> response) {
                if(response.isSuccessful()) {
                    CompanyStoryResponse storyResponse = response.body();
                    viewModel.setStories(storyResponse.getData());
                    storyList.addAll(storyResponse.getData());
                }
            }

            @Override
            public void onFailure(Call<CompanyStoryResponse> call, Throwable t) {
            }
        });
    }

    private void getScholarships() {
        scholarshipList = new ArrayList<>();

        String token = "token " + SharedPrefManager.getInstance(CompanyProfileActivity.this).getToken();
        Call<CompanyScholarshipResponse> userRequestCall = ApiClient.getUserService().getScholarshipsByCompany(companyId, token);
        userRequestCall.enqueue(new Callback<CompanyScholarshipResponse>() {
            @Override
            public void onResponse(Call<CompanyScholarshipResponse> call, Response<CompanyScholarshipResponse> response) {
                if (response.isSuccessful()) {
                    CompanyScholarshipResponse scholarshipResponse = response.body();
                    viewModel.setScholarships(scholarshipResponse.getData());
                    scholarshipList.addAll(scholarshipResponse.getData());
                    Collections.sort(scholarshipList, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));
                    Log.d("ScholarshipByCompany: ", "" + scholarshipResponse.getData());
                }
            }

            @Override
            public void onFailure(Call<CompanyScholarshipResponse> call, Throwable t) {
            }
        });
    }


    private void setValues() {
        binding.companyTitleTxt.setText(company.getName());
        binding.companyProfileTxt.setText(company.getProfile());

        Glide.with(getApplicationContext()).load(company.getLogoUrl())
                .placeholder(Constants.PLACEHOLDER_IMAGE)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(binding.companyLogoImg);

        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.parentLayout.animate().alpha(1).setDuration(300);

        if(!SharedPrefManager.getInstance(this).isEmployer()){
            binding.buttonLayout.setVisibility(View.VISIBLE);
            binding.fabLayout.setVisibility(View.GONE);
            if(isFollowing) {
                binding.btnSubs.setVisibility(View.GONE);
                binding.btnUnsubs.setVisibility(View.VISIBLE);
            } else {
                binding.btnSubs.setVisibility(View.VISIBLE);
                binding.btnUnsubs.setVisibility(View.GONE);
            }
        } else {
            if(company.getId()==SharedPrefManager.getInstance(this).getCompanyID()){
                binding.fabLayout.setVisibility(View.VISIBLE);
            }
            binding.buttonLayout.setVisibility(View.GONE);
        }
        setUpViewPager();
    }


    private void setUpViewPager() {
        String[] tabTexts = {"Overview", "About", "Initiatives", "Jobs", "Stories", "Scholarships"};
        int[] tabIcons = {0, 0, R.drawable.ic_initiative, R.drawable.ic_jobs, R.drawable.ic_stories, R.drawable.ic_scholarship};
        CompanyProfileViewPagerAdapter companyProfileViewPagerAdapter = new CompanyProfileViewPagerAdapter(this);
        binding.companyVp2.setAdapter(companyProfileViewPagerAdapter);

        new TabLayoutMediator(binding.companyTbl, binding.companyVp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTexts[position]);
                if(position>1)
                    tab.setIcon(tabIcons[position]);
            }
        }).attach();
    }


    public void goto_fragment(int fragment) {
        binding.companyVp2.setCurrentItem(fragment);
    }


    private void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnSubs:
                Snackbar snackbar = Snackbar.make(binding.parentLayout, "Please wait...", BaseTransientBottomBar.LENGTH_INDEFINITE);
                snackbar.show();
                subscribeCompany(snackbar);
                break;
            case R.id.btnUnsubs:
                Snackbar snackbar2 = Snackbar.make(binding.parentLayout, "Please wait...", BaseTransientBottomBar.LENGTH_INDEFINITE);
                snackbar2.show();
                unsubscribeCompany(snackbar2);
                break;
        }
        setResult(RESULT_OK, resultIntent);
    }


    private void subscribeCompany(Snackbar snackbar) {

        String token  = "token "+ SharedPrefManager.getInstance(CompanyProfileActivity.this).getToken();

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().followCompany(companyId, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.isSuccessful()) {
                    binding.btnSubs.setVisibility(View.GONE);
                    binding.btnUnsubs.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                snackbar.dismiss();
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                snackbar.dismiss();
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void unsubscribeCompany(Snackbar snackbar) {

        String token  = "token "+ SharedPrefManager.getInstance(CompanyProfileActivity.this).getToken();

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().unfollowCompany(companyId, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    binding.btnSubs.setVisibility(View.VISIBLE);
                    binding.btnUnsubs.setVisibility(View.GONE);
                    snackbar.dismiss();
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                snackbar.dismiss();
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void shareCompany(int id) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this company on Inclusivo https://inclusivo.netlify.app/home/company/"+id);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share company via");
        startActivity(shareIntent);
    }



}