package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityEmailBinding;
import com.dsciiita.inclusivo.responses.EmailResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailActivity extends AppCompatActivity {

    private ActivityEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.loginContinueBtn.setOnClickListener(this::onClick);
    }


    /*---------------------- API CALLS ----------------------*/

    private void checkUser() {
        binding.loginContinueBtn.setEnabled(false);

        binding.tilEmail.setError(null);

        String email = binding.tilEmail.getEditText().getText().toString().toLowerCase().trim();

        if(email.isEmpty()){
            binding.loginContinueBtn.setEnabled(true);
            binding.tilEmail.setError("Email required");
            return;
        }


        binding.progressBar.setVisibility(View.VISIBLE);
        Call<EmailResponse> userRequestCall = ApiClient.getUserService().checkUser(email);
        userRequestCall.enqueue(new Callback<EmailResponse>() {
            @Override
            public void onResponse(Call<EmailResponse> call, Response<EmailResponse> response) {
                EmailResponse emailResponse = response.body();

                if(response.isSuccessful()){
                    if(emailResponse.getData().getIsUser().equals("true")) {
                        startActivity(new Intent(EmailActivity.this, LoginActivity.class).putExtra("email", email));
                    }
                    else
                        startActivity(new Intent(EmailActivity.this, CreateAccountActivity.class).putExtra("email", email));
                    finish();
                }else{
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.GONE);
                binding.loginContinueBtn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<EmailResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.loginContinueBtn.setEnabled(true);
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    /*---------------------- EVENTS HANDLING ----------------------*/

    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_continue_btn:
                checkUser();
        }
    }


}
