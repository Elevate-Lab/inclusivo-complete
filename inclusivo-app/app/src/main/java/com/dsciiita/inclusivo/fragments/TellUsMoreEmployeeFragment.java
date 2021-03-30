package com.dsciiita.inclusivo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.AddCompanyActivity;
import com.dsciiita.inclusivo.activities.CreateAccountActivity;
import com.dsciiita.inclusivo.activities.NavigationActivity;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentTellUsMoreEmployeeBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.responses.CompanyListsResponse;
import com.dsciiita.inclusivo.models.User;
import com.dsciiita.inclusivo.models.UserEmployee;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TellUsMoreEmployeeFragment extends Fragment implements Step {
    FragmentTellUsMoreEmployeeBinding binding;

    private String refVia;
    private ArrayList<String> companyTitles;
    private ArrayList<Company> companies;

    public TellUsMoreEmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTellUsMoreEmployeeBinding.inflate(inflater, container, false);
        binding.btnSubmit.setOnClickListener(this::onClick);
        binding.btnAddYours.setOnClickListener(this::onClick);
        binding.companyIdAct.addTextChangedListener(new ValidationTextWatcher(binding.companyIdAct));
        binding.btnSubmit.setOnClickListener(this::onClick);
        binding.refViaEmployee.setOnItemSelectedListener(myListener);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        getCompanies();
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getContext(),
                android.R.layout.select_dialog_item, companyTitles);
        binding.companyIdAct.setThreshold(1); //will start working from first character
        binding.companyIdAct.setAdapter(adapter);
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

    private void getCompanies() {
        companyTitles = new ArrayList<>();
        companies = new ArrayList<>();

        String token = "token "+ SharedPrefManager.getInstance(getActivity()).getToken();

        Call<CompanyListsResponse> userRequestCall = ApiClient.getUserService().getCompanies(token);
        userRequestCall.enqueue(new Callback<CompanyListsResponse>() {
            @Override
            public void onResponse(Call<CompanyListsResponse> call, Response<CompanyListsResponse> response) {
                if(response.isSuccessful()) {
                    CompanyListsResponse companyListsResponse = response.body();
                    Company[] array = companyListsResponse.getData();
                    for (Company value : array) {
                        companies.add(value);
                        Log.i("Company ", value.getTitle());
                        companyTitles.add(value.getTitle());
                    }
                    Log.i("SIZES", companyTitles.size()+"");
                } else
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<CompanyListsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void completeEmployee() {
        binding.btnSubmit.setEnabled(false);

        binding.tilAlternateContactNumber.setError(null);
        binding.tilContactNumber.setError(null);

        String company = binding.tilCompanyId.getEditText().getText().toString();
        String mobile = binding.tilContactNumber.getEditText().getText().toString().trim();
        String alternateMobile = binding.tilAlternateContactNumber.getEditText().getText().toString();

        if(!checkValidCompanyId(company)) {
            binding.btnSubmit.setEnabled(true);
            return;
        }

        if(mobile.isEmpty()) {
            binding.tilContactNumber.setError("Contact number required");
            binding.btnSubmit.setEnabled(true);
            return;
        }

        if(alternateMobile.isEmpty()) {
            binding.tilAlternateContactNumber.setError("Alternate contact number required");
            binding.btnSubmit.setEnabled(true);
            return;
        }

        //get company details
        int index = companyTitles.indexOf(company);
        Log.i("COMPANY", index+"");
        if(index==-1){
            Toast.makeText(getContext(), "Invalid company. Add company first", Toast.LENGTH_SHORT).show();
            binding.btnSubmit.setEnabled(true);
            return;
        }
        int companyId = companies.get(index).getId();


        UserEmployee userEmployee = new UserEmployee(new User(), companyId, mobile, refVia, alternateMobile);
        String token = "token "+ SharedPrefManager.getInstance(getActivity()).getToken();

        ((CreateAccountActivity) getActivity()).showProgressBar();

        Call<UserEmployee> userRequestCall = ApiClient.getUserService().completeEmployee(userEmployee, token);
        userRequestCall.enqueue(new Callback<UserEmployee>() {
            @Override
            public void onResponse(Call<UserEmployee> call, Response<UserEmployee> response) {
                if(response.isSuccessful()) {
                    SharedPrefManager.getInstance(getActivity()).saveCompanyID(companyId);
                    Toast.makeText(getContext(), "Profile completed successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), NavigationActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else
                    Snackbar.make(binding.parent, "Something went wrong. Try again later.", BaseTransientBottomBar.LENGTH_SHORT).show();
                ((CreateAccountActivity) getActivity()).hideProgressBar();

                binding.btnSubmit.setEnabled(true);
            }
            @Override
            public void onFailure(Call<UserEmployee> call, Throwable t) {
                ((CreateAccountActivity) getActivity()).hideProgressBar();
                binding.btnSubmit.setEnabled(true);
                Snackbar.make(binding.parent, "Something went wrong. Try again later.", BaseTransientBottomBar.LENGTH_SHORT).show();

            }
        });
    }



    /*---------------------- EVENTS HANDLING ----------------------*/

    private void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            completeEmployee();
        } else if (view.getId()==R.id.btnAddYours){
            startActivity(new Intent(getContext(), AddCompanyActivity.class));
        }
    }

    AdapterView.OnItemSelectedListener myListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
            int id = parent.getId();
            switch (id) {
                case R.id.ref_via_employee:
                    refVia = binding.refViaEmployee.getItemAtPosition(position).toString();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };



    /*---------------------- VALIDATIONS ----------------------*/

    private boolean checkValidCompanyId(String company) {
        if (TextUtils.isEmpty(company)) {
            binding.tilCompanyId.setError("Company ID is required !");
            requestFocus(binding.companyIdAct);
            return false;
        } else {
            binding.tilCompanyId.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.company_id_act:
                    checkValidCompanyId(binding.companyIdAct.getText().toString().trim());
                    break;
            }
        }
    }
}