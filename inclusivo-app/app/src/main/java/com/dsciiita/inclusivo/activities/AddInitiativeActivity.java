package com.dsciiita.inclusivo.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityAddInitiativeBinding;
import com.dsciiita.inclusivo.models.Initiative;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInitiativeActivity extends AppCompatActivity {

    ActivityAddInitiativeBinding binding;

    private HashMap<String, String> genderMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddInitiativeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(view->finish());

        binding.btnAdd.setOnClickListener(view->{
            addInitiative();
            setResult(RESULT_OK, new Intent());
        });

        genderMap = Constants.buildMap();
        setValidationTextWatcher();
    }


    private void addInitiative() {

        binding.btnAdd.setEnabled(false);

        String token = "token "+ SharedPrefManager.getInstance(this).getToken();

        String name = binding.tilInitiativeName.getEditText().getText().toString().trim();
        String desc = binding.tilInitiativeDescription.getEditText().getText().toString().trim();

        if(name.isEmpty()){
            binding.tilInitiativeName.setError("Name required");
            binding.btnAdd.setEnabled(true);
            return;
        }

        if(desc.isEmpty()){
            binding.tilInitiativeDescription.setError("Description required");
            binding.btnAdd.setEnabled(true);
            return;
        }

        Initiative initiative = new Initiative(desc, name);
        binding.progressBar.setVisibility(View.VISIBLE);

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().addInitiative(initiative, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddInitiativeActivity.this, "Initiative created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Snackbar.make(binding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }

                binding.btnAdd.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnAdd.setEnabled(true);
                Snackbar.make(binding.parent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private class ValidationTextWatcher implements TextWatcher {
        private View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.initiative_description_tie:
                    String[] text = binding.tilInitiativeDescription.getEditText().getText().toString().split(" ");
                    String lastWord, twoWords;
                    if(text.length==0)
                        lastWord = "";
                    else
                        lastWord = text[text.length-1];

                    if(text.length<=1)
                        twoWords = "";
                    else
                        twoWords = text[text.length-2]+" "+text[text.length-1];

                    lastWord = lastWord.toLowerCase();
                    twoWords = twoWords.toLowerCase();

                    if(genderMap.containsKey(lastWord))
                        binding.tilInitiativeDescription.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(lastWord)+"' instead of '"+lastWord+"'");
                    else if(genderMap.containsKey(twoWords))
                        binding.tilInitiativeDescription.getEditText().setError("Use gender inclusive terms like '"+genderMap.get(twoWords)+"' instead of '"+twoWords+"'");
                    else
                        binding.tilInitiativeDescription.getEditText().setError(null);
                    break;
            }
        }
    }

    private void setValidationTextWatcher() {
        binding.tilInitiativeDescription.getEditText().addTextChangedListener(new ValidationTextWatcher(binding.tilInitiativeDescription.getEditText()));
    }

}