package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityViewStoryBinding;
import com.dsciiita.inclusivo.models.Degree;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.IDStoryResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryInfoActivity extends AppCompatActivity {

    private ActivityViewStoryBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewStoryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setToolBar();

        int id = getIntent().getIntExtra("id", 0);
        getStories(id);

        binding.shareStory.setOnClickListener(view->shareStory(id));

        binding.btnDelete.setOnClickListener(view->{
            deleteStory(id);
        });
    }

    private void setToolBar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view -> finish());
    }


    private void deleteStory(int id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnDelete.setEnabled(false);
        String token = "token "+SharedPrefManager.getInstance(this).getToken();

        Call<Void> userRequestCall = ApiClient.getUserService().deleteStory(id, token);
        userRequestCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                    setResult(RESULT_OK, new Intent());
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(StoryInfoActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                binding.btnDelete.setEnabled(true);

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.btnDelete.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(StoryInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getStories(int id) {

        binding.parentLayout.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String token  = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<IDStoryResponse> userRequestCall = ApiClient.getUserService().getStoryByID(id, token);
        userRequestCall.enqueue(new Callback<IDStoryResponse>() {
            @Override
            public void onResponse(Call<IDStoryResponse> call, Response<IDStoryResponse> response) {
                if(response.isSuccessful()) {
                    IDStoryResponse storyResponse = response.body();
                    Story story = storyResponse.getData();
                    setValues(story);
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<IDStoryResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues(Story story) {
        binding.storyName.setText(story.getName());
        binding.storyDescription.setText(story.getDescription());
        Glide.with(getApplicationContext()).load(story.getPhotoUrl())
                .placeholder(Constants.PLACEHOLDER_IMAGE)
                .into(binding.storyImg);

        if(SharedPrefManager.getInstance(this).isEmployer() &&
        SharedPrefManager.getInstance(this).getCompanyID() ==  story.getCompany().getId()){
            binding.btnDelete.setVisibility(View.VISIBLE);
        } else
            binding.btnDelete.setVisibility(View.GONE);
        
        binding.parentLayout.setVisibility(View.VISIBLE);
    }

    private void shareStory(int id) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this story on Inclusivo https://inclusivo.netlify.app/home/story/"+id);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share story via");
        startActivity(shareIntent);
    }


}