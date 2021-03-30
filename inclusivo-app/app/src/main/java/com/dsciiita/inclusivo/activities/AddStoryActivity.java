package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityAddInitiativeBinding;
import com.dsciiita.inclusivo.databinding.ActivityAddStoryBinding;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoryActivity extends AppCompatActivity {

    ActivityAddStoryBinding binding;

    private Uri filepath;
    String profileUrl;
    private boolean isImageSelected;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStoryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        binding.btnAdd.setOnClickListener(this::onClick);
        binding.addStoryIcon.setOnClickListener(this::onClick);

        binding.toolbar.setNavigationOnClickListener(view->finish());

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.add_company_bottom_sheet);
        ImageButton sheetAddNew = bottomSheetDialog.findViewById(R.id.btnAddNew);
        ImageButton sheetRemove = bottomSheetDialog.findViewById(R.id.btnRemove);
        sheetRemove.setOnClickListener(this::onClick);
        sheetAddNew.setOnClickListener(this::onClick);
    }


    private void addStory() {

        binding.btnAdd.setEnabled(false);
        String token = "token "+ SharedPrefManager.getInstance(this).getToken();

        String name = binding.tilStoryName.getEditText().getText().toString().trim();
        String desc = binding.tilStoryDescription.getEditText().getText().toString().trim();

        if(name.isEmpty()){
            binding.tilStoryName.setError("Name required");
            binding.btnAdd.setEnabled(true);
            return;
        }

        if(desc.isEmpty()){
            binding.tilStoryDescription.setError("Description required");
            binding.btnAdd.setEnabled(true);
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        Story story = new Story(desc, name, profileUrl);

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().addStory(story, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddStoryActivity.this, "Story created successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddStoryActivity.this, CompanyProfileActivity.class)
                            .putExtra("companyID", SharedPrefManager.getInstance(AddStoryActivity.this).getCompanyID()));
                    finish();
                } else {
                    Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.btnAdd.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnAdd.setEnabled(true);
                Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            filepath = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                binding.storyImg.setImageBitmap(bitmap);
                binding.addStoryIcon.setImageResource(R.drawable.edit_logo);
                isImageSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if(filepath != null){
            binding.progressBar.setVisibility(View.VISIBLE);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String profileTitle = binding.tilStoryName.getEditText().getText().toString().trim()+"_"+timestamp.getTime();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("company/story/"+profileTitle);
            reference.putFile(filepath).addOnSuccessListener(taskSnapshot ->
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                            task -> {
                                profileUrl = task.getResult().toString();
                                addStory();
                            })).addOnFailureListener(e -> {
                binding.progressBar.setVisibility(View.GONE);
                Log.i("FAILURE", e.toString());
            });
        }
    }

    private void onClick(View view) {
        if(view.getId() == R.id.btnAdd) {
            if(isImageSelected)
                uploadImage();
            else
                addStory();
        } else if (view.getId() == R.id.add_story_icon) {
            if (!isImageSelected)
                chooseImage();
            else
                bottomSheetDialog.show();
        } else if(view.getId() == R.id.btnRemove) {
            isImageSelected = false;
            profileUrl = "";
            binding.addStoryIcon.setImageResource(R.drawable.profile_default);
            binding.storyImg.setImageResource(R.drawable.add_logo_default);
            bottomSheetDialog.cancel();
        } else if (view.getId() == R.id.btnAddNew) {
            chooseImage();
            isImageSelected = false;
            bottomSheetDialog.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddStoryActivity.this, CompanyProfileActivity.class)
                .putExtra("companyID", SharedPrefManager.getInstance(AddStoryActivity.this).getCompanyID()));
        finish();
    }

}