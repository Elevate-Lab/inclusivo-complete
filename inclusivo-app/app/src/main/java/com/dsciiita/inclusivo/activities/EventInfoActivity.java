package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.databinding.ActivityEventInfoBinding;
import com.dsciiita.inclusivo.models.Event;
import com.google.gson.Gson;

public class EventInfoActivity extends AppCompatActivity {

    ActivityEventInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventInfoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Event event = new Gson().fromJson(getIntent().getStringExtra("event"), Event.class);

        setValues(event);
    }

    private void setValues(Event event) {

        binding.title.setText(event.getTitle());
        binding.desc.setText(event.getDescription());
        binding.location.setText(event.getLocation());

        if(event.getThumbnail_url()!=null)
            Glide.with(this).load(event.getThumbnail_url())
                    .placeholder(R.drawable.login_bg_img)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                    .into(binding.thumbnail);
        else
            Glide.with(this).load(R.drawable.login_bg_img)
                    .into(binding.thumbnail);

        binding.parentLayout.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
}