package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityViewJobApplicationBinding;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.JobApplication;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.responses.ApplicationByIdResponse;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewJobApplicationActivity extends AppCompatActivity {

    private ActivityViewJobApplicationBinding binding;

    private ArrayAdapter<CharSequence> adapter;
    private int id;
    private String updatedStatus, status, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewJobApplicationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setToolBar();

        adapter = ArrayAdapter.createFromResource(this,
                R.array.application_status_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.updateStatus.setOnClickListener(this::onClick);
        binding.resumeLinkTxt.setOnClickListener(this::onClick);

        token  = "token "+ SharedPrefManager.getInstance(this).getToken();
        id = getIntent().getIntExtra("applicationId", 0);

        getJobApplication();
    }


    private void setToolBar() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void onClick(View view) {
        int id = view.getId();
        if (id == R.id.update_status) {
            getConfirmation();
        }

    }


    private void getJobApplication(){

        binding.progressBar.setVisibility(View.VISIBLE);

        Call<ApplicationByIdResponse> userRequestCall = ApiClient.getUserService().getApplicationByID(id, token);
        userRequestCall.enqueue(new Callback<ApplicationByIdResponse>() {
            @Override
            public void onResponse(Call<ApplicationByIdResponse> call, Response<ApplicationByIdResponse> response) {
                if(response.isSuccessful()) {
                    JobApplication application = response.body().getData();
                    setValues(application);
                } else {
                    Snackbar.make(binding.parent, "Something went wrong.", Snackbar.LENGTH_SHORT);
                }

                binding.progressBar.setVisibility(View.GONE);
                binding.parent.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<ApplicationByIdResponse> call, Throwable t) {
                Snackbar.make(binding.parent, "Something went wrong.", Snackbar.LENGTH_SHORT);
                binding.progressBar.setVisibility(View.GONE);
                binding.parent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setValues(JobApplication application) {
        UserCandidate candidate = application.getCandidate();
        if(candidate.getUser() != null) {
            binding.firstNameTxt.setText(candidate.getUser().getFirstName());
            binding.lastNameTxt.setText(candidate.getUser().getLastName());
            binding.emailTxt.setText(candidate.getUser().getEmail());
            binding.dobTxt.setText(candidate.getUser().getDob());
            binding.genderTxt.setText(candidate.getUser().getGender());

        }

        binding.answer.setText(application.getMessage());
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

        status = application.getStatus();
        binding.status.setText(application.getStatus());
        if(application.getStatus().equals("Selected") || application.getStatus().equals("Rejected"))
            binding.updateStatus.setVisibility(View.GONE);

       setTags(application);
       setLocations(application);


       binding.resumeLinkTxt.setOnClickListener(view->{
           Timestamp timestamp = new Timestamp(System.currentTimeMillis());
           startActivity(new Intent(this, PDFViewerActivity.class)
                   .putExtra("resume_link", application.getCandidate().getResumeLink())
                   .putExtra("name", candidate.getFirstName()+
                           "_"+candidate.getLastName()+"_"+timestamp.getTime()));
       });

        if (candidate.getLinkedIn() != null)
            addLink(binding.linkedIn, candidate.getLinkedIn());

        if (candidate.getTwitter() != null)
            addLink(binding.twitter, candidate.getTwitter());

        if (candidate.getGithub() != null)
            addLink(binding.github, candidate.getGithub());

    }

    private void updateApplicationStatus(BottomSheetDialog dialog, View progressBar) {

        String token = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().updateApplicationStatus(id, updatedStatus, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                status = updatedStatus;
                binding.status.setText(updatedStatus);
                if(status.equals("Selected") || status.equals("Rejected"))
                    binding.updateStatus.setVisibility(View.GONE);
                Toast.makeText(ViewJobApplicationActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                Toast.makeText(ViewJobApplicationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setTags(JobApplication application){
        List<Diversity> tags = application.getCandidate().getDiversity();
        for(Diversity tag: tags){
            String text = tag.getName();
            Chip chip = (Chip) LayoutInflater.from(ViewJobApplicationActivity.this)
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

    private void setLocations(JobApplication application){
        ArrayList<CityResponse> cities = application.getCandidate().getPrefCities();
        for(CityResponse cityResponse: cities){
            String text = cityResponse.getName() + ", " + cityResponse.getState().getName() + ", " + cityResponse.getState().getCounty().getName();
            addLocationChip(text);
        }
    }

    private void addLocationChip(String selected){
        Chip chip = (Chip) LayoutInflater.from(ViewJobApplicationActivity.this)
                .inflate(R.layout.tag_chip_layout,
                        binding.prefCitiesChipGrp, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        chip.setCloseIconVisible(false);
        binding.prefCitiesChipGrp.addView(chip);
    }

    private void getConfirmation() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.update_application_bottomsheet);
        MaterialButton applyBtn = bottomSheetDialog.findViewById(R.id.apply);
        applyBtn.setVisibility(View.GONE);
        MaterialButton cancelBtn = bottomSheetDialog.findViewById(R.id.cancel);
        ListView listView = bottomSheetDialog.findViewById(R.id.list_item);

        ArrayList<String> list = new ArrayList<>();

        TextView msg = bottomSheetDialog.findViewById(R.id.msg);
        msg.setText("Current status: "+status);

        switch (status) {
            case "Pending":
                list.add("Process");
                list.add("Rejected");
                break;
            case "Shortlisted":
                list.add("Selected");
                list.add("Rejected");
                break;
            case "Process":
                list.add("Shortlisted");
                list.add("Selected");
                list.add("Rejected");
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
            updateApplicationStatus(bottomSheetDialog, bottomSheetDialog.findViewById(R.id.progress_bar));
        });

        bottomSheetDialog.show();

        cancelBtn.setOnClickListener(view->bottomSheetDialog.dismiss());
    }
}