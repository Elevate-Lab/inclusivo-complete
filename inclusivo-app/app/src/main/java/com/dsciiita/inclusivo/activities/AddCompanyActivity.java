package com.dsciiita.inclusivo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityAddCompanyBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCompanyActivity extends AppCompatActivity {
    private ActivityAddCompanyBinding addCompanyBinding;

    private String name, title, shortCode, phoneNumber, size, profile, email, desc, address;
    private String website, twitter, facebook, linkedin, instagram, logoUrl = "";
    private Uri downloadUrl, filepath;
    private boolean isImageChoosen;
    private BottomSheetDialog bottomSheetDialog;
    private ImageButton sheetAddNew, sheetRemove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addCompanyBinding = ActivityAddCompanyBinding.inflate(LayoutInflater.from(this));
        setContentView(addCompanyBinding.getRoot());
        setToolBar();
        setValidations();
        isImageChoosen = false;

        addCompanyBinding.btnAdd.setOnClickListener(this::onClick);
        addCompanyBinding.addLogoIcon.setOnClickListener(this::onClick);

        addCompanyBinding.companySizeSpinner.setOnItemSelectedListener(myListener);


        bottomSheetDialog = new BottomSheetDialog(AddCompanyActivity.this);
        bottomSheetDialog.setContentView(R.layout.add_company_bottom_sheet);
        sheetAddNew = bottomSheetDialog.findViewById(R.id.btnAddNew);
        sheetRemove = bottomSheetDialog.findViewById(R.id.btnRemove);
        sheetRemove.setOnClickListener(this::onClick);
        sheetAddNew.setOnClickListener(this::onClick);
    }

    private void setToolBar() {
        addCompanyBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    /*---------------------- DATA PROCESSING ----------------------*/

    private void getValues() {
        name = addCompanyBinding.tilCompanyName.getEditText().getText().toString().trim();
        title = addCompanyBinding.tilCompanyTitle.getEditText().getText().toString().trim();
        shortCode = addCompanyBinding.tilCompanyShortCode.getEditText().getText().toString().trim();
        phoneNumber = addCompanyBinding.tilCompanyContactNum.getEditText().getText().toString().trim();
        profile = addCompanyBinding.tilCompanyProfile.getEditText().getText().toString().trim();
        email = addCompanyBinding.tilCompanyEmail.getEditText().getText().toString().trim();
        desc = addCompanyBinding.tilCompanyDescription.getEditText().getText().toString().trim();
        address = addCompanyBinding.tilCompanyAddress.getEditText().getText().toString().trim();
        website = addCompanyBinding.tilCompanyWebsite.getEditText().getText().toString().trim();
        twitter = addCompanyBinding.tilCompanyTwitter.getEditText().getText().toString().trim();
        facebook = addCompanyBinding.tilCompanyFacebook.getEditText().getText().toString().trim();
        linkedin = addCompanyBinding.tilCompanyLinkedin.getEditText().getText().toString().trim();
        instagram = addCompanyBinding.tilCompanyInstagram.getEditText().getText().toString().trim();
    }


    /*---------------------- API CALLS ----------------------*/

    private void addCompany(){
        addCompanyBinding.btnAdd.setEnabled(false);

        getValues();

        if(!validateCompanyTitle(title)) {
            addCompanyBinding.btnAdd.setEnabled(true);
            return;
        }
        if(!validateCompanyDescription(desc)){
            addCompanyBinding.btnAdd.setEnabled(true);
            return;
        }
        if(!validateCompanyProfile(profile)){
            addCompanyBinding.btnAdd.setEnabled(true);
            return;
        }

        String token = "token "+SharedPrefManager.getInstance(this).getToken();
        Company company = new Company(0, name, title, shortCode, phoneNumber, size,
                profile, email, desc, address, website, twitter, facebook, linkedin, instagram, logoUrl);

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().addCompany(company, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (addCompanyBinding.progressBar.getVisibility()==View.VISIBLE) {
                        addCompanyBinding.progressBar.setVisibility(View.GONE);
                    }
                    Toast.makeText(getApplicationContext(), "Company created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else{
                    Toast.makeText(AddCompanyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

                addCompanyBinding.btnAdd.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                addCompanyBinding.btnAdd.setEnabled(true);
                Toast.makeText(AddCompanyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            filepath = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                addCompanyBinding.logoImg.setImageBitmap(bitmap);
                isImageChoosen = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if(filepath != null){
            addCompanyBinding.progressBar.setVisibility(View.VISIBLE);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String logoTitle = addCompanyBinding.tilCompanyTitle.getEditText().getText().toString().trim()+"_"+timestamp.getTime();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("company/logo/"+logoTitle);
            reference.putFile(filepath).addOnSuccessListener(taskSnapshot ->
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                    task -> {
                        downloadUrl = task.getResult();
                        logoUrl = downloadUrl.toString();
                        addCompany();
                        Log.i("DOWNLOAD", downloadUrl.toString());
                    })).addOnFailureListener(e ->{
                        addCompanyBinding.progressBar.setVisibility(View.GONE);
                        Log.i("FAILURE", e.toString());
                    });
        }
    }



    /*---------------------- EVENTS HANDLING ----------------------*/

    private void onClick(View view) {

        if (view.getId() == R.id.btnAdd) {
            if(isImageChoosen)
                uploadImage();
            else
                addCompany();
        } else if (view.getId() == R.id.add_logo_icon){
            if(!isImageChoosen)
                chooseImage();
            else
                bottomSheetDialog.show();
        } else if(view.getId() == R.id.btnRemove) {
            isImageChoosen = false;
            logoUrl = "";
            addCompanyBinding.logoImg.setImageResource(R.drawable.add_logo_default);
            bottomSheetDialog.cancel();
        } else if (view.getId() == R.id.btnAddNew) {
            chooseImage();
            isImageChoosen = false;
            bottomSheetDialog.cancel();
        }
    }


    AdapterView.OnItemSelectedListener myListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
            int id = parent.getId();
            switch (id) {
                case R.id.company_size_spinner:
                    size = addCompanyBinding.companySizeSpinner.getItemAtPosition(position).toString();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };



    /*---------------------- VALIDATIONS ----------------------*/


    private void setValidations() {
        addCompanyBinding.companyTitleTie.addTextChangedListener(new ValidationTextWatcher(addCompanyBinding.companyTitleTie));
        addCompanyBinding.companyDescriptionTie.addTextChangedListener(new ValidationTextWatcher(addCompanyBinding.companyDescriptionTie));
        addCompanyBinding.companyProfileTie.addTextChangedListener(new ValidationTextWatcher(addCompanyBinding.companyProfileTie));
    }

    private boolean validateCompanyDescription(String companyTitle) {
        if (TextUtils.isEmpty(companyTitle)) {
            addCompanyBinding.tilCompanyDescription.setError("Company description is required !");
            requestFocus(addCompanyBinding.companyDescriptionTie);
            return false;
        } else {
            addCompanyBinding.tilCompanyTitle.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCompanyTitle(String companyDescription) {
        if (TextUtils.isEmpty(companyDescription)) {
            addCompanyBinding.tilCompanyTitle.setError("Company title is required !");
            requestFocus(addCompanyBinding.companyTitleTie);
            return false;
        } else {
            addCompanyBinding.tilCompanyTitle.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCompanyProfile(String companyProfile) {
        if (TextUtils.isEmpty(companyProfile)) {
            addCompanyBinding.tilCompanyProfile.setError("Profile description is required !");
            requestFocus(addCompanyBinding.companyProfileTie);
            return false;
        } else {
            addCompanyBinding.tilCompanyProfile.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class ValidationTextWatcher implements TextWatcher {
        private final View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.company_title_tie:
                    validateCompanyTitle(addCompanyBinding.tilCompanyTitle.getEditText().getText().toString().trim());
                    break;
                case R.id.company_description_tie:
                    validateCompanyDescription(addCompanyBinding.tilCompanyDescription.getEditText().getText().toString().trim());
                    break;
                case R.id.company_profile_tie:
                    validateCompanyProfile(addCompanyBinding.tilCompanyProfile.getEditText().getText().toString().trim());
            }
        }
    }
}