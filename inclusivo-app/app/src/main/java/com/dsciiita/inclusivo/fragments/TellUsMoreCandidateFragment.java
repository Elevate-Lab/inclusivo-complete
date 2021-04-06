package com.dsciiita.inclusivo.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.CreateAccountActivity;
import com.dsciiita.inclusivo.activities.NavigationActivity;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentTellUsMoreCandidateBinding;
import com.dsciiita.inclusivo.models.City;
import com.dsciiita.inclusivo.models.Country;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.State;
import com.dsciiita.inclusivo.models.User;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.LocationResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;


public class TellUsMoreCandidateFragment extends Fragment implements Step {

    FragmentTellUsMoreCandidateBinding binding;

    private String mobile, alternateMobile, resumeLink="";
    private String nationality, jobRole, profileDesc, regVia, year, month, token;
    private boolean isRelocation = false;
    private City city;
    private State state;
    private Country country;
    private ArrayList<Diversity> diversities;
    private ArrayList<City> cities;
    private ArrayList<CityResponse> prefCities;
    private ArrayList<State> states;
    private ArrayList<String> stateTitles;
    private ArrayList<Country> countries;
    private ArrayList<String> countryTitles;
    private ArrayList<String> locations;

    private TextView tvFileName;
    private String displayName;
    private int currYear;
    private LinearProgressIndicator uploadIndicator;
    private ImageButton btnClose;
    private LottieAnimationView btnUpload;
    private Uri resumeDownloadUrl, resumeFilepath;
    private boolean isAddVisible;


    public TellUsMoreCandidateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTellUsMoreCandidateBinding.inflate(inflater, container, false);

        binding.btnSubmit.setOnClickListener(this::onClick);
        diversities = new ArrayList<>();
        String[] refViaList = new String[]{"Email", "Social", "Careers", "Friends"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, refViaList);
        binding.spinnerRefVia.setAdapter(adapter);
        binding.spinnerRefVia.setOnItemSelectedListener(myListener);


        binding.cbIsRelocation.setOnCheckedChangeListener((buttonView, isChecked) -> isRelocation = isChecked);


        cities = new ArrayList<>();
        states = new ArrayList<>();
        stateTitles = new ArrayList<>();
        countryTitles = new ArrayList<>();
        countries = new ArrayList<>();
        prefCities = new ArrayList<>();


