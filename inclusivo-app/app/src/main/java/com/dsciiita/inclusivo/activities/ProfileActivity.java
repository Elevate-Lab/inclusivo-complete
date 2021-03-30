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
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.GetUserResponse;
import com.dsciiita.inclusivo.responses.UserTypeResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

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
                        UserCandidate candidate = user.getCandidate();
                        setCandidate(candidate);
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


    private void setCandidate(UserCandidate candidate) {
        if(candidate.getUser() != null) {
            binding.firstNameTxt.setText(candidate.getUser().getFirstName());
            binding.lastNameTxt.setText(candidate.getUser().getLastName());
            binding.emailTxt.setText(candidate.getUser().getEmail());
            binding.dobTxt.setText(candidate.getUser().getDob());
            binding.genderTxt.setText(candidate.getUser().getGender());
        }

        binding.profileDescriptionTxt.setText(candidate.getProfileDesc());
        binding.candidateJobRoleTxt.setText(candidate.getJobRole());
        binding.contactNumTxt.setText(candidate.getMobile());
        binding.alternateContactNumTxt.setText(candidate.getAlternateMobile());
        binding.nationalityTxt.setText(candidate.getNationality());

        if(candidate.getCountry()!=null)
            binding.countryTxt.setText(candidate.getCountry().getName());
        if(candidate.getState()!=null)
            binding.stateTxt.setText(candidate.getState().getName());
        if(candidate.getCity()!=null)
            binding.cityTxt.setText(candidate.getCity().getName());

        binding.YearTxt.setText(candidate.getYear());
        binding.MonthTxt.setText(candidate.getMonth());

        binding.cbIsRelocation.setEnabled(true);
        binding.cbIsRelocation.setChecked(candidate.getIsRelocation());
        binding.cbIsRelocation.setEnabled(false);


        setTags(candidate);
        setLocations(candidate);

        binding.resumeLinkTxt.setOnClickListener(view-> startActivity(new Intent(this, PDFViewerActivity.class)
                .putExtra("resume_link", candidate.getResumeLink())));

        if (candidate.getLinkedIn() != null)
            addLink(binding.linkedIn, candidate.getLinkedIn());

        if (candidate.getTwitter() != null)
            addLink(binding.twitter, candidate.getTwitter());

        if (candidate.getGithub() != null)
            addLink(binding.github, candidate.getGithub());

        CircleImageView imageView = findViewById(R.id.profile_img);
        if(imageView!=null)
            Glide.with(getApplicationContext()).load(candidate.getUser().getProfileUrl())
                    .placeholder(R.drawable.profile_default)
                    .into(imageView);

    }

    private void setTags(UserCandidate candidate){
        List<Diversity> tags = candidate.getDiversity();
        for(Diversity tag: tags){
            String text = tag.getName();
            Chip chip = (Chip) LayoutInflater.from(ProfileActivity.this)
                    .inflate(R.layout.tag_chip_layout,
                            binding.TagChipGroup, false);
            chip.setText(text);
            chip.setId(ViewCompat.generateViewId());
            chip.setCloseIconVisible(false);
            binding.TagChipGroup.addView(chip);
        }
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

    private void setLocations(UserCandidate candidate){
        ArrayList<CityResponse> cities = candidate.getPrefCities();
        for(CityResponse cityResponse: cities){
            String text = cityResponse.getName() + ", " + cityResponse.getState().getName() + ", " + cityResponse.getState().getCounty().getName();
            addLocationChip(text);
        }
    }

    private void addLocationChip(String selected){
        Chip chip = (Chip) LayoutInflater.from(ProfileActivity.this)
                .inflate(R.layout.tag_chip_layout,
                        binding.prefCitiesChipGrp, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        chip.setCloseIconVisible(false);
        binding.prefCitiesChipGrp.addView(chip);
    }

}