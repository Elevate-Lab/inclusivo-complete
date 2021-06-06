package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityProfileBinding;
import com.dsciiita.inclusivo.databinding.ActivityProfileEmployerBinding;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.User;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.models.UserEmployee;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.GetUserResponse;
import com.dsciiita.inclusivo.responses.UserTypeResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployerProfileActivity extends AppCompatActivity {

    ActivityProfileEmployerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEmployerBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view->finish());

        binding.progressBar.setVisibility(View.VISIBLE);
        getData();

        binding.refreshLayout.setOnRefreshListener(this::getData);

    }

    private void getData() {
        binding.errorView.setVisibility(View.GONE);

        getUser();
    }

    private void getUser(){

        String token  = "token "+  SharedPrefManager.getInstance(this).getToken();

        Call<GetUserResponse> userRequestCall = ApiClient.getUserService().getUser(token);
        userRequestCall.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if(response.isSuccessful()) {
                    UserTypeResponse user = response.body().getData();
                    UserEmployee employer = user.getEmployee();
                    setValues(employer);

                    binding.parent.setVisibility(View.VISIBLE);
                } else{
                    binding.errorView.setVisibility(View.VISIBLE);
                    binding.errorAnim.playAnimation();
                }
                binding.progressBar.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorAnim.playAnimation();
                binding.refreshLayout.setRefreshing(false);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void setValues(UserEmployee employee) {
        if(employee.getUser() != null) {
            User user = employee.getUser();
            binding.firstNameTxt.setText(user.getFirstName());
            binding.lastNameTxt.setText(user.getLastName());
            binding.emailTxt.setText(user.getEmail());
            binding.dobTxt.setText(user.getDob());
            binding.genderTxt.setText(user.getGender());

            CircleImageView imageView = findViewById(R.id.profile_img);
            if(imageView!=null)
                Glide.with(getApplicationContext()).load(user.getProfileUrl())
                        .placeholder(Constants.PLACEHOLDER_IMAGE)
                        .into(imageView);
        }

        binding.contactNumTxt.setText(employee.getMobile());
        binding.alternateContactNumTxt.setText(employee.getAlternateMobile());

    }

    public void openLink(View view){
        String url = (String) view.getTag();
        if(!url.isEmpty()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    private void addLink(View view, String url){
        view.setVisibility(View.VISIBLE);
        if(url.isEmpty()) {
            view.setVisibility(View.GONE);
            return;
        }
        if(!url.startsWith("https://"))
            url = "https://"+url;
        view.setTag(url);
        view.setOnClickListener(this::openLink);
    }

}