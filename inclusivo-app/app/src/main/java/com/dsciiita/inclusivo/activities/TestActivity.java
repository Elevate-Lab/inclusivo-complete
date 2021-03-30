package com.dsciiita.inclusivo.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.fragments.TellUsMoreCandidateFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportFragmentManager().beginTransaction().add(R.id.test_fragment_container, new TellUsMoreCandidateFragment()).commit();
    }
}