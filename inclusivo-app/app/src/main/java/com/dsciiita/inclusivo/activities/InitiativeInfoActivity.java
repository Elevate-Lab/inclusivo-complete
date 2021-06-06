package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityInitiativeInfoBinding;
import com.dsciiita.inclusivo.models.Initiative;
import com.dsciiita.inclusivo.responses.InitiativeByIdResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitiativeInfoActivity extends AppCompatActivity {

    ActivityInitiativeInfoBinding binding;
    Initiative initiative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitiativeInfoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setToolBar();

        int id = getIntent().getIntExtra("id", 0);
        getInitiative(id);


    }

    private void deleteInitiative(int id) {
        binding.btnDelete.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        String token = "token "+SharedPrefManager.getInstance(this).getToken();

        Call<Void> userRequestCall = ApiClient.getUserService().deleteInitiative(id, token);
        userRequestCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                    setResult(RESULT_OK, new Intent());
                binding.btnDelete.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(InitiativeInfoActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.btnDelete.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(InitiativeInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolBar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view-> finish());
    }

    private void getInitiative(int id) {

        binding.parent.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        String token  = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<InitiativeByIdResponse> userRequestCall = ApiClient.getUserService().getInitiativeByID(id, token);
        userRequestCall.enqueue(new Callback<InitiativeByIdResponse>() {
            @Override
            public void onResponse(Call<InitiativeByIdResponse> call, Response<InitiativeByIdResponse> response) {
                if(response.isSuccessful()) {
                    InitiativeByIdResponse initiativesResponse = response.body();
                    initiative = initiativesResponse.getData();
                    binding.initiativeDescription.setText(initiative.getDescription());
                    binding.initiativeTitle.setText(initiative.getName());
                    checkEmployer(initiative.getCompanyID(), initiative.getId());
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<InitiativeByIdResponse> call, Throwable t) {
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmployer(int companyId, int initiativeId) {
        if(SharedPrefManager.getInstance(this).isEmployer()) {
            if(SharedPrefManager.getInstance(this).getCompanyID()==companyId) {
                binding.btnDelete.setVisibility(View.VISIBLE);
                binding.btnDelete.setOnClickListener(view -> deleteInitiative(initiativeId));
            } else
                binding.btnDelete.setVisibility(View.GONE);
        } else
            binding.btnDelete.setVisibility(View.GONE);

        binding.parent.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
}