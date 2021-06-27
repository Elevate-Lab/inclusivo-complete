package com.dsciiita.inclusivo.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.AddCompanyActivity;
import com.dsciiita.inclusivo.activities.CreateAccountActivity;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentUpdateProfileBinding;
import com.dsciiita.inclusivo.databinding.UploadResumeBinding;
import com.dsciiita.inclusivo.models.User;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class UpdateProfileFragment extends Fragment implements Step {
    public static FragmentUpdateProfileBinding updateProfileFragmentBinding;
    private Calendar calendar;
    private String role="empty", gender="empty", profileUrl = "";
    private boolean isEmployer;
    private Uri filepath;
    private boolean isImageSelected;
    private BottomSheetDialog bottomSheetDialog;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateProfileFragmentBinding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        updateProfileFragmentBinding.dobTie.setOnClickListener(this::onClick);
        updateProfileFragmentBinding.candidateCard.setOnClickListener(this::onClick);
        updateProfileFragmentBinding.jobCard.setOnClickListener(this::onClick);
        updateProfileFragmentBinding.btnNext.setOnClickListener(this::onClick);
        updateProfileFragmentBinding.genderSpinner.setOnItemSelectedListener(myListener);
        calendar = Calendar.getInstance();

        updateProfileFragmentBinding.addLogoIcon.setOnClickListener(this::onClick);
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.add_company_bottom_sheet);
        ImageButton sheetAddNew = bottomSheetDialog.findViewById(R.id.btnAddNew);
        ImageButton sheetRemove = bottomSheetDialog.findViewById(R.id.btnRemove);
        sheetRemove.setOnClickListener(this::onClick);
        sheetAddNew.setOnClickListener(this::onClick);
        return updateProfileFragmentBinding.getRoot();
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

    private void checkedCandidateCard() {
        updateProfileFragmentBinding.candidateImg.setImageResource(R.drawable.looking_for_candidate_selected);
        updateProfileFragmentBinding.jobImg.setImageResource(R.drawable.looking_for_job_unselected);
        updateProfileFragmentBinding.candidateCard.setCardBackgroundColor(getResources().getColor(R.color.primary_color));
        updateProfileFragmentBinding.jobCard.setCardBackgroundColor(getResources().getColor(R.color.white));
        isEmployer = true;
        role = "Employer";
    }

    private void checkedJobCard() {
        updateProfileFragmentBinding.candidateImg.setImageResource(R.drawable.looking_for_candidate_unselected);
        updateProfileFragmentBinding.jobImg.setImageResource(R.drawable.looking_for_job_selected);
        updateProfileFragmentBinding.candidateCard.setCardBackgroundColor(getResources().getColor(R.color.white));
        updateProfileFragmentBinding.jobCard.setCardBackgroundColor(getResources().getColor(R.color.primary_color));
        isEmployer = false;
        role = "Candidate";
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        updateProfileFragmentBinding.dobTie.setText(sdf.format(calendar.getTime()));
    }


    /*---------------------- API CALLS ----------------------*/

    public void updateUserDetails(){



        String firstName = updateProfileFragmentBinding.tilFirstName.getEditText().getText().toString();
        String lastName = updateProfileFragmentBinding.tilLastName.getEditText().getText().toString();
        String email = updateProfileFragmentBinding.tilEmail.getEditText().getText().toString().trim();
        String dob = updateProfileFragmentBinding.dobTil.getEditText().getText().toString().trim();

        ((CreateAccountActivity) getActivity()).hideProgressBar();

        if(email.isEmpty()){
            updateProfileFragmentBinding.btnNext.setEnabled(true);
            Snackbar.make(updateProfileFragmentBinding.parent, "Email required", BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        if (!validate(firstName, lastName)) {
            updateProfileFragmentBinding.btnNext.setEnabled(true);
            return;
        }


        if(dob.isEmpty()){
            updateProfileFragmentBinding.btnNext.setEnabled(true);
            Toast.makeText(getActivity(), "Select date of birth", Toast.LENGTH_SHORT).show();
            return;
        }

        if(gender.equals("empty") || gender.equals("Select")){
            updateProfileFragmentBinding.btnNext.setEnabled(true);
            Toast.makeText(getActivity(), "Select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if(role.equals("empty")){
            updateProfileFragmentBinding.btnNext.setEnabled(true);
            Toast.makeText(getActivity(), "Select a role", Toast.LENGTH_SHORT).show();
            return;
        }


        ((CreateAccountActivity) getActivity()).showProgressBar();


        User user = new User(profileUrl, firstName, lastName, isEmployer, email, dob, gender);
        //auth token
        String token = "token "+ SharedPrefManager.getInstance(getActivity()).getToken();

        Call<User> userRequestCall = ApiClient.getUserService().updateUser(user, token);
        userRequestCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    //navigate to respective pages
                    if(isEmployer)
                        ((CreateAccountActivity) getActivity()).replaceFragments(4);
                    else
                        ((CreateAccountActivity) getActivity()).replaceFragments(3);
                    SharedPrefManager.getInstance(getActivity()).setEmployer(isEmployer);
                }else
                    Snackbar.make(updateProfileFragmentBinding.parent, "Something went wrong. Try again later.", BaseTransientBottomBar.LENGTH_SHORT).show();
                ((CreateAccountActivity) getActivity()).hideProgressBar();
                updateProfileFragmentBinding.btnNext.setEnabled(true);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                updateProfileFragmentBinding.btnNext.setEnabled(true);
                Snackbar.make(updateProfileFragmentBinding.parent, "Something went wrong. Try again later.", BaseTransientBottomBar.LENGTH_SHORT).show();
                ((CreateAccountActivity) getActivity()).hideProgressBar();
            }
        });
    }



    /*---------------------- BACKGROUND PROCESSING ----------------------*/

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            filepath = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                updateProfileFragmentBinding.profileImg.setImageBitmap(bitmap);
                updateProfileFragmentBinding.addLogoIcon.setImageResource(R.drawable.edit_logo);
                isImageSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if(filepath != null){
            ((CreateAccountActivity) getActivity()).showProgressBar();
            updateProfileFragmentBinding.btnNext.setEnabled(false);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String profileTitle = updateProfileFragmentBinding.tilFirstName.getEditText().getText().toString().trim()+"_"+
                    updateProfileFragmentBinding.tilLastName.getEditText().getText().toString().trim()+"_"+timestamp.getTime();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("user/profile/"+profileTitle);
            reference.putFile(filepath).addOnSuccessListener(taskSnapshot ->
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                            task -> {
                                profileUrl = task.getResult().toString();
                                updateUserDetails();
                            })).addOnFailureListener(e -> {
                                updateProfileFragmentBinding.btnNext.setEnabled(true);
                                ((CreateAccountActivity) getActivity()).hideProgressBar();
                                Log.i("FAILURE", e.toString());
                            });
        }
    }



    /*---------------------- EVENTS HANDLING ----------------------*/

    public void onClick(View view) {
        if (view.getId() == R.id.dob_tie) {
            showDatePicker();
        } else if (view.getId() == R.id.job_card) {
            checkedJobCard();
        } else if (view.getId() == R.id.candidate_card) {
            checkedCandidateCard();
        }else if(view.getId() == R.id.btnNext) {
            if(isImageSelected)
                uploadImage();
            else
                updateUserDetails();
        } else if (view.getId() == R.id.add_logo_icon) {
            if (!isImageSelected)
                chooseImage();
            else
                bottomSheetDialog.show();
        } else if(view.getId() == R.id.btnRemove) {
            isImageSelected = false;
            profileUrl = "";
            updateProfileFragmentBinding.addLogoIcon.setImageResource(R.drawable.profile_default);
            updateProfileFragmentBinding.profileImg.setImageResource(R.drawable.add_logo_default);
            bottomSheetDialog.cancel();
        } else if (view.getId() == R.id.btnAddNew) {
            chooseImage();
            isImageSelected = false;
            bottomSheetDialog.cancel();
        }
    }


    AdapterView.OnItemSelectedListener myListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
            int id = parent.getId();
            switch (id) {
                case R.id.gender_spinner:
                    gender = updateProfileFragmentBinding.genderSpinner.getItemAtPosition(position).toString();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };



    /*---------------------- VALIDATIONS ----------------------*/

    private boolean validate(String firstName, String lastName) {
        if(firstName.isEmpty()){
            updateProfileFragmentBinding.tilFirstName.setError("First name is required field!");
            requestFocus(updateProfileFragmentBinding.tilFirstName.getEditText());
            return false;
        }else{
            updateProfileFragmentBinding.tilFirstName.setErrorEnabled(false);
        }

        if(lastName.isEmpty()){
            updateProfileFragmentBinding.tilLastName.setError("Last name is required field!");
            requestFocus(updateProfileFragmentBinding.tilLastName.getEditText());
            return false;
        }else{
            updateProfileFragmentBinding.tilLastName.setErrorEnabled(false);
        }

        if(role.equals("empty"))
            Toast.makeText(getContext(), "Select a role", Toast.LENGTH_SHORT).show();

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}