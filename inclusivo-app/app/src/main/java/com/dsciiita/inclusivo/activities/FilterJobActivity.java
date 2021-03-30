package com.dsciiita.inclusivo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityFilterBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.responses.CompanyListsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterJobActivity extends AppCompatActivity {

    ActivityFilterBinding binding;
    String selected;
    ArrayList<String> values, types, companies, tags, jobType;
    ArrayList<Integer> companySelected, tagSelected, jobTypeSelected;
    ArrayAdapter<String> arrayAdapter, typeAdapter;
    SeekBar salarySeekBar,experienceSeekBar;
    TextView salaryTextView, experienceTextView;
    RelativeLayout slidersLayout;
    LinearLayout salaryLayout,experienceLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(view -> finish());

        values = new ArrayList<>();
        types = new ArrayList<>();
        companySelected = new ArrayList<>();
        tagSelected = new ArrayList<>();
        jobTypeSelected = new ArrayList<>();
        slidersLayout = findViewById(R.id.slidersLayout);
        salarySeekBar = findViewById(R.id.seekBarSalary);
        experienceSeekBar = findViewById(R.id.seekBarExperience);
        salaryTextView = findViewById(R.id.seekBarSalaryText);
        experienceTextView = findViewById(R.id.seekBarExperienceText);
        salaryLayout = findViewById(R.id.salaryLayout);
        experienceLayout = findViewById(R.id.ExperienceLayout);
        slidersLayout.setVisibility(View.INVISIBLE);
        salaryLayout.setVisibility(View.INVISIBLE);
        experienceLayout.setVisibility(View.INVISIBLE);

        types.add("Job Type");
        types.add("Company");
        types.add("Tags");
        types.add("Salary");
        types.add("Experience");

        salarySeekBar.setMax(10000000);
        experienceSeekBar.setMax(60);
        salarySeekBar.setProgress(100000);
        experienceSeekBar.setProgress(1);

        getTags();
        getCompanies();
        getJobTypes();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.filter_view, values);
        binding.options.setAdapter(arrayAdapter);

        typeAdapter = new ArrayAdapter<>(this, R.layout.types_list_item, types);
        binding.type.setAdapter(typeAdapter);

        for(int i=0;i< values.size(); i++ )  {
            binding.options.setItemChecked(i,false);
        }

        binding.options.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        binding.options.setOnItemClickListener((parent, view, position, id) -> {
            switch (selected) {
                case "Company":
                    if (binding.options.isItemChecked(position))
                        companySelected.add(position);
                    else if (companySelected.contains(position))
                        companySelected.remove(Integer.valueOf(position));
                    break;
                case "Tags":
                    if (binding.options.isItemChecked(position))
                        tagSelected.add(position);
                    else if (tagSelected.contains(position))
                        tagSelected.remove(Integer.valueOf(position));
                    break;
                case "Job Type":
                    if (binding.options.isItemChecked(position))
                        jobTypeSelected.add(position);
                    else if (jobTypeSelected.contains(position))
                        jobTypeSelected.remove(Integer.valueOf(position));
                    break;
            }
        });

        binding.type.setOnItemClickListener((parent, view, position, id) -> {
            values.clear();
            if(position==0){
                selected = "Job Type";
                values.addAll(jobType);
                setChecked(jobTypeSelected);
                slidersLayout.setVisibility(View.GONE);
            } else if(position==1){
                selected = "Company";
                values.addAll(companies);
                setChecked(companySelected);
                slidersLayout.setVisibility(View.GONE);
            } else if(position==2){
                selected = "Tags";
                values.addAll(tags);
                setChecked(tagSelected);
                slidersLayout.setVisibility(View.GONE);
            } else if(position == 3){
                selected = "Salary";
                slidersLayout.setVisibility(View.VISIBLE);
                experienceLayout.setVisibility(View.GONE);
                salaryLayout.setVisibility(View.VISIBLE);

            } else if(position == 4){
                selected = "Experience";
                slidersLayout.setVisibility((View.VISIBLE));
                salaryLayout.setVisibility(View.GONE);
                experienceLayout.setVisibility(View.VISIBLE);
            }

            arrayAdapter.notifyDataSetChanged();
        });

        binding.apply.setOnClickListener(view->{
            createFilter();
        });

        binding.reset.setOnClickListener(view->{
            clearFilter();
        });


        salarySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10000;
                progress = progress * 10000;
                salaryTextView.setText(String.valueOf(progress));
                Log.i("<>",String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        experienceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                experienceTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void clearFilter() {
        tagSelected.clear();
        companySelected.clear();
        jobTypeSelected.clear();
        for(int i=0;i< values.size(); i++ )  {
            binding.options.setItemChecked(i,false);
        }
    }


    private void setChecked(ArrayList<Integer> arrayList) {
        for(int i=0; i< binding.options.getAdapter().getCount(); i++)  {
            binding.options.setItemChecked(i, false);
        }
        try {
            for(int i=0; i< arrayList.size(); i++)  {
                binding.options.setItemChecked(arrayList.get(i), true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    private void getTags(){
        tags = new ArrayList<>();
        tags.add("Female");
        tags.add("LGBTQI");
        tags.add("Veteran");
        tags.add("Working Mother");
        tags.add("Specially Abled");
    }

    private void getJobTypes() {
        jobType = new ArrayList<>();
        jobType.add("Full Time");
        jobType.add("Internship");
        jobType.add("Student");
        jobType.add("Contract");
    }

    private void getCompanies() {
        binding.progressBar.setVisibility(View.VISIBLE);

        String token = "token "+ SharedPrefManager.getInstance(this).getToken();
        companies = new ArrayList<>();
        Call<CompanyListsResponse> userRequestCall = ApiClient.getUserService().getCompanies(token);
        userRequestCall.enqueue(new Callback<CompanyListsResponse>() {
            @Override
            public void onResponse(Call<CompanyListsResponse> call, Response<CompanyListsResponse> response) {
                if(response.isSuccessful()) {
                    Company[] companyList = response.body().getData();
                    for (Company company : companyList) {
                        companies.add(company.getName());
                    }
                } else {
                    Toast.makeText(FilterJobActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<CompanyListsResponse> call, Throwable t) {
                Log.i("ERROR FAILURE", t.getMessage());
            }
        });
    }


    private void createFilter() {
        StringBuilder finalCompanies = new StringBuilder();
        for(int i=0; i<companySelected.size(); i++){
            if(i!=companySelected.size()-1)
                finalCompanies.append(companies.get(companySelected.get(i))).append("#@#");
            else
                finalCompanies.append(companies.get(companySelected.get(i)));
        }

        StringBuilder finalTags = new StringBuilder();
        for(int i=0; i<tagSelected.size(); i++){
            if(i!=tagSelected.size()-1)
                finalTags.append(tags.get(tagSelected.get(i))).append("#@#");
            else
                finalTags.append(tags.get(tagSelected.get(i)));
        }

        StringBuilder finalTypes = new StringBuilder();
        for(int i=0; i<jobTypeSelected.size(); i++){
            if(i!=jobTypeSelected.size()-1)
                finalTypes.append(jobType.get(jobTypeSelected.get(i))).append("#@#");
            else
                finalTypes.append(jobType.get(jobTypeSelected.get(i)));
        }



        Intent resultIntent = new Intent();
        resultIntent.putExtra("company_name", finalCompanies.toString());
        resultIntent.putExtra("tags", finalTags.toString());
        resultIntent.putExtra("job_type", finalTypes.toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }


}