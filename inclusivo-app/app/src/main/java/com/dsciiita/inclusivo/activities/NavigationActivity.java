package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.ActivityNavigationBinding;
import com.dsciiita.inclusivo.fragments.Dashboard.DashboardJobFragment;
import com.dsciiita.inclusivo.fragments.Dashboard.CompaniesFragment;
import com.dsciiita.inclusivo.fragments.Dashboard.ScholarshipsFragment;
import com.dsciiita.inclusivo.fragments.Dashboard.StoriesFragment;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.models.UserEmployee;
import com.dsciiita.inclusivo.responses.GetUserResponse;
import com.dsciiita.inclusivo.responses.UserTypeResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityNavigationBinding mainBinding;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityNavigationBinding.inflate(LayoutInflater.from(this));
        setContentView(mainBinding.getRoot());

        getUser();
        setUpSideNavigation();
        setUpBottomNavigation();
    }


    private void setUpSideNavigation(){

        navigationView = findViewById(R.id.main_nav_view);

        setSupportActionBar(mainBinding.contentLayout.toolbarMain);
        getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mainBinding.drawer,
                mainBinding.contentLayout.toolbarMain,
                R.string.drawer_open, R.string.drawer_close);
        mainBinding.drawer.addDrawerListener(toggle);

        toggle.syncState();

        mainBinding.mainNavView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.my_account_menu) {
            if(SharedPrefManager.getInstance(this).isEmployer())
                startActivity(new Intent(this, EmployerProfileActivity.class));
            else
                startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.saved_jobs_menu) {
            startActivity(new Intent(this, SavedJobsActivity.class));
        } else if (id == R.id.saved_scholarships_menu) {
            startActivity(new Intent(this, SavedScholarshipActivity.class));
        } else if(id == R.id.subs_companies_menu) {
            startActivity(new Intent(this, FollowedCompaniesActivity.class));
        } else if(id == R.id.applied_jobs) {
            startActivity(new Intent(this, AppliedJobsActivity.class));
        } else if(id == R.id.my_company) {
            startActivity(new Intent(this, CompanyProfileActivity.class)
                    .putExtra("companyID", SharedPrefManager.getInstance(this).getCompanyID()));
        } else if(id == R.id.logout_menu) {
            SharedPrefManager.getInstance(this).clear();
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NavigationActivity.this, EmailActivity.class));
            finish();
        }
        mainBinding.drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setUpBottomNavigation() {
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, new DashboardJobFragment()).commit();
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.job_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.job_menu:
                    fragment = new DashboardJobFragment();
                    break;
                case R.id.companies_menu:
                    fragment = new CompaniesFragment();
                    break;
                case R.id.scholarship_menu:
                    fragment = new ScholarshipsFragment();
                    break;
                case R.id.stories_menu:
                    fragment = new StoriesFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
            return true;
        });
    }

    private void getUser(){
        String token  = "token "+  SharedPrefManager.getInstance(this).getToken();

        Call<GetUserResponse> userRequestCall = ApiClient.getUserService().getUser(token);
        userRequestCall.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if(response.isSuccessful()) {
                    UserTypeResponse user = response.body().getData();
                    String userData = new Gson().toJson(user);
                    if (user.isEmployer()) {
                        updateEmployerUI(user);
                        SharedPrefManager.getInstance(NavigationActivity.this).setEmployer(true);
                    } else {
                        updateCandidateUI(user);
                        SharedPrefManager.getInstance(NavigationActivity.this).setEmployer(false);
                    }
                    Log.d("Checking the Type :", "" + user.isEmployer() + " SharedPrefValue : " + SharedPrefManager.getInstance(NavigationActivity.this).isEmployer());
                }
            }
            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                Toast.makeText(NavigationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCandidateUI(UserTypeResponse user){
        UserCandidate candidate = user.getCandidate();
        navigationView.getMenu().findItem(R.id.my_company).setVisible(false);
        navigationView.getMenu().findItem(R.id.applied_jobs).setVisible(true);
        navigationView.getMenu().findItem(R.id.saved_scholarships_menu).setVisible(true);
        navigationView.getMenu().findItem(R.id.saved_jobs_menu).setVisible(true);
        navigationView.getMenu().findItem(R.id.subs_companies_menu).setVisible(true);
        if(candidate.getUser()!=null)
            setValues(candidate.getUser().getProfileUrl());
        SharedPrefManager.getInstance(NavigationActivity.this).saveCompanyID(-1);
    }

    private void updateEmployerUI(UserTypeResponse user){
        UserEmployee userEmployee = user.getEmployee();
        navigationView.getMenu().findItem(R.id.my_company).setVisible(true);
        navigationView.getMenu().findItem(R.id.applied_jobs).setVisible(false);
        navigationView.getMenu().findItem(R.id.saved_scholarships_menu).setVisible(false);
        navigationView.getMenu().findItem(R.id.saved_jobs_menu).setVisible(false);
        navigationView.getMenu().findItem(R.id.subs_companies_menu).setVisible(false);
        if(userEmployee.getUser()!=null)
            setValues(userEmployee.getUser().getProfileUrl());
        SharedPrefManager.getInstance(NavigationActivity.this).saveCompanyID(userEmployee.getCompany().getId());
    }

    private void setValues(String url) {
        CircleImageView imageView = findViewById(R.id.user_profile_img);
        if (imageView != null)
            Glide.with(getApplicationContext()).load(url)
                    .placeholder(R.drawable.profile_default)
                    .into(imageView);
    }

    @Override
    public void onBackPressed() {
        if (mainBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawer.closeDrawer(GravityCompat.START);
        } else if (bottomNavigationView.getSelectedItemId() != R.id.job_menu) {
            bottomNavigationView.setSelectedItemId(R.id.job_menu);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new DashboardJobFragment()).commit();
        } else {
            super.onBackPressed();
        }
    }
}