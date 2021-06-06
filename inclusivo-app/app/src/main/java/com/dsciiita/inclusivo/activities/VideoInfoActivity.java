package com.dsciiita.inclusivo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.VideoRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityVideoInfoBinding;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Video;
import com.dsciiita.inclusivo.models.Video;
import com.dsciiita.inclusivo.responses.BlogsByIdResponse;
import com.dsciiita.inclusivo.responses.VideosIdResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoInfoActivity extends AppCompatActivity {

    ActivityVideoInfoBinding binding;
    private float curr_sec = 0;
    private boolean isShowLess = false;
    private List<Diversity> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);

        binding = ActivityVideoInfoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setToolBar();

        getLifecycle().addObserver(binding.youtubePlayerView);


        binding.chipRemain.setOnClickListener(v -> {
            if(!isShowLess) {
                binding.chipRemain.setText("Show less");
                isShowLess = true;
                showAllTags();
            }else{
                isShowLess = false;
                showSelectTags();
            }
        });

        int id = getIntent().getIntExtra("id", 0);
        getVideo(id);

        //binding.shareStory.setOnClickListener(view->shareVideo(id));
    }

    private void showSelectTags() {
        binding.videoTagsChipGrp.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            String text = tags.get(i).getName();
            addTags(binding.videoTagsChipGrp, text);
            if (i == 2)
                break;
        }
        if (tags.size() > 3) {
            String remain = "+ " + (tags.size() - 3);
            binding.chipRemain.setVisibility(View.VISIBLE);
            binding.chipRemain.setText(remain);
        }
    }

    private void showAllTags() {
        binding.videoTagsChipGrp.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            String text = tags.get(i).getName();
            addTags(binding.videoTagsChipGrp, text);
        }
    }

    private void setToolBar() {

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeActionContentDescription("Back");
        binding.toolbar.setNavigationOnClickListener(view -> finish());
    }


    private void getVideo(int id) {
        binding.parentLayout.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String token  = "token "+ SharedPrefManager.getInstance(this).getToken();

        Call<VideosIdResponse> userRequestCall = ApiClient.getUserService().getVideoById(id, token);
        userRequestCall.enqueue(new Callback<VideosIdResponse>() {
            @Override
            public void onResponse(Call<VideosIdResponse> call, Response<VideosIdResponse> response) {
                if(response.isSuccessful()) {
                    VideosIdResponse response1 = response.body();
                    Video video  = response1.getData();
                    setValues(video);
                } else {
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<VideosIdResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void setValues(Video video) {
        binding.title.setText(video.getName());
        binding.author.setText(video.getAuthorCredits());
        binding.desc.setText(video.getDescription());
        Log.d("LINK", video.getVideoLink());
        String link = video.getVideoLink().split("=")[1];
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = link;
                youTubePlayer.loadVideo(videoId, curr_sec);
                youTubePlayer.play();
            }
            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                curr_sec = second;
            }
        });

        binding.youtubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        tags = video.getTags();
        int size = tags.size();
        if(size==0)
            binding.chipLayout.setVisibility(View.GONE);
        showSelectTags();

        binding.credit.setText(video.getCredits());

        binding.parentLayout.setVisibility(View.VISIBLE);
    }

    private void addTags(ChipGroup chipGroup, String selected){
        Chip chip = (Chip) LayoutInflater.from(this)
                .inflate(R.layout.tag_chip_layout, chipGroup, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        chipGroup.addView(chip);
    }

    private void shareVideo(int id) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //TODO: Video path
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this video on Inclusivo https://inclusivo.netlify.app/home/video/"+id);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share video via");
        startActivity(shareIntent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.toolbar.setVisibility(View.GONE);
            binding.youtubePlayerView.enterFullScreen();
            hideStatusBar();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            binding.toolbar.setVisibility(View.VISIBLE);
            showStatusBar();
            binding.youtubePlayerView.exitFullScreen();
        }
    }

    @Override
    public void onBackPressed() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }
    }


    void hideStatusBar(){
        View decorView = getWindow().getDecorView();
        // Hide Status Bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    void showStatusBar(){
        View decorView = getWindow().getDecorView();
        // Show Status Bar.
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}