package com.dsciiita.inclusivo.activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.JobDescriptionViewPagerAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityJobDescriptionBinding;
import com.dsciiita.inclusivo.models.EvaluationData;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.responses.EvaluationResponse;
import com.dsciiita.inclusivo.responses.JobByIdResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.dsciiita.inclusivo.viewmodel.JobViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

@SuppressLint("ClickableViewAccessibility")
public class JobDescriptionActivity extends AppCompatActivity {
    private ActivityJobDescriptionBinding binding;

    private int jobId;
    private String token, status, updatedStatus;
    private String applicationStatus;
    private Job job;
    private boolean isApplied = false, isLiked;
    private Intent resultIntent;
    private JobViewModel viewModel;
    private BottomSheetDialog bottomSheetDialog, helpDialog;
    private MaterialButton understoodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobDescriptionBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view-> finish());

        token  = "token "+ SharedPrefManager.getInstance(this).getToken();
        jobId = getIntent().getIntExtra("id", 0);

        helpDialog = new BottomSheetDialog(JobDescriptionActivity.this);
        helpDialog.setContentView(R.layout.status_help_bottomsheet);
        understoodBtn = helpDialog.findViewById(R.id.understood);

        binding.btnApply.setOnClickListener(this::onClick);
        binding.btnEdit.setOnClickListener(this::onClick);
        binding.saveImg.setOnClickListener(this::onClick);
        binding.shareImg.setOnClickListener(this::onClick);
        binding.btnViewApplication.setOnClickListener(this::onClick);
        binding.btnEvaluate.setOnClickListener(this::onClick);
        viewModel = new ViewModelProvider(this).get(JobViewModel.class);

        resultIntent = new Intent();

        getJob();


    }


    private void startShowCaseTourCandidate() {
        String SHOWCASE_ID = "JOB_ACTIVITY_CANDIDATE";
        ShowcaseConfig config = new ShowcaseConfig();
        config.setFadeDuration(500);
        config.setDelay(200);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);

        sequence.addSequenceItem(binding.btnEvaluate,
                "Match your skills with job requirements", "GOT IT");
        sequence.start();
    }

    private void startShowCaseTourEmployer() {
        String SHOWCASE_ID = "JOB_ACTIVITY_EMPLOYER";
        ShowcaseConfig config = new ShowcaseConfig();
        config.setFadeDuration(500);
        config.setDelay(200);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);
        sequence.addSequenceItem(binding.btnViewApplication,
                "Access applications for this job", "GOT IT");
        sequence.addSequenceItem(binding.btnEdit,
                "Update job status", "GOT IT");
        sequence.start();
    }


    private void getJob() {
        Call<JobByIdResponse> userRequestCall = ApiClient.getUserService().getJobByID(jobId, token);
        userRequestCall.enqueue(new Callback<JobByIdResponse>() {
            @Override
            public void onResponse(Call<JobByIdResponse> call, Response<JobByIdResponse> response) {
                if(response.isSuccessful()) {
                    JobByIdResponse jobsResponse = response.body();
                    job = jobsResponse.getData().getJob();
                    applicationStatus = jobsResponse.getData().getApplicationStatus();
                    viewModel.setJob(job);
                    isApplied = jobsResponse.getData().isApplied();
                    isLiked = jobsResponse.getData().getJob().isLiked();
                    setValues();
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JobByIdResponse> call, Throwable t) {
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    private void createApplication(LottieAnimationView view, RelativeLayout progressBar, String message) {
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().addApplicationForJob(jobId, jobId, "Pending", token,
                message);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.isSuccessful()) {
                    binding.btnApply.setText("Applied");
                    binding.btnApply.setEnabled(false);
                    view.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                bottomSheetDialog.dismiss();
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private void showStatusInfo() {
        helpDialog.show();
        understoodBtn.setOnClickListener(view->{
            helpDialog.cancel();
        });
    }

    private void getConfirmation() {
        bottomSheetDialog = new BottomSheetDialog(this);
        if(job.getIsApplyHere())
            bottomSheetDialog.setContentView(R.layout.apply_job_bottomsheet);
        else
            bottomSheetDialog.setContentView(R.layout.apply_external_bottomsheet);
        MaterialButton applyBtn = bottomSheetDialog.findViewById(R.id.apply);
        MaterialButton cancelBtn = bottomSheetDialog.findViewById(R.id.cancel);

        TextInputLayout msg = bottomSheetDialog.findViewById(R.id.profile_description_til);


        LottieAnimationView animationView = bottomSheetDialog.findViewById(R.id.animation_view);
        RelativeLayout progressBar = bottomSheetDialog.findViewById(R.id.progress_bar);

        applyBtn.setOnClickListener(view->{
            if(job.getIsApplyHere()) {
                String message = msg.getEditText().getText().toString().trim();
                if(message.isEmpty()) {
                    Toast.makeText(this, "Answer the questions", Toast.LENGTH_SHORT).show();
                    return;
                }
                createApplication(animationView, progressBar, message);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(job.getApplyUrl()));
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bottomSheetDialog.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        cancelBtn.setOnClickListener(view->bottomSheetDialog.dismiss());
    }


    private void getStatusConfirmation() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.update_application_bottomsheet);
        MaterialButton applyBtn = bottomSheetDialog.findViewById(R.id.apply);
        applyBtn.setVisibility(View.GONE);
        MaterialButton cancelBtn = bottomSheetDialog.findViewById(R.id.cancel);
        ListView listView = bottomSheetDialog.findViewById(R.id.list_item);

        ArrayList<String> list = new ArrayList<>();

        TextView msg = bottomSheetDialog.findViewById(R.id.msg);
        msg.setText("Current status: "+status);

        TextView heading = bottomSheetDialog.findViewById(R.id.heading);
        heading.setText("Update job status");

        switch (status) {
            case "Draft":
                list.add("Published");
                list.add("Disabled");
                list.add("Hired");
                list.add("Expired");
                break;
            case "Published":
                list.add("Disabled");
                list.add("Hired");
                list.add("Expired");
                break;
            case "Disabled":
                list.add("Published");
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            updatedStatus = adapter.getItem(position);
            applyBtn.setVisibility(View.VISIBLE);
        });


        applyBtn.setOnClickListener(view->{
            bottomSheetDialog.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            updateJobStatus(bottomSheetDialog, bottomSheetDialog.findViewById(R.id.progress_bar));
        });

        bottomSheetDialog.show();

        cancelBtn.setOnClickListener(view->bottomSheetDialog.dismiss());
    }

    private void updateJobStatus(BottomSheetDialog dialog, View progressBar) {
        String token = "token "+SharedPrefManager.getInstance(this).getToken();
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().updateJobStatus(jobId, updatedStatus, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                status = updatedStatus;
                if(status.equals("Hired") || status.equals("Expired"))
                    binding.btnEdit.setVisibility(View.GONE);
                Toast.makeText(JobDescriptionActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                Toast.makeText(JobDescriptionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void likeJob() {
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().likeJob(jobId, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.isSuccessful()) {
                    isLiked = true;
                    binding.saveImg.setSpeed(1);
                    binding.saveImg.playAnimation();
                    Snackbar.make(binding.parentLayout, "Added to saved jobs", Snackbar.LENGTH_SHORT).show();
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


    private void unlikeJob() {
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().unlikeJob(jobId, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                isLiked = false;
                binding.saveImg.setSpeed(-2);
                binding.saveImg.playAnimation();
                binding.saveProgress.setVisibility(View.GONE);
                Snackbar.make(binding.parentLayout, "Removed from saved jobs", Snackbar.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.i("ERROR FAILURE", t.getMessage());
                binding.saveProgress.setVisibility(View.GONE);
            }
        });
    }

    private void setValues() {
        binding.jobTitleTxt.setText(job.getTitle());
        binding.jobTypeTxt.setText(job.getJobType());
        binding.companyNameTxt.setText(job.getCompany().getName());

        Glide.with(getApplicationContext()).load(job.getCompany().getLogoUrl())
                .timeout(10000)
                .placeholder(Constants.PLACEHOLDER_IMAGE)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(binding.companyLogoImg);

        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.parentLayout.animate().alpha(1).setDuration(300);

        if(SharedPrefManager.getInstance(this).isEmployer()){
            setViewsGone(binding.btnApply, binding.btnEvaluate, binding.saveImg);
            status = job.getStatus();

            if(SharedPrefManager.getInstance(this).getCompanyID()==job.getCompany().getId()) {
                setViewsVisible(binding.btnEdit, binding.btnViewApplication);
                startShowCaseTourEmployer();
            }

            if(status.equals("Hired") || status.equals("Expired"))
                binding.btnEdit.setVisibility(View.GONE);
        } else {
            if(isLiked)
                binding.saveImg.setFrame(50);
            else
                binding.saveImg.setFrame(0);

            if(job.getStatus().equals("Expired")){
                setViewsVisible(binding.applicationStatus);
                setViewsGone(binding.btnApply, binding.saveImg);
                Chip chip = (Chip) LayoutInflater.from(JobDescriptionActivity.this)
                        .inflate(R.layout.job_status_chip_layout, binding.applicationStatus, false);
                chip.setId(ViewCompat.generateViewId());
                chip.setText("This job is expired");
                binding.applicationStatus.addView(chip);
            } else if(job.getStatus().equals("Hired")) {
                setViewsVisible(binding.applicationStatus);
                setViewsGone(binding.btnApply, binding.saveImg);
                Chip chip = (Chip) LayoutInflater.from(JobDescriptionActivity.this)
                        .inflate(R.layout.job_status_chip_layout, binding.applicationStatus, false);
                chip.setId(ViewCompat.generateViewId());
                chip.setText("Hiring is closed for this job");
                binding.applicationStatus.addView(chip);
            }
            else if(isApplied){
                setViewsVisible(binding.applicationStatus);
                setViewsGone(binding.btnApply);
                int layout = R.layout.pending_application_chip_layout;
                switch (applicationStatus) {
                    case "Process":
                        layout = R.layout.process_application_chip_layout;
                        break;
                    case "Shortlisted":
                        layout = R.layout.shortlisted_application_chip_layout;
                        break;
                    case "Rejected":
                        layout = R.layout.rejected_application_chip_layout;
                        break;
                    case "Selected":
                        layout = R.layout.selected_application_chip_layout;
                        break;
                }
                Chip chip = (Chip) LayoutInflater.from(JobDescriptionActivity.this)
                        .inflate(layout, binding.applicationStatus, false);
                chip.setId(ViewCompat.generateViewId());
                chip.setCloseIconVisible(false);
                chip.setOnTouchListener((v, event) -> {
                    if (v==chip) {
                        if (event.getX() <= v.getPaddingLeft()) {
                            showStatusInfo();
                        }
                        return true;
                    }
                    return false;
                });
                binding.applicationStatus.addView(chip);
            } else {
                setViewsVisible(binding.btnApply, binding.btnEvaluate);
                startShowCaseTourCandidate();
            }
        }

        if(!job.getIsApplyHere()){
            binding.btnApply.setIconResource(R.drawable.ic_round_open_in_new_24);
            binding.btnApply.setIconPadding(10);
        }

        try {
            String diff = computeDifference();
            binding.jobPostedTimeTxt.setText(diff);
        }catch (Exception e){
            e.printStackTrace();
        }



        setUpViewPager();
    }

    private void setViewsVisible(View... views){
        for(View view: views){
            view.setVisibility(View.VISIBLE);
        }
    }

    private void setViewsGone(View... views){
        for(View view: views){
            view.setVisibility(View.GONE);
        }
    }

    private String computeDifference() throws ParseException {
        String dateString = job.getPostedOn().split("T")[0];
        String timeString = job.getPostedOn().split("T")[1].split("\\.")[0];
        String formattedDT = dateString+" "+timeString;

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
        int weeks = (int) difference_In_Days/7;
        if(weeks==0) {
            if (difference_In_Days == 0) {
                if (diffHours == 0) {
                    if(diffMinutes==1)
                        return "a min ago";
                    return diffMinutes + " mins ago";
                }
                if(diffHours==1)
                    return "an hour ago";
                return diffHours + " hours ago";
            }
            if(difference_In_Days==1)
                return "a day ago";
            return difference_In_Days + " days ago";
        }
        if(weeks==1)
            return "a week ago";
        return weeks+" weeks ago";
    }

    private void setUpViewPager() {
        binding.jobDescriptionVp2.setAdapter(new JobDescriptionViewPagerAdapter(this));
        String[] tabTextsArray = {"Overview", "About", "Recruitment", "More Info"};
        new TabLayoutMediator(binding.jobDescriptionTbl, binding.jobDescriptionVp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                tab.setText(tabTextsArray[position]);
            }
        }).attach();
    }


    private void onClick(View view) {
        int id = view.getId();
        if(id== R.id.btnEdit)
            getStatusConfirmation();
        else if(id==R.id.btnViewApplication)
            startActivity(new Intent(this, JobApplicationsActivity.class).putExtra("id", jobId));
        else if(id==R.id.btnApply)
            getConfirmation();
        else if(id==R.id.btnEvaluate)
            getEvaluationConfirmation();
        else if(id==R.id.save_img) {
            if (isLiked) {
                unlikeJob();
            } else
                likeJob();

            setResult(RESULT_OK, resultIntent);
            binding.saveProgress.setVisibility(View.VISIBLE);
        } else if(id==R.id.share_img)
            shareJob();
    }

    private void getEvaluationConfirmation() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.evaluate_job_bottomsheet);
        MaterialButton evaluate = bottomSheetDialog.findViewById(R.id.apply);
        MaterialButton cancelBtn = bottomSheetDialog.findViewById(R.id.cancel);
        LinearLayout layout = bottomSheetDialog.findViewById(R.id.parent);
        LottieAnimationView animationView = bottomSheetDialog.findViewById(R.id.animation_view);

        evaluate.setOnClickListener(view->{
            animationView.setVisibility(View.VISIBLE);
            evaluate(animationView, bottomSheetDialog, layout);
            layout.setVisibility(View.INVISIBLE);
        });

        bottomSheetDialog.show();

        cancelBtn.setOnClickListener(view->bottomSheetDialog.dismiss());
    }

    private void evaluate(LottieAnimationView animationView, BottomSheetDialog bottomSheetDialog, LinearLayout layout) {
        Call<EvaluationResponse> userRequestCall = ApiClient.getUserService().evaluateCandidate(jobId, token);
        userRequestCall.enqueue(new Callback<EvaluationResponse>() {
            @Override
            public void onResponse(Call<EvaluationResponse> call, Response<EvaluationResponse> response) {
                if(response.isSuccessful()) {
                    EvaluationData data = response.body().getData();
                    startActivity(new Intent(JobDescriptionActivity.this, EvaluationResults.class)
                            .putExtra("data", new Gson().toJson(data)));

                } else {
                    Toast.makeText(JobDescriptionActivity.this, "Request failed. Try again", Toast.LENGTH_SHORT).show();
                }
                bottomSheetDialog.dismiss();
                layout.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<EvaluationResponse> call, Throwable t) {
                bottomSheetDialog.dismiss();
                animationView.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                Toast.makeText(JobDescriptionActivity.this, "Request failed. Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void shareJob() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this job on Inclusivo https://inclusivo.netlify.app/home/job/"+jobId);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share job via");
        startActivity(shareIntent);
    }


    public void gotoFragment(int fragment) {
        binding.jobDescriptionVp2.setCurrentItem(fragment);
    }

}