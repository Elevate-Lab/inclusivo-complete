package com.dsciiita.inclusivo.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityAddJobBinding;
import com.dsciiita.inclusivo.fragments.RegisterFragment;
import com.dsciiita.inclusivo.models.City;
import com.dsciiita.inclusivo.models.Degree;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.responses.DegreeListsResponse;
import com.dsciiita.inclusivo.responses.JobByIdResponse;
import com.dsciiita.inclusivo.responses.LocationResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditJobActivity extends AppCompatActivity {
    Calendar calendar;
    ActivityAddJobBinding addJobBinding;

    private String title, shortCode, jobRole, desc, applyUrl, jobType, lastDate, selectionProcess, minSal, maxSal, status="Draft";
    private int companyId, minExp = 0, maxExp = 0, vacancies;
    private boolean isApplyHere = true, displaySalary = false;

    private List<City> cities, selectedCities;
    private List<Degree> degrees, selectedDegrees;
    private List<Diversity> tags;
    private List<String> degreeTitles;
    private ArrayList<String> locations;

    private ArrayAdapter<CharSequence> adapter;

    private Job editedJob;
    private int jobID;

    private HashMap<String, String> genderMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        addJobBinding = ActivityAddJobBinding.inflate(LayoutInflater.from(this));
        setContentView(addJobBinding.getRoot());

        setToolBar();

        addJobBinding.lastDateTie.setOnClickListener(this::onClick);
        addJobBinding.btnAdd.setOnClickListener(this::onClick);
        addJobBinding.btnSaveJob.setOnClickListener(this::onClick);

        addJobBinding.workExpRangeSeekBar.setOnRangeChangedListener(rangeChangedListener);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.job_type_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addJobBinding.spinnerJobType.setAdapter(adapter);
        addJobBinding.spinnerJobType.setOnItemSelectedListener(myListener);

        addJobBinding.displaySalaryCheckBox.setOnCheckedChangeListener(checkListener);
        addJobBinding.isApplyCheckBox.setOnCheckedChangeListener(checkListener);
        addJobBinding.publicCheckBox.setOnCheckedChangeListener(checkListener);

        degrees = new ArrayList<>();
        cities = new ArrayList<>();
        selectedCities = new ArrayList<>();
        selectedDegrees = new ArrayList<>();
        tags = new ArrayList<>();

        addJobBinding.locationAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                int indexLoc = locations.indexOf(selected);
                City city = cities.get(indexLoc);
                selectedCities.add(city);
                addLocationChip(city, selected);
                addJobBinding.tilLocations.getEditText().setText("");
                Log.i("SELECTED", city.getName()+" | "+city.getId());
            }
        });


        addJobBinding.degreesAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                int indexDeg = degreeTitles.indexOf(selected);
                Degree degree = degrees.get(indexDeg);
                selectedDegrees.add(degree);
                addDegreeChip(degree, selected);
                addJobBinding.tilDegrees.getEditText().setText("");
                Log.i("SELECTED", degree.getName()+" | "+degree.getId());
            }
        });


        Intent intent = getIntent();
        if(intent.hasExtra("id")) {
            jobID = intent.getIntExtra("id", -1);
            getJob(jobID);
            addJobBinding.title.setText("Edit Job");
            addJobBinding.progressBar.setVisibility(View.VISIBLE);
            addJobBinding.jobStatus.setVisibility(View.VISIBLE);
            addJobBinding.publicCheckBox.setVisibility(View.GONE);
        }


        genderMap = Constants.buildMap();
        setValidationTextWatcher();
    }

    @Override
    public void onResume() {
        super.onResume();

        addJobBinding.parent.setVisibility(View.INVISIBLE);
        getDegrees();
        getLocations();
        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<String> (AddEditJobActivity.this,
                android.R.layout.select_dialog_item, degreeTitles);
        addJobBinding.degreesAct.setThreshold(0); //will start working from first character
        addJobBinding.degreesAct.setAdapter(degreeAdapter);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.select_dialog_item, locations);
        addJobBinding.locationAct.setThreshold(0); //will start working from first character
        addJobBinding.locationAct.setAdapter(locationAdapter);
    }


    private void setToolBar() {
        addJobBinding.toolbar.setNavigationOnClickListener(view -> finish());
    }


    /*---------------------- DATA PROCESSING ----------------------*/

    private void addLocationChip(City city, String selected){
        Chip chip = (Chip) LayoutInflater.from(AddEditJobActivity.this)
                .inflate(R.layout.add_job_parent_width_chip_layout,
                        addJobBinding.locationChipGrp, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        chip.setCloseIconVisible(true);
        addJobBinding.locationChipGrp.addView(chip);
        addJobBinding.locationChipGrp.setVisibility(View.VISIBLE);
        chip.setOnCloseIconClickListener(v -> {
            selectedCities.remove(city);
            addJobBinding.locationChipGrp.removeView(chip);
        });
    }

    private void addDegreeChip(Degree degree, String selected){
        Chip chip = (Chip) LayoutInflater.from(AddEditJobActivity.this)
                .inflate(R.layout.add_job_parent_width_chip_layout,
                        addJobBinding.degreesChipGrp, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        chip.setCloseIconVisible(true);
        addJobBinding.degreesChipGrp.addView(chip);
        addJobBinding.degreesChipGrp.setVisibility(View.VISIBLE);
        chip.setOnCloseIconClickListener(v -> {
            selectedDegrees.remove(degree);
            addJobBinding.degreesChipGrp.removeView(chip);
        });
    }

    private void getValues() {
        companyId = SharedPrefManager.getInstance(this).getCompanyID();
        title = addJobBinding.tilJobTitle.getEditText().getText().toString();
        shortCode = addJobBinding.tilJobCode.getEditText().getText().toString();
        jobRole = addJobBinding.tilJobRole.getEditText().getText().toString().trim();
        desc = addJobBinding.tilJobDescription.getEditText().getText().toString().trim();
        lastDate = addJobBinding.tilLastDate.getEditText().getText().toString().trim();
        selectionProcess = addJobBinding.tilSelectionProcess.getEditText().getText().toString().trim();
        minSal = addJobBinding.tilMinimumSalary.getEditText().getText().toString().trim();
        maxSal = addJobBinding.tilMaximumSalary.getEditText().getText().toString().trim();

        String minExpS = addJobBinding.tilMinExp.getEditText().getText().toString().trim();
        String maxExpS = addJobBinding.tilMaxExp.getEditText().getText().toString().trim();
        String vacancyS = addJobBinding.tilJobVacancies.getEditText().getText().toString().trim();
        if(!minExpS.isEmpty())
            minExp = Integer.parseInt(minExpS);
        if(!maxExpS.isEmpty())
            maxExp = Integer.parseInt(maxExpS);
        if(!vacancyS.isEmpty())
            vacancies = Integer.parseInt(vacancyS);


        List<Integer> ids = addJobBinding.TagChipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = addJobBinding.TagChipGroup.findViewById(id);
            tags.add(new Diversity(0, chip.getText().toString()));
        }

        if(!isApplyHere)
            applyUrl = addJobBinding.tilApplyUrl.getEditText().getText().toString();
        else
            applyUrl = null;
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
        addJobBinding.lastDateTie.setText(sdf.format(calendar.getTime()));
    }



    /*---------------------- For Edit Job ----------------------*/

    private void getJob(int jobId) {

        String token  = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<JobByIdResponse> userRequestCall = ApiClient.getUserService().getJobByID(jobId, token);
        userRequestCall.enqueue(new Callback<JobByIdResponse>() {
            @Override
            public void onResponse(Call<JobByIdResponse> call, Response<JobByIdResponse> response) {
                if(response.isSuccessful()) {
                    JobByIdResponse jobsResponse = response.body();
                    editedJob = jobsResponse.getData().getJob();
                    setValues(editedJob);
                } else {
                    Snackbar.make(addJobBinding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JobByIdResponse> call, Throwable t) {
                Snackbar.make(addJobBinding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues(Job job) {
        addJobBinding.tilJobTitle.getEditText().setText(job.getTitle());
        addJobBinding.tilJobDescription.getEditText().setText(job.getDescription());
        addJobBinding.tilJobRole.getEditText().setText(job.getJobRole());
        addJobBinding.tilJobCode.getEditText().setText(job.getShortCode());
        addJobBinding.tilLastDate.getEditText().setText(job.getLastDate());
        addJobBinding.tilJobVacancies.getEditText().setText(String.valueOf(job.getVacancies()));
        addJobBinding.tilMinExp.getEditText().setText(String.valueOf(job.getMinExp()));
        addJobBinding.tilMaxExp.getEditText().setText(String.valueOf(job.getMaxExp()));
        addJobBinding.tilMinimumSalary.getEditText().setText(job.getMinSal());
        addJobBinding.tilMaximumSalary.getEditText().setText(job.getMaxSal());
        int position = adapter.getPosition(job.getJobType());
        addJobBinding.spinnerJobType.setSelection(position);
        addJobBinding.tilSelectionProcess.getEditText().setText(job.getSelectionProcess());

        addJobBinding.isApplyCheckBox.setChecked(job.getApplyHere());
        addJobBinding.displaySalaryCheckBox.setChecked(job.getDisplaySalary());

        setTags(job);
        setLocations(job);
        setDegrees(job);

        addJobBinding.btnAdd.setVisibility(View.GONE);
        addJobBinding.btnSaveJob.setVisibility(View.VISIBLE);
        addJobBinding.btnDeleteJob.setVisibility(View.VISIBLE);

    }

    private void setLocations(Job job){
        List<CityResponse> cities = job.getLocations();
        for(CityResponse cityResponse: cities){
            City city = new City(cityResponse.getId(), cityResponse.getName());
            selectedCities.add(city);
            String text = cityResponse.getName() + ", " + cityResponse.getStateName() + ", " + cityResponse.getCountryName();
            addLocationChip(city, text);
        }
    }

    private void setTags(Job job){
        List<Diversity> tags = job.getDiversities();
        for(Diversity tag: tags){
            String text = tag.getName();
            Chip chip = addJobBinding.TagChipGroup.findViewWithTag(text);
            chip.setChecked(true);
        }
    }

    private void setDegrees(Job job){
        List<Degree> degrees = job.getDegrees();
        for(Degree degree: degrees){
            selectedDegrees.add(degree);
            String text = degree.getName();
            addDegreeChip(degree, text);
        }
    }

    private void updateJobStatus() {
        addJobBinding.btnSaveJob.setEnabled(false);

        addJobBinding.progressBar.setVisibility(View.VISIBLE);

        String token = "token "+SharedPrefManager.getInstance(this).getToken();

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().updateJobStatus(jobID, status, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                addJobBinding.progressBar.setVisibility(View.GONE);
                DefaultResponse response1 = response.body();
                Toast.makeText(AddEditJobActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();

                addJobBinding.btnSaveJob.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                addJobBinding.progressBar.setVisibility(View.GONE);
                addJobBinding.btnSaveJob.setEnabled(true);
                Toast.makeText(AddEditJobActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*---------------------- API CALLS ----------------------*/

    private void addJob() {
        addJobBinding.btnAdd.setEnabled(false);

        addJobBinding.tilJobTitle.setError(null);
        addJobBinding.tilJobDescription.setError(null);
        addJobBinding.tilJobRole.setError(null);
        addJobBinding.tilLastDate.setError(null);
        addJobBinding.tilJobCode.setError(null);
        addJobBinding.tilSelectionProcess.setError(null);

        getValues();

        if(title.isEmpty()){
            addJobBinding.tilJobTitle.setError("Title required");
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(jobRole.isEmpty()){
            addJobBinding.tilJobRole.setError("Job role required");
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(jobRole.length()>50){
            addJobBinding.tilJobRole.setError("Maximum 50 characters are allowed.");
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(desc.isEmpty()){
            addJobBinding.tilJobDescription.setError("Description required");
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(selectionProcess.isEmpty()){
            addJobBinding.tilSelectionProcess.setError("Selection process required");
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(tags.isEmpty()){
            Toast.makeText(this, "Add tags", Toast.LENGTH_SHORT).show();
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(locations.isEmpty()){
            Toast.makeText(this, "Add locations", Toast.LENGTH_SHORT).show();
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(shortCode.isEmpty()){
            addJobBinding.tilJobCode.setError("Short code required");
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(lastDate.isEmpty()){
            addJobBinding.tilLastDate.setError("Last date required");
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        if(!isApplyHere && applyUrl.isEmpty()){
            Toast.makeText(this, "Provide external apply URL", Toast.LENGTH_SHORT).show();
            addJobBinding.btnAdd.setEnabled(true);
            return;
        }

        String token = "token "+SharedPrefManager.getInstance(this).getToken();

        Job job = new Job(companyId, title, shortCode, selectedCities, tags, jobRole, desc,
                selectedDegrees, jobType, isApplyHere, applyUrl, lastDate, minExp, maxExp, selectionProcess,
                minSal, maxSal, displaySalary, status, vacancies);

        addJobBinding.progressBar.setVisibility(View.VISIBLE);
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().addJob(job, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                addJobBinding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditJobActivity.this, "Job created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Snackbar.make(addJobBinding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();

                addJobBinding.btnAdd.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                addJobBinding.progressBar.setVisibility(View.GONE);
                addJobBinding.btnAdd.setEnabled(true);
                Snackbar.make(addJobBinding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getDegrees() {
        degreeTitles = new ArrayList<>();
        degrees = new ArrayList<>();

        Call<DegreeListsResponse> userRequestCall = ApiClient.getUserService().getDegrees();
        userRequestCall.enqueue(new Callback<DegreeListsResponse>() {
            @Override
            public void onResponse(Call<DegreeListsResponse> call, Response<DegreeListsResponse> response) {
                if(response.isSuccessful()) {
                    DegreeListsResponse companyListsResponse = response.body();
                    Degree[] array = companyListsResponse.getData();
                    for (Degree value : array) {
                        degrees.add(value);
                        Log.i("Degree ", value.getName());
                        String degreeTitle = value.getName()+", "+value.getType()+", "+value.getSpecialization();
                        degreeTitles.add(degreeTitle);
                    }
                } else
                    Snackbar.make(addJobBinding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                addJobBinding.progressBar.setVisibility(View.GONE);
                addJobBinding.parent.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<DegreeListsResponse> call, Throwable t) {
                Snackbar.make(addJobBinding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private void getLocations() {
        addJobBinding.progressBar.setVisibility(View.VISIBLE);
        locations = new ArrayList<>();

        Call<LocationResponse> userRequestCall = ApiClient.getUserService().getLocations("india");
        userRequestCall.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if(response.isSuccessful()) {
                    LocationResponse locationResponse = response.body();
                    //single object containing list of cities
                    List<CityResponse> cityResponses = locationResponse.getCities();
                    for(CityResponse cityResponse : cityResponses) {
                        String city = cityResponse.getName();
                        String state = cityResponse.getStateName();
                        String country = cityResponse.getCountryName();
                        String entry = city + ", " + state + ", " + country;
                        locations.add(entry);
                        Log.i("CITY", cityResponse.getId() + "aa" + cityResponse.getName());
                        cities.add(new City(cityResponse.getId(), city));
                    }
                    addJobBinding.tilLocations.setEnabled(true);
                    addJobBinding.locationAct.setHint("Search locations");
                } else {
                    Snackbar.make(addJobBinding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Snackbar.make(addJobBinding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



   /*---------------------- EVENTS HANDLING ----------------------*/


    private void onClick(View view) {
        if (view.getId() == R.id.last_date_tie) {
            showDatePicker();
        } else if (view.getId()==R.id.btnAdd) {
            setResult(RESULT_OK, new Intent());
            addJob();
        } else if (view.getId()==R.id.btnSaveJob) {
            updateJobStatus();
        }
    }


    AdapterView.OnItemSelectedListener myListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
            int id = parent.getId();
            switch (id) {
                case R.id.spinnerJobType:
                    jobType = addJobBinding.spinnerJobType.getItemAtPosition(position).toString();
                    break;
                case R.id.spinnerJobStatus:
                    if(addJobBinding.jobStatus.getVisibility()==View.VISIBLE)
                        status = addJobBinding.spinnerJobStatus.getItemAtPosition(position).toString();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };


    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            switch (id) {
                case R.id.display_salary_check_box:
                    displaySalary = addJobBinding.displaySalaryCheckBox.isChecked();
                    break;
                case R.id.is_apply_check_box:
                    isApplyHere = addJobBinding.isApplyCheckBox.isChecked();
                    if(!isApplyHere){
                        addJobBinding.tilApplyUrl.setVisibility(View.VISIBLE);
                        applyUrl = null;
                        addJobBinding.tilApplyUrl.getEditText().setText("");
                    }
                    else
                        addJobBinding.tilApplyUrl.setVisibility(View.GONE);
                    break;
                case R.id.public_check_box:
                    if(addJobBinding.publicCheckBox.getVisibility()==View.VISIBLE) {
                        if (addJobBinding.displaySalaryCheckBox.isChecked())
                            status = "Published";
                        else
                            status = "Draft";
                    }
                    break;
            }
        }
    };

    OnRangeChangedListener rangeChangedListener = new OnRangeChangedListener() {
        @Override
        public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
            addJobBinding.tilMinExp.getEditText().setText(String.valueOf((int)leftValue/10));
            addJobBinding.tilMaxExp.getEditText().setText(String.valueOf((int)rightValue/10));
        }

        @Override
        public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

        }

        @Override
        public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

        }
    };


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
                case R.id.job_description_tie:
                    String[] text = addJobBinding.tilJobDescription.getEditText().getText().toString().split(" ");
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
                        addJobBinding.tilJobDescription.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(lastWord)+"' instead of '"+lastWord+"'");
                    else if(genderMap.containsKey(twoWords))
                        addJobBinding.tilJobDescription.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(twoWords)+"' instead of '"+twoWords+"'");
                    else
                        addJobBinding.tilJobDescription.getEditText().setError(null);
                    break;
                case R.id.selection_process_tie:
                    String[] text2 = addJobBinding.tilSelectionProcess.getEditText().getText().toString().split(" ");
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
                        addJobBinding.tilSelectionProcess.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(lastWord2)+"' instead of '"+lastWord2+"'");
                    else if(genderMap.containsKey(twoWords2))
                        addJobBinding.tilSelectionProcess.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(twoWords2)+"' instead of '"+twoWords2+"'");
                    else
                        addJobBinding.tilSelectionProcess.getEditText().setError(null);
                    break;
            }
        }
    }

    private void setValidationTextWatcher() {
        addJobBinding.tilJobDescription.getEditText().addTextChangedListener(new ValidationTextWatcher(addJobBinding.tilJobDescription.getEditText()));
        addJobBinding.tilSelectionProcess.getEditText().addTextChangedListener(new ValidationTextWatcher(addJobBinding.tilSelectionProcess.getEditText()));
    }

}