package com.dsciiita.inclusivo.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityAddScholarshipBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.Degree;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.responses.DegreeListsResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScholarshipActivity extends AppCompatActivity {

    ActivityAddScholarshipBinding binding;

    private HashMap<String, String> genderMap;

    private Calendar calendar;
    private String title, shortCode, desc, applyUrl, lastDate, selectionProcess, mobile;
    private int companyId, vacancy;
    private boolean isApplyHere;
    private List<Degree> degrees, selectedDegrees;
    private List<Diversity> tags;
    private List<String> degreeTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddScholarshipBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        calendar = Calendar.getInstance();

        binding.parent.setVisibility(View.INVISIBLE);
        getDegrees();

        binding.toolbar.setNavigationOnClickListener(view->finish());

        binding.lastDateTie.setOnClickListener(this::onClick);
        binding.btnAdd.setOnClickListener(this::onClick);

        degrees = new ArrayList<>();
        selectedDegrees = new ArrayList<>();
        tags = new ArrayList<>();


        binding.degreesAct.setOnItemClickListener((adapterView, view, i, l) -> {
            String selected = adapterView.getItemAtPosition(i).toString();
            int indexDeg = degreeTitles.indexOf(selected);
            Degree degree = degrees.get(indexDeg);
            selectedDegrees.add(degree);
            addDegreeChip(degree, selected);
            binding.tilDegrees.getEditText().setText("");
        });

        genderMap = Constants.buildMap();
        setValidationTextWatcher();
    }


    @Override
    public void onResume() {
        super.onResume();

        getDegrees();
        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<String> (AddScholarshipActivity.this,
                android.R.layout.select_dialog_item, degreeTitles);
        binding.degreesAct.setThreshold(0);             //will start working from first character
        binding.degreesAct.setAdapter(degreeAdapter);

    }

    private void addDegreeChip(Degree degree, String selected){
        Chip chip = (Chip) LayoutInflater.from(AddScholarshipActivity.this)
                .inflate(R.layout.add_job_parent_width_chip_layout,
                        binding.degreesChipGrp, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        chip.setCloseIconVisible(true);
        binding.degreesChipGrp.addView(chip);
        binding.degreesChipGrp.setVisibility(View.VISIBLE);
        chip.setOnCloseIconClickListener(v -> {
            selectedDegrees.remove(degree);
            binding.degreesChipGrp.removeView(chip);
        });
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.lastDateTie.setText(sdf.format(calendar.getTime()));
    }



    private void getValues() {
        companyId = SharedPrefManager.getInstance(this).getCompanyID();
        title = binding.tilScholarshipTitle.getEditText().getText().toString();
        shortCode = binding.tilScholarshipShortCode.getEditText().getText().toString();
        desc = binding.tilScholarshipDescription.getEditText().getText().toString().trim();
        lastDate = binding.tilLastDate.getEditText().getText().toString().trim();
        selectionProcess = binding.tilSelectionProcess.getEditText().getText().toString().trim();
        mobile = binding.tilScholarshipContactNum.getEditText().getText().toString().trim();
        isApplyHere = false;

        String vacancyS = binding.tilScholarshipVacancies.getEditText().getText().toString().trim();
        if(!vacancyS.isEmpty())
            vacancy = Integer.parseInt(vacancyS);

        List<Integer> ids = binding.TagChipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = binding.TagChipGroup.findViewById(id);
            tags.add(new Diversity(0, chip.getText().toString()));
        }

        applyUrl = binding.tilApplyUrl.getEditText().getText().toString();

        binding.tilScholarshipTitle.setError(null);
        binding.tilScholarshipDescription.setError(null);
        binding.tilSelectionProcess.setError(null);
        binding.tilApplyUrl.setError(null);
        binding.tilLastDate.setError(null);
    }


    private void getDegrees() {
        degreeTitles = new ArrayList<>();
        degrees = new ArrayList<>();

        binding.progressBar.setVisibility(View.VISIBLE);

        Call<DegreeListsResponse> userRequestCall = ApiClient.getUserService().getDegrees();
        userRequestCall.enqueue(new Callback<DegreeListsResponse>() {
            @Override
            public void onResponse(Call<DegreeListsResponse> call, Response<DegreeListsResponse> response) {
                if(response.isSuccessful()) {
                    DegreeListsResponse companyListsResponse = response.body();
                    Degree[] array = companyListsResponse.getData();
                    for (Degree value : array) {
                        degrees.add(value);
                        String degreeTitle = value.getName()+", "+value.getType()+", "+value.getSpecialization();
                        degreeTitles.add(degreeTitle);
                    }
                } else
                    Snackbar.make(binding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.parent.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<DegreeListsResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });

    }




    private void addScholarship() {
        binding.btnAdd.setEnabled(false);
        getValues();

        if(title.isEmpty()){
            binding.tilScholarshipTitle.setError("Title is required");
            binding.btnAdd.setEnabled(true);
            return;
        }

        if(desc.isEmpty()){
            binding.tilScholarshipDescription.setError("Description is required");
            binding.btnAdd.setEnabled(true);
            return;
        }

        if(selectionProcess.isEmpty()){
            binding.tilSelectionProcess.setError("Selection process is required");
            binding.btnAdd.setEnabled(true);
            return;
        }

        if(tags.isEmpty()){
            Snackbar.make(binding.parent, "Select at least 1 tag", BaseTransientBottomBar.LENGTH_SHORT).show();
            binding.btnAdd.setEnabled(true);
            return;
        }

        if(lastDate.isEmpty()){
            binding.tilLastDate.setError("Last date is required");
            binding.btnAdd.setEnabled(true);
            return;
        }

        if(applyUrl.isEmpty()){
            binding.tilApplyUrl.setError("Provide a apply url");
            binding.btnAdd.setEnabled(true);
            return;
        }

        String token = "token "+SharedPrefManager.getInstance(this).getToken();

        Scholarship scholarship = new Scholarship(title, companyId, shortCode, tags, desc,
                lastDate, isApplyHere, selectionProcess, vacancy, applyUrl, selectedDegrees, mobile, "Published");

        binding.progressBar.setVisibility(View.VISIBLE);
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().addScholarship(scholarship, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(AddScholarshipActivity.this, "Scholarship added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Snackbar.make(binding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();

                binding.btnAdd.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnAdd.setEnabled(true);
                Snackbar.make(binding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void onClick(View view) {
        if (view.getId() == R.id.last_date_tie) {
            showDatePicker();
        } else if (view.getId()==R.id.btnAdd) {
            setResult(RESULT_OK, new Intent());
            addScholarship();
        }
    }


    private class ValidationTextWatcher implements TextWatcher {
        private View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.scholarship_description_tie:
                    String[] text = binding.tilScholarshipDescription.getEditText().getText().toString().split(" ");
                    String lastWord, twoWords;
                    if(text.length==0)
                        lastWord = "";
                    else
                        lastWord = text[text.length-1];

                    if(text.length<=1)
                        twoWords = "";
                    else
                        twoWords = text[text.length-2]+" "+text[text.length-1];

                    lastWord = lastWord.toLowerCase();
                    twoWords = twoWords.toLowerCase();

                    if(genderMap.containsKey(lastWord))
                        binding.tilScholarshipDescription.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(lastWord)+"' instead of '"+lastWord+"'");
                    else if(genderMap.containsKey(twoWords))
                        binding.tilScholarshipDescription.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(twoWords)+"' instead of '"+twoWords+"'");
                    else
                        binding.tilScholarshipDescription.getEditText().setError(null);
                    break;
                case R.id.selection_process_tie:
                    String[] text2 = binding.tilSelectionProcess.getEditText().getText().toString().split(" ");
                    String lastWord2, twoWords2;
                    if(text2.length==0)
                        lastWord2 = "";
                    else
                        lastWord2 = text2[text2.length-1];

                    if(text2.length<=1)
                        twoWords2 = "";
                    else
                        twoWords2 = text2[text2.length-2]+" "+text2[text2.length-1];

                    lastWord2 = lastWord2.toLowerCase();
                    twoWords2 = twoWords2.toLowerCase();
                    if(genderMap.containsKey(lastWord2))
                        binding.tilSelectionProcess.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(lastWord2)+"' instead of '"+lastWord2+"'");
                    else if(genderMap.containsKey(twoWords2))
                        binding.tilSelectionProcess.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(twoWords2)+"' instead of '"+twoWords2+"'");
                    else
                        binding.tilSelectionProcess.getEditText().setError(null);
                    break;
            }
        }
    }

    private void setValidationTextWatcher() {
        binding.tilScholarshipDescription.getEditText().addTextChangedListener(new ValidationTextWatcher(binding.tilScholarshipDescription.getEditText()));
        binding.tilSelectionProcess.getEditText().addTextChangedListener(new ValidationTextWatcher(binding.tilScholarshipDescription.getEditText()));
    }

}