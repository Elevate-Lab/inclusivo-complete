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
import com.dsciiita.inclusivo.databinding.ActivityLoginPasswordBinding;
import com.dsciiita.inclusivo.responses.GetUserResponse;
import com.dsciiita.inclusivo.responses.LoginResponse;
import com.dsciiita.inclusivo.responses.UserTypeResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPasswordBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        String email = getIntent().getStringExtra("email");
        binding.tilEmail.getEditText().setText(email);

        binding.btnLogin.setOnClickListener(this::onClick);
        setValidationTextWatcher();
    }



    /*---------------------- API CALLS ----------------------*/

    public void loginUser() {
        binding.btnLogin.setEnabled(false);

        String email = binding.tilEmail.getEditText().getText().toString().trim();
        String password = binding.tilPassword.getEditText().getText().toString().trim();

        if (!validateEmail(email)) {
            binding.btnLogin.setEnabled(true);
            return;
        }
        if (!validatePassword(password)) {
            binding.btnLogin.setEnabled(true);
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        Call<LoginResponse> userRequestCall = ApiClient.getUserService().loginUser(email, password);
        userRequestCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    if (response.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(LoginActivity.this).saveToken(loginResponse.getId());
                        getUser();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            showErrors(jObjError);
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                binding.btnLogin.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*---------------------- EVENT HANDLING ----------------------*/

    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            loginUser();
        }
    }


    /*---------------------- VALIDATIONS ----------------------*/

    private void getUser() {
        String token = "token " + SharedPrefManager.getInstance(this).getToken();

        Call<GetUserResponse> userRequestCall = ApiClient.getUserService().getUser(token);
        userRequestCall.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.isSuccessful()) {
                    UserTypeResponse user = response.body().getData();
                    SharedPrefManager.getInstance(LoginActivity.this).setEmployer(user.isEmployer());
                    startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class).putExtra("PROGRESS", true)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.tilPassword.setError("Password is required !");
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

    private class ValidationTextWatcher implements TextWatcher {
        private final View view;

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
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void setValidationTextWatcher() {
        binding.tilPassword.getEditText().addTextChangedListener(new ValidationTextWatcher(binding.tilPassword.getEditText()));
    }


    private void showErrors(JSONObject error) {
        if(error.has("non_field_errors")){
            if(error.optString("non_field_errors").contains("verified")){
                Toast.makeText(LoginActivity.this, "Verify email to login", Toast.LENGTH_SHORT).show();
            } else if(error.optString("non_field_errors").contains("Unable to log in with provided credentials."))
                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}