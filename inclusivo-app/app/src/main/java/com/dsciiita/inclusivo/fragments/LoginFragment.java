package com.dsciiita.inclusivo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.CreateAccountActivity;
import com.dsciiita.inclusivo.activities.LoginActivity;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentLoginPasswordBinding;
import com.dsciiita.inclusivo.responses.LoginResponse;
import com.dsciiita.inclusivo.models.User;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements Step {
    public static FragmentLoginPasswordBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginPasswordBinding.inflate(inflater, container, false);
        binding.btnLogin.setOnClickListener(this::onClick);
        return binding.getRoot();
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

    private void loginUser() {
        binding.btnLogin.setEnabled(false);

        ((CreateAccountActivity) getActivity()).showProgressBar();
        String email = binding.tilEmail.getEditText().getText().toString().toLowerCase().trim();
        String password = binding.tilPassword.getEditText().getText().toString().trim();
        Call<LoginResponse> userRequestCall = ApiClient.getUserService().loginUser(email, password);
        userRequestCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (response.isSuccessful()) {
                    UpdateProfileFragment.updateProfileFragmentBinding.tilEmail.getEditText().setText(email);
                    Toast.makeText(getContext(), "Logged In Successfully", Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getActivity()).saveToken(loginResponse.getId());
                    ((CreateAccountActivity) getActivity()).replaceFragments(2);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        showErrors(jObjError);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                ((CreateAccountActivity) getActivity()).hideProgressBar();
                binding.btnLogin.setEnabled(true);
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                ((CreateAccountActivity) getActivity()).hideProgressBar();
                binding.btnLogin.setEnabled(true);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*---------------------- EVENTS HANDLING ----------------------*/

    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            loginUser();
        }
    }


    /*---------------------- VALIDATIONS ----------------------*/

    private void showErrors(JSONObject error) {
        if(error.has("non_field_errors")){
            if(error.optString("non_field_errors").contains("verified")){
                Toast.makeText(getContext(), "Verify email to login", Toast.LENGTH_SHORT).show();
            } else if(error.optString("non_field_errors").contains("Unable to log in with provided credentials."))
                Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}