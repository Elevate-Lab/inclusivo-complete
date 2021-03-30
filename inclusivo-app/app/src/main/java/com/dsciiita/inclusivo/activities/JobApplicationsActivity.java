package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dsciiita.inclusivo.adapters.JobApplicationsRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityJobApplicationsBinding;
import com.dsciiita.inclusivo.models.JobApplication;
import com.dsciiita.inclusivo.responses.ApplicationsByJobResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobApplicationsActivity extends AppCompatActivity implements JobApplicationsRVAdapter.onClickListener{


    ActivityJobApplicationsBinding binding;
    private int jobID;
    private ArrayList<JobApplication> applications;
    private JobApplicationsRVAdapter applicationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJobApplicationsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setToolBar();

        applications = new ArrayList<>();

        jobID = getIntent().getIntExtra("id", 0);
        getApplications();
        applicationAdapter = new JobApplicationsRVAdapter(this, applications, this);
        binding.viewApplicationsRv.setAdapter(applicationAdapter);

        binding.animationView.setOnClickListener(view-> binding.animationView.playAnimation());
    }


    private void setToolBar() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getApplications(){

        String token  = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<ApplicationsByJobResponse> userRequestCall = ApiClient.getUserService().getApplicationsForJob(jobID, token);
        userRequestCall.enqueue(new Callback<ApplicationsByJobResponse>() {
            @Override
            public void onResponse(Call<ApplicationsByJobResponse> call, Response<ApplicationsByJobResponse> response) {
                if(response.isSuccessful()) {
                    ApplicationsByJobResponse appli = response.body();
                    for(JobApplication application: appli.getData()){
                        applications.add(application);
                        applicationAdapter.notifyDataSetChanged();
                    }

                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.viewApplicationsRv.setVisibility(View.VISIBLE);
                    if(applications.isEmpty()) {
                        binding.emptyResult.setVisibility(View.VISIBLE);
                        binding.animationView.playAnimation();
                    } else
                        binding.emptyResult.setVisibility(View.GONE);
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApplicationsByJobResponse> call, Throwable t) {
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(int position, View v) {
        startActivity(new Intent(this, ViewJobApplicationActivity.class).putExtra("applicationId", applications.get(position).getId()));
    }

    @Override
    public void onLongClick(int position) {

    }
}