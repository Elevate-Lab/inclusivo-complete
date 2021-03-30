package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityRegisterBinding;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(this::onClick);
        binding.btnLogin.setOnClickListener(this::onClick);
        setValidationTextWatcher();

    }


    /*---------------------- API CALLS ----------------------*/

    public void registerUser(){

        binding.btnRegister.setEnabled(false);

        String email = binding.tilEmail.getEditText().getText().toString();
        String password = binding.tilPassword.getEditText().getText().toString();
        String confPassword = binding.tilConfPassword.getEditText().getText().toString();

        if (!validateEmail(email)) {
            binding.btnRegister.setEnabled(true);
            return;
        }
        if (!validatePassword(password)) {
            binding.btnRegister.setEnabled(true);
            return;
        }
        if(!validateConfirmPassword(confPassword)){
            binding.btnRegister.setEnabled(true);
            return;
        }

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().registerUser(email, password, confPassword);

        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    finish();
                }else{
                    Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
                binding.btnRegister.setEnabled(true);
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                binding.btnRegister.setEnabled(true);
                Snackbar.make(binding.parentLayout, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    /*---------------------- EVENT HANDLING ----------------------*/

    public void onClick(View view){
        if(view.getId()==R.id.btnLogin) {
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            finish();
        } else if(view.getId()==R.id.btnRegister){
            registerUser();
        }
    }


    /*---------------------- VALIDATIONS ----------------------*/

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
                case R.id.password_tie:
                    validatePassword(binding.tilPassword.getEditText().getText().toString().trim());
                    break;
                case R.id.email_tie:
                    validateEmail(binding.tilEmail.getEditText().getText().toString().trim());
                    break;
                case R.id.tie_cnf_pwd:
                    validateConfirmPassword(binding.tilConfPassword.getEditText().getText().toString().trim());
            }
        }
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.tilPassword.setError("Password is required !");
            requestFocus(binding.tilPassword.getEditText());
            return false;
        } else if (password.length() < 6) {
            binding.tilPassword.setError("Password can't be less than 6 characters");
            requestFocus(binding.tilPassword.getEditText());
            return false;
        } else {
            binding.tilPassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            binding.tilEmail.setError("Email is required !");
            requestFocus(binding.tilEmail.getEditText());
            return false;
        } else {
            Boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (!isValid) {
                binding.tilEmail.setError("Invalid Email address, ex: abc@example.com");
                requestFocus(binding.tilEmail.getEditText());
                return false;
            } else {
                binding.tilEmail.setErrorEnabled(false);
            }
        }
        return true;
    }

    private boolean validateConfirmPassword(String confirmPassword) {
        if (confirmPassword.isEmpty()) {
            binding.tilConfPassword.setError("Please enter your password again to confirm !");
            requestFocus(binding.tilConfPassword.getEditText());
            return false;
        } else if (confirmPassword.length() < 6) {
            binding.tilConfPassword.setError("Password can't be less than 6 characters");
            requestFocus(binding.tilConfPassword.getEditText());
            return false;
        } else {
            binding.tilConfPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void setValidationTextWatcher() {
        binding.tilPassword.getEditText().addTextChangedListener(new RegistrationActivity.ValidationTextWatcher(binding.tilPassword.getEditText()));
        binding.tilConfPassword.getEditText().addTextChangedListener(new RegistrationActivity.ValidationTextWatcher(binding.tilConfPassword.getEditText()));
    }
}