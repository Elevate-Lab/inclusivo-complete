package com.dsciiita.inclusivo.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.CreateAccountActivity;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentRegisterBinding;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment implements Step {
    private FragmentRegisterBinding registerFragmentBinding;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerFragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        registerFragmentBinding.btnRegister.setOnClickListener(this::onClick);

        String email = ((CreateAccountActivity) getActivity()).getEmail();
        registerFragmentBinding.tilEmail.getEditText().setText(email);
        setupHyperlink();
        return registerFragmentBinding.getRoot();
    }

    private void setupHyperlink() {
        registerFragmentBinding.consent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }


    /*---------------------- API CALLS ----------------------*/

    public void registerUser(){

        registerFragmentBinding.btnRegister.setEnabled(false);
        String email = registerFragmentBinding.tilEmail.getEditText().getText().toString().toLowerCase().trim();
        String password = registerFragmentBinding.tilPassword.getEditText().getText().toString().trim();
        String confPassword = registerFragmentBinding.tilConfPassword.getEditText().getText().toString().trim();

        if (!validateEmail(email)) {
            registerFragmentBinding.btnRegister.setEnabled(true);
            return;
        }
        if (!validatePassword(password)) {
            registerFragmentBinding.btnRegister.setEnabled(true);
            return;
        }
        if(!validateConfirmPassword(confPassword, password)){
            registerFragmentBinding.btnRegister.setEnabled(true);
            return;
        }

        ((CreateAccountActivity) getActivity()).showProgressBar();

        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().registerUser(email, password, confPassword);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                LoginFragment.binding.tilEmail.getEditText().setText(email);
                LoginFragment.binding.tilPassword.getEditText().setText(password);
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Verification email sent successfully", Toast.LENGTH_SHORT).show();
                    ((CreateAccountActivity) getActivity()).replaceFragments(1);
                }else{
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                ((CreateAccountActivity) getActivity()).hideProgressBar();
                registerFragmentBinding.btnRegister.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                ((CreateAccountActivity) getActivity()).hideProgressBar();
                registerFragmentBinding.btnRegister.setEnabled(true);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*---------------------- EVENTS HANDLING ----------------------*/

    public void onClick(View view) {
        if(view.getId() == R.id.btnRegister){
            registerUser();
        }
    }


    /*---------------------- VALIDATIONS ----------------------*/

    private void showErrors(JSONObject error) {
        if(error.has("email"))
            if(error.optString("email").contains("already registered")){
                Toast.makeText(getContext(), "Account already exists. Login instead", Toast.LENGTH_SHORT).show();
                ((CreateAccountActivity) getActivity()).replaceFragments(1);
            }
            registerFragmentBinding.tilEmail.setError(error.optString("email"));
        if(error.has("password1"))
            registerFragmentBinding.tilPassword.setError(error.optString("password1"));
        if(error.has("password2"))
            registerFragmentBinding.tilConfPassword.setError(error.optString("password2"));
        if(error.has("non_field_errors")) {
            registerFragmentBinding.tilConfPassword.setError(error.optString("non_field_errors"));
            registerFragmentBinding.tilPassword.setError(error.optString("non_field_errors"));
        }
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
                case R.id.password_tie:
                    validatePassword(registerFragmentBinding.tilPassword.getEditText().getText().toString().trim());
                    break;
                case R.id.email_tie:
                    validateEmail(registerFragmentBinding.tilEmail.getEditText().getText().toString().trim());
                    break;
                case R.id.tie_cnf_pwd:
                    validateConfirmPassword(registerFragmentBinding.tilConfPassword.getEditText().getText().toString().trim(),
                            registerFragmentBinding.tilPassword.getEditText().getText().toString().trim());
            }
        }
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            registerFragmentBinding.tilPassword.setError("Password is required !");
            requestFocus(registerFragmentBinding.tilPassword.getEditText());
            return false;
        } else if (password.length() < 6) {
            registerFragmentBinding.tilPassword.setError("Password can't be less than 8 characters");
            requestFocus(registerFragmentBinding.tilPassword.getEditText());
            return false;
        } else {
            registerFragmentBinding.tilPassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            registerFragmentBinding.tilEmail.setError("Email is required !");
            requestFocus(registerFragmentBinding.tilEmail.getEditText());
            return false;
        } else {
            Boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (!isValid) {
                registerFragmentBinding.tilEmail.setError("Invalid Email address, ex: abc@example.com");
                requestFocus(registerFragmentBinding.tilEmail.getEditText());
                return false;
            } else {
                LoginFragment.binding.tilEmail.getEditText().setText(email);
                LoginFragment.binding.tilPassword.getEditText().setText(email);
                registerFragmentBinding.tilEmail.setErrorEnabled(false);
            }
        }
        return true;
    }

    private boolean validateConfirmPassword(String confirmPassword, String password) {
        if (confirmPassword.isEmpty()) {
            registerFragmentBinding.tilConfPassword.setError("Please enter your password again to confirm !");
            requestFocus(registerFragmentBinding.tilConfPassword.getEditText());
            return false;
        } else if (!confirmPassword.equals(password)) {
            registerFragmentBinding.tilConfPassword.setError("The two password fields didn't match.");
            requestFocus(registerFragmentBinding.tilConfPassword.getEditText());
            return false;
        } else {
            registerFragmentBinding.tilConfPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void setValidationTextWatcher() {
        registerFragmentBinding.tilPassword.getEditText().addTextChangedListener(new ValidationTextWatcher(registerFragmentBinding.tilPassword.getEditText()));
        registerFragmentBinding.tilConfPassword.getEditText().addTextChangedListener(new ValidationTextWatcher(registerFragmentBinding.tilConfPassword.getEditText()));
    }
}