package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.dsciiita.inclusivo.R;

public class OpenLinks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_links);

        Intent intent = getIntent();
        Uri data = intent.getData();

        String url = data.toString();

        int id;

        if(url.charAt(url.length()-1)=='/')
            url = url.substring(0, url.length()-2);

        if(url.equals("https://inclusivo.netlify.app"))
            Toast.makeText(this, "This is the Inclusivo!", Toast.LENGTH_LONG).show();
        else if(url.contains("list")) {
            if (url.contains("home/job/"))
                startActivity(new Intent(OpenLinks.this, JobListingActivity.class).putExtra("no_filter", ""));
            else if (url.contains("home/company/"))
                startActivity(new Intent(OpenLinks.this, CompanyListingActivity.class).putExtra("no_filter", ""));
            else if (url.contains("home/story/"))
                startActivity(new Intent(OpenLinks.this, StoryListing.class).putExtra("no_filter", ""));
            else if (url.contains("home/scholarship/"))
                startActivity(new Intent(OpenLinks.this, ScholarshipListing.class).putExtra("no_filter", ""));
        } else {
            String[] parts = url.split("/");
            try {
                id = Integer.parseInt(parts[parts.length - 1]);
                if (url.contains("home/job/"))
                    startActivity(new Intent(OpenLinks.this, JobDescriptionActivity.class).putExtra("id", id));
                else if (url.contains("home/company/"))
                    startActivity(new Intent(OpenLinks.this, CompanyProfileActivity.class).putExtra("companyID", id));
                else if (url.contains("home/story/"))
                    startActivity(new Intent(OpenLinks.this, StoryInfoActivity.class).putExtra("id", id));
                else if (url.contains("home/scholarship/"))
                    startActivity(new Intent(OpenLinks.this, ScholarshipDescriptionActivity.class).putExtra("id", id));
            }catch (Exception e){
                Toast.makeText(this, "Unsupported url", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}