package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.databinding.ActivityEvaluationResultsBinding;
import com.dsciiita.inclusivo.models.EvaluationData;
import com.google.gson.Gson;

import java.util.List;

public class EvaluationResults extends AppCompatActivity {

    ActivityEvaluationResultsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEvaluationResultsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        EvaluationData data = new Gson().fromJson(getIntent().getStringExtra("data"), EvaluationData.class);


        if(data.getMatchingRate()>0.75)
            binding.rate.setTextColor(getResources().getColor(R.color.green));
        else if(data.getMatchingRate()>0.35)
            binding.rate.setTextColor(getResources().getColor(R.color.yellow));
        else
            binding.rate.setTextColor(getResources().getColor(R.color.red));

        int matchingRatPercent = (int) (data.getMatchingRate()*100);
        binding.rate.setText("Matching rate: "+matchingRatPercent+" %");
        List<String> matchingSkills = data.getMatchingSkills();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, matchingSkills);
        binding.matchList.setAdapter(adapter);


    }
}