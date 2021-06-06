package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityViewBlogBinding;
import com.dsciiita.inclusivo.databinding.ActivityViewStoryBinding;
import com.dsciiita.inclusivo.models.Blog;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.responses.BlogsByIdResponse;
import com.dsciiita.inclusivo.responses.BlogsByIdResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogInfoActivity extends AppCompatActivity {

    private ActivityViewBlogBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewBlogBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setToolBar();

        int id = getIntent().getIntExtra("id", 0);
        getBlog(id);

        binding.shareStory.setOnClickListener(view->shareBlog(id));

    }

    private void setToolBar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view -> finish());
    }


    private void getBlog(int id) {
        binding.parentLayout.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String token  = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<BlogsByIdResponse> userRequestCall = ApiClient.getUserService().getBlogById(id, token);
        userRequestCall.enqueue(new Callback<BlogsByIdResponse>() {
            @Override
            public void onResponse(Call<BlogsByIdResponse> call, Response<BlogsByIdResponse> response) {
                if(response.isSuccessful()) {
                    BlogsByIdResponse response1 = response.body();
                    Blog blog = response1.getData();
                    setValues(blog);
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<BlogsByIdResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues(Blog blog) {
        binding.storyName.setText(blog.getName());
        binding.storyDescription.setText(blog.getDescription());
        binding.blogAuthor.setText(blog.getAuthorCredits());
        Glide.with(getApplicationContext()).load(blog.getPhotoUrl())
                .placeholder(Constants.PLACEHOLDER_IMAGE)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                .into(binding.storyImg);

        List<Diversity> tags = blog.getTags();
        int size = tags.size();
        if(size==0)
            binding.chipLayout.setVisibility(View.GONE);
        binding.videoTagsChipGrp.removeAllViews();
        for (int i = 0; i < size; i++) {
            String text = tags.get(i).getName();
            addTags(binding.videoTagsChipGrp, text);
        }

        binding.credit.setText(blog.getCredits());

        binding.parentLayout.setVisibility(View.VISIBLE);
    }


    private void addTags(ChipGroup chipGroup, String selected){
        Chip chip = (Chip) LayoutInflater.from(this)
                .inflate(R.layout.tag_chip_layout, chipGroup, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        chipGroup.addView(chip);
    }

    private void shareBlog(int id) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //TODO: Blog path
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this blog on Inclusivo https://inclusivo.netlify.app/home/blog/"+id);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share blog via");
        startActivity(shareIntent);
    }


}