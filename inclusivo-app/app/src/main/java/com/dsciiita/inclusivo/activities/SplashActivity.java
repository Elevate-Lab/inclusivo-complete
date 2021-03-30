package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.storage.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SharedPrefManager.getInstance(this).isNew()){
            startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
        } else if (SharedPrefManager.getInstance(this).getToken()!=null) {
            startActivity(new Intent(SplashActivity.this, CreateAccountActivity.class).putExtra("PROGRESS", true));
            finish();
        } else
            startActivity(new Intent(SplashActivity.this, EmailActivity.class));
    }

}