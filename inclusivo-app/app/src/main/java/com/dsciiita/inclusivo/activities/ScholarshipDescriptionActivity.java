package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.ScholarshipDescriptionViewPagerAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityScholarshipDescriptionBinding;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.responses.ScholarshipByIDResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.dsciiita.inclusivo.viewmodel.ScholarshipViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScholarshipDescriptionActivity extends AppCompatActivity {
    private ActivityScholarshipDescriptionBinding binding;
    private int scholarshipId;
    private Scholarship scholarship;
    private String token;
    private ScholarshipViewModel scholarshipViewModel;
    private boolean isLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScholarshipDescriptionBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        token = "token " + SharedPrefManager.getInstance(this).getToken();
        scholarshipId = getIntent().getIntExtra("id", 0);
        scholarshipViewModel = new ViewModelProvider(this).get(ScholarshipViewModel.class);
        binding.saveImg.setOnClickListener(this::onClick);
        binding.shareImg.setOnClickListener(this::onClick);
        binding.btnApply.setOnClickListener(this::onClick);
        getScholarship();
    }

    private void onClick(View view) {
        int id = view.getId();
        if (id == R.id.save_img) {
            if (isLiked) {
                unlikeScholarship();
            } else
                likeScholarship();
            binding.saveProgress.setVisibility(View.VISIBLE);
        } else if (id == R.id.share_img)
            shareScholarship(scholarshipId);
        else if (id == R.id.btnApply)
            applyForScholarship();
    }

    private void applyForScholarship() {
        try {
            Uri applyWebPage = Uri.parse(scholarship.getApplyUrl());
            Intent intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
            intent.setData(applyWebPage);
            startActivity(intent);
        }catch (Exception e) {
            Snackbar.make(binding.parentLayout, "Something went wrong", BaseTransientBottomBar.LENGTH_SHORT);
        }
    }


    private void likeScholarship() {
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().likeScholarship(scholarshipId, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    isLiked = true;
                    binding.saveImg.setBackgroundResource(R.drawable.ic_save_red_filled);
                    Snackbar.make(binding.parentLayout, "Added to saved scholarships", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.saveProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                binding.saveProgress.setVisibility(View.GONE);
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void unlikeScholarship() {
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().unlikeScholarship(scholarshipId, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                isLiked = false;
                binding.saveProgress.setVisibility(View.GONE);
                binding.saveImg.setBackgroundResource(R.drawable.ic_save_red);
                Snackbar.make(binding.parentLayout, "Removed from saved scholarships", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.i("ERROR FAILURE", t.getMessage());
                binding.saveProgress.setVisibility(View.GONE);
            }
        });
    }

    private void shareScholarship(int id) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this scholarship on Inclusivo https://inclusivo.netlify.app/home/scholarship/"+id);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share scholarship via");
        startActivity(shareIntent);
    }


    private void getScholarship() {
        Call<ScholarshipByIDResponse> userRequestCall = ApiClient.getUserService().getScholarshipById(scholarshipId, token);
        userRequestCall.enqueue(new Callback<ScholarshipByIDResponse>() {
            @Override
            public void onResponse(Call<ScholarshipByIDResponse> call, Response<ScholarshipByIDResponse> response) {
                if (response.isSuccessful()) {
                    ScholarshipByIDResponse scholarshipByIDResponse = response.body();
                    scholarship = scholarshipByIDResponse.getData();
                    scholarshipViewModel.setScholarship(scholarship);
                    isLiked = scholarship.getIsLiked();
                    setValues();
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScholarshipByIDResponse> call, Throwable t) {
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues() {
        binding.scholarshipTitleTxt.setText(scholarship.getTitle());

        if(scholarship.getCompany()!=null) {
            binding.companyNameTxt.setText(scholarship.getCompany().getName());

            Glide.with(getApplicationContext()).load(scholarship.getCompany().getLogoUrl())
                    .timeout(10000)
                    .placeholder(Constants.PLACEHOLDER_IMAGE)
                    .into(binding.companyLogoImg);
        }

        if (SharedPrefManager.getInstance(this).isEmployer())
            binding.saveImg.setVisibility(View.GONE);
        else {
            binding.btnApply.setVisibility(View.VISIBLE);
            if (isLiked)
                binding.saveImg.setBackgroundResource(R.drawable.ic_save_red_filled);
            else
                binding.saveImg.setBackgroundResource(R.drawable.ic_save_red);
        }

        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.parentLayout.animate().alpha(1).setDuration(300);


        try {
            String diff = computeDifference();
            binding.scholarshipPostedTimeTxt.setText(diff);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUpViewPager();
    }

    private String computeDifference() throws ParseException {
        String dateString = scholarship.getPostedOn().split("T")[0];
        String timeString = scholarship.getPostedOn().split("T")[1].split("\\.")[0];
        String formattedDT = dateString + " " + timeString;

        SimpleDateFormat formatGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat formatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formatLocal.setTimeZone(TimeZone.getDefault());

        Date postedGMTDate = formatGMT.parse(formattedDT);

        String localDate = formatLocal.format(postedGMTDate);
        Date postedDate = formatLocal.parse(localDate);

        Date date = new Date();

        long diff = date.getTime() - postedDate.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
        int weeks = (int) difference_In_Days / 7;
        if (weeks == 0) {
            if (difference_In_Days == 0) {
                if (diffHours == 0) {
                    if (diffMinutes == 1)
                        return "a min ago";
                    return diffMinutes + " mins ago";
                }
                if (diffHours == 1)
                    return "an hour ago";
                return diffHours + " hours ago";
            }
            if (difference_In_Days == 1)
                return "a day ago";
            return difference_In_Days + " days ago";
        }
        if (weeks == 1)
            return "a week ago";
        return weeks + " weeks ago";
    }

    private void setUpViewPager() {
        binding.scholarshipDescriptionVp2.setAdapter(new ScholarshipDescriptionViewPagerAdapter(this));
        String[] tabTextsArray = {"Overview", "About", "Selection", "More Info"};
        new TabLayoutMediator(binding.scholarshipDescriptionTbl, binding.scholarshipDescriptionVp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTextsArray[position]);
            }
        }).attach();
    }

    public void gotoFragment(int fragment) {
        binding.scholarshipDescriptionVp2.setCurrentItem(fragment);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}