        currYear = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(currYear);
        month = "January";
        Integer[] years = {currYear, currYear+1, currYear+2};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, years);
        binding.yearAct.setAdapter(arrayAdapter);

        binding.yearAct.setOnItemSelectedListener(myListener);
        binding.monthAct.setOnItemSelectedListener(myListener);

        binding.locationAct.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedTitle = adapterView.getItemAtPosition(i).toString();
            String[] selected = adapterView.getItemAtPosition(i).toString().split(", ");
            int indexCity = locations.indexOf(selectedTitle);
            city = cities.get(indexCity);
            int indexState = stateTitles.indexOf(selected[1]);
            state = states.get(indexState);
            int indexCountry = countryTitles.indexOf(selected[2]);
            country = countries.get(indexCountry);
        });

        binding.preferredCityAct.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedTitle = adapterView.getItemAtPosition(i).toString();
            String[] selected = adapterView.getItemAtPosition(i).toString().split(", ");
            int indexCity = locations.indexOf(selectedTitle);
            City city = cities.get(indexCity);
            int indexState = stateTitles.indexOf(selected[1]);
            State state = states.get(indexState);
            int indexCountry = countryTitles.indexOf(selected[2]);
            Country country = countries.get(indexCountry);
            CityResponse pref_location = new CityResponse(city.getId(), state.getName(), city.getName(), country.getName(), state.getId(), country.getId());
            prefCities.add(pref_location);
            Log.d("CITIES", state.getName()+ city.getName()+ country.getName());
            addPrefCityChip(city, selectedTitle);
            binding.tilPreferredCity.getEditText().setText("");
        });


        isAddVisible = true;
        tvFileName = binding.getRoot().findViewById(R.id.file_name);
        btnClose = binding.getRoot().findViewById(R.id.cancel_button);
        uploadIndicator = binding.getRoot().findViewById(R.id.linear_progress);
        btnUpload = binding.getRoot().findViewById(R.id.upload_button);
        btnUpload.setOnClickListener(this::onClick);
        btnClose.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        getLocations();
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getContext(),
                android.R.layout.select_dialog_item, locations);
        binding.locationAct.setThreshold(1); //will start working from first character
        binding.locationAct.setAdapter(adapter);
        binding.preferredCityAct.setAdapter(adapter);
        binding.preferredCityAct.setThreshold(1);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }



    /*---------------------- DATA PROCESSING ----------------------*/

    private void getValues() {
        nationality = binding.tilNationality.getEditText().getText().toString().trim();
        jobRole = binding.tilJobRole.getEditText().getText().toString().trim();
        profileDesc = binding.profileDescriptionTil.getEditText().getText().toString().trim();


        mobile = binding.tilContactNumber.getEditText().getText().toString().trim();
        alternateMobile = binding.tilAlternateContactNumber.getEditText().getText().toString().trim();


        List<Integer> ids = binding.divChipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = binding.divChipGroup.findViewById(id);
            diversities.add(new Diversity(0, chip.getText().toString()));
        }
        token = "token "+ SharedPrefManager.getInstance(getActivity()).getToken();
    }

    private void addPrefCityChip(City city, String selected){
        Chip chip = new Chip(getContext());
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        chip.setCloseIconVisible(true);
        binding.prefCitiesChipGrp.addView(chip);
        binding.prefCitiesChipGrp.setVisibility(View.VISIBLE);
        chip.setOnCloseIconClickListener(v -> {
            prefCities.remove(city);
            binding.prefCitiesChipGrp.removeView(chip);
        });
    }


    /*---------------------- API CALLS ----------------------*/

    private void getLocations() {

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
                        cities.add(new City(cityResponse.getId(), cityResponse.getName()));
                        String state = cityResponse.getStateName();
                        stateTitles.add(cityResponse.getStateName());
                        states.add(new State(cityResponse.getStateId(), cityResponse.getStateName()));
                        countries.add(new Country(cityResponse.getCountryId(), cityResponse.getCountryName()));
                        countryTitles.add(cityResponse.getCountryName());
                        String country = cityResponse.getCountryName();
                        String entry = city + ", " + state + ", " + country;
                        locations.add(entry);
                    }
                    //Toast.makeText(AddJobActivity.this, "Done", Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(binding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Snackbar.make(binding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    public void completeCandidate(){

        binding.btnSubmit.setEnabled(false);
        getValues();

        binding.tilContactNumber.setError(null);
        binding.tilAlternateContactNumber.setError(null);

        if(mobile.isEmpty()){
            binding.btnSubmit.setEnabled(true);
            binding.tilContactNumber.setError("Required contact number");
            return;
        }

        if(alternateMobile.isEmpty()){
            binding.btnSubmit.setEnabled(true);
            binding.tilAlternateContactNumber.setError("Required alternate contact number");
            return;
        }

        if(diversities.isEmpty()){
            binding.btnSubmit.setEnabled(true);
            Toast.makeText(getActivity(), "Select diversities", Toast.LENGTH_SHORT).show();
            return;
        }

        if(resumeLink.isEmpty()){
            binding.btnSubmit.setEnabled(true);
            Toast.makeText(getActivity(), "Upload resume", Toast.LENGTH_SHORT).show();
            return;
        }

        UserCandidate userCandidate = new UserCandidate(new User(), nationality, jobRole, profileDesc, year, month, regVia,
                city, country, state, isRelocation, diversities, prefCities,
                mobile, alternateMobile, resumeLink);

        ((CreateAccountActivity) getActivity()).showProgressBar();

        Call<UserCandidate> userRequestCall = ApiClient.getUserService().completeCandidate(userCandidate, token);
        userRequestCall.enqueue(new Callback<UserCandidate>() {
            @Override
            public void onResponse(Call<UserCandidate> call, Response<UserCandidate> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Profile completed successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), NavigationActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else
                    Snackbar.make(binding.parent, "Something went wrong. Try again later.", BaseTransientBottomBar.LENGTH_SHORT).show();
                ((CreateAccountActivity) getActivity()).hideProgressBar();
                binding.btnSubmit.setEnabled(true);
            }
            @Override
            public void onFailure(Call<UserCandidate> call, Throwable t) {
                ((CreateAccountActivity) getActivity()).hideProgressBar();
                binding.btnSubmit.setEnabled(true);
                Snackbar.make(binding.parent, "Something went wrong. Try again later.", BaseTransientBottomBar.LENGTH_SHORT).show();

            }
        });
    }


    /*---------------------- BACKGROUND PROCESSING ----------------------*/

    private void choseFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file"), 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            resumeFilepath = data.getData();
            String uriString = resumeFilepath.toString();
            File myFile = new File(uriString);

            if (uriString.startsWith("content://")) {
                try (Cursor cursor = getActivity().getContentResolver().query(resumeFilepath, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
            tvFileName.setText(displayName);
            btnClose.setImageResource(R.drawable.ic_cancel);
            btnUpload.setVisibility(View.VISIBLE);
            isAddVisible = false;
        }
    }


    private void uploadFile() {
        if(resumeFilepath != null){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String resumeTitle = displayName+"_"+timestamp.getTime();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("candidate/resume/"+resumeTitle);
            reference.putFile(resumeFilepath).addOnSuccessListener(taskSnapshot ->
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                            task -> {
                                resumeDownloadUrl = task.getResult();
                                resumeLink = resumeDownloadUrl.toString();
                                uploadIndicator.setVisibility(GONE);
                                btnUpload.setVisibility(GONE);
                                btnUpload.cancelAnimation();
                                tvFileName.setText(displayName);
                                btnClose.setImageResource(R.drawable.ic_cancel);
                            })).addOnFailureListener(e -> Log.i("FAILURE", e.toString()))
            .addOnProgressListener(snapshot -> {
                int progress =(int) (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                uploadIndicator.setProgress(progress);
                tvFileName.setText("Uploading..."+progress+"%");
            });
        }
    }



    /*---------------------- EVENTS HANDLING ----------------------*/

    public void onClick(View view) {
        if(view.getId()== R.id.btnSubmit){
            completeCandidate();
        } else if(view.getId() == R.id.cancel_button) {
            if(isAddVisible)
                choseFile();
            else {
                tvFileName.setText("Upload resume");
                uploadIndicator.setVisibility(GONE);
                resumeLink = "";
                btnUpload.setVisibility(GONE);
                btnClose.setImageResource(R.drawable.ic_plus);
                isAddVisible = true;
            }

            btnUpload.setOnClickListener(this::onClick);
        } else if (view.getId() == R.id.upload_button) {
            uploadFile();
            btnUpload.playAnimation();
            btnUpload.setOnClickListener(null);
            uploadIndicator.setVisibility(View.VISIBLE);
            uploadIndicator.setProgress(0);
        }
    }

    AdapterView.OnItemSelectedListener myListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
            int id = parent.getId();
            switch (id) {
                case R.id.spinnerRefVia:
                    regVia = binding.spinnerRefVia.getItemAtPosition(position).toString();
                    break;
                case R.id.month_act:
                    month = binding.monthAct.getItemAtPosition(position).toString();
                    break;
                case R.id.year_act:
                    year = binding.yearAct.getItemAtPosition(position).toString();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

}