package com.dsciiita.inclusivo.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.CreateAccountStepperAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityCreateAccountBinding;
import com.dsciiita.inclusivo.fragments.RegisterFragment;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.models.UserEmployee;
import com.dsciiita.inclusivo.responses.GetUserResponse;
import com.dsciiita.inclusivo.responses.UserTypeResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.gson.Gson;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity implements StepperLayout.StepperListener {
    private ActivityCreateAccountBinding activityCreateAccountBinding;
    private CreateAccountStepperAdapter createAccountStepperAdapter;

    private int currStep;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateAccountBinding = ActivityCreateAccountBinding.inflate(LayoutInflater.from(this));
        setContentView(activityCreateAccountBinding.getRoot());
        createAccountStepperAdapter = new CreateAccountStepperAdapter(getSupportFragmentManager(), this);
        activityCreateAccountBinding.stepperLayout.setAdapter(createAccountStepperAdapter);
        activityCreateAccountBinding.stepperLayout.setListener(this);
        activityCreateAccountBinding.stepperLayout.setTabNavigationEnabled(false);
        currStep = 0;

        email = getIntent().getStringExtra("email");

        if(getIntent().hasExtra("PROGRESS")){
            activityCreateAccountBinding.stepperLayout.setVisibility(View.INVISIBLE);
            activityCreateAccountBinding.progressBar.setVisibility(View.VISIBLE);
            activityCreateAccountBinding.verifyTxt.setVisibility(View.VISIBLE);
            activityCreateAccountBinding.noConnection.setVisibility(View.GONE);
            getUser();
        }
    }



    private void getUser() {

        if(!isConnection()) {
            activityCreateAccountBinding.stepperLayout.setVisibility(View.GONE);
            activityCreateAccountBinding.noConnection.setVisibility(View.VISIBLE);
            activityCreateAccountBinding.animation.playAnimation();
            activityCreateAccountBinding.progressBar.setVisibility(View.GONE);
            activityCreateAccountBinding.verifyTxt.setVisibility(View.GONE);
            return;
        }


        String token = "token " + SharedPrefManager.getInstance(this).getToken();

        Call<GetUserResponse> userRequestCall = ApiClient.getUserService().getUser(token);
        userRequestCall.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.isSuccessful()) {
                    UserTypeResponse user = response.body().getData();
                    if(user.isUpdated()) {
                        if (user.isEmployer()) {
                            UserEmployee employee = user.getEmployee();
                            if (employee.getMobile() == null || employee.getMobile().isEmpty()) {
                                activityCreateAccountBinding.stepperLayout.setCurrentStepPosition(3);
                                replaceFragments(4);
                                activityCreateAccountBinding.stepperLayout.setVisibility(View.VISIBLE);
                            }
                            else {
                                startActivity(new Intent(CreateAccountActivity.this, NavigationActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        } else{
                            UserCandidate candidate = user.getCandidate();
                            if (candidate.getMobile() == null || candidate.getMobile().isEmpty()) {
                                replaceFragments(3);
                                activityCreateAccountBinding.stepperLayout.setVisibility(View.VISIBLE);
                            }
                            else {
                                startActivity(new Intent(CreateAccountActivity.this, NavigationActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        }
                    } else {
                        activityCreateAccountBinding.stepperLayout.setCurrentStepPosition(2);
                        activityCreateAccountBinding.stepperLayout.setVisibility(View.VISIBLE);
                    }
                } else{
                    activityCreateAccountBinding.stepperLayout.setCurrentStepPosition(1);
                    activityCreateAccountBinding.stepperLayout.setVisibility(View.VISIBLE);
                    activityCreateAccountBinding.progressBar.setVisibility(View.GONE);
                    activityCreateAccountBinding.verifyTxt.setVisibility(View.GONE);
                }
//                activityCreateAccountBinding.progressBar.setVisibility(View.GONE);
//                activityCreateAccountBinding.verifyTxt.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                activityCreateAccountBinding.progressBar.setVisibility(View.GONE);
                activityCreateAccountBinding.verifyTxt.setVisibility(View.GONE);
                Toast.makeText(CreateAccountActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String getEmail(){
        return email;
    }

    //go to next step
    public void replaceFragments(int nextStep) {
        //adding extra fragment
        if(nextStep==4) {
            createAccountStepperAdapter.createStep(nextStep);
            createAccountStepperAdapter.notifyDataSetChanged();
        }
        currStep = nextStep;
        activityCreateAccountBinding.stepperLayout.setCurrentStepPosition(nextStep);
    }

    public void showProgressBar(){
        activityCreateAccountBinding.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        activityCreateAccountBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {
    }

    @Override
    public void onReturn() {

    }

    @Override
    public void onBackPressed() {
            finish();
    }

    public boolean isConnection(){
        ConnectivityManager connectivityManager = ((ConnectivityManager) CreateAccountActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}