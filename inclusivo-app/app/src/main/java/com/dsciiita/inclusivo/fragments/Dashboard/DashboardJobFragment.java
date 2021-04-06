package com.dsciiita.inclusivo.fragments.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.AppliedJobsActivity;
import com.dsciiita.inclusivo.activities.CompanyProfileActivity;
import com.dsciiita.inclusivo.activities.FollowedCompaniesActivity;
import com.dsciiita.inclusivo.activities.JobApplicationsActivity;
import com.dsciiita.inclusivo.activities.JobDescriptionActivity;
import com.dsciiita.inclusivo.activities.JobListingActivity;
import com.dsciiita.inclusivo.activities.SavedJobsActivity;
import com.dsciiita.inclusivo.activities.ViewJobApplicationActivity;
import com.dsciiita.inclusivo.adapters.AppliedJobsRV;
import com.dsciiita.inclusivo.adapters.DashboardCircleTagsRVAdapter;
import com.dsciiita.inclusivo.adapters.DashboardCompanyInfoRVAdapter;
import com.dsciiita.inclusivo.adapters.DashboardJobItemsRVAdapter;
import com.dsciiita.inclusivo.adapters.JobApplicationsRVAdapter;
import com.dsciiita.inclusivo.adapters.JobRVAdapter;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.databinding.FragmentCandidateDashboardBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.FollowedCompanyData;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.JobApplication;
import com.dsciiita.inclusivo.models.JobApplicationByCandidateData;
import com.dsciiita.inclusivo.models.LikedJobs;
import com.dsciiita.inclusivo.models.TitleLinkObject;
import com.dsciiita.inclusivo.responses.ApplicationByCandidate;
import com.dsciiita.inclusivo.responses.ApplicationsByJobResponse;
import com.dsciiita.inclusivo.responses.CompanyJobsResponse;
import com.dsciiita.inclusivo.responses.FollowedCompaniesResponse;
import com.dsciiita.inclusivo.responses.LikedJobsResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dsciiita.inclusivo.storage.Constants.BENGALURU_URL;
import static com.dsciiita.inclusivo.storage.Constants.CHENNAI_URL;
import static com.dsciiita.inclusivo.storage.Constants.DELHI_URL;
import static com.dsciiita.inclusivo.storage.Constants.FEMALE_URL;
import static com.dsciiita.inclusivo.storage.Constants.GURGAON_URL;
import static com.dsciiita.inclusivo.storage.Constants.LGBTQI_URL;
import static com.dsciiita.inclusivo.storage.Constants.MUMBAI_URL;
import static com.dsciiita.inclusivo.storage.Constants.PUNE_URL;
import static com.dsciiita.inclusivo.storage.Constants.SPECIALLY_ABLED_URL;
import static com.dsciiita.inclusivo.storage.Constants.VETERAN_URL;
import static com.dsciiita.inclusivo.storage.Constants.WORKING_MOTHER_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardJobFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;



    private DashboardCompanyInfoRVAdapter companyAdapter;
    private DashboardJobItemsRVAdapter citiesRVAdapter;
    private DashboardCircleTagsRVAdapter tagAdapter;
    private JobRVAdapter prefCityAdapter, savedJobAdapter, companyJobAdapter;
    private AppliedJobsRV appliedJobsAdapter;
    private List<Job> savedJobList, jobsPrefCities, companyJobs;
    private List<Company> companyList;
    private List<JobApplicationByCandidateData> appliedJobs;
    private List<TitleLinkObject> popularCities, tagList;
    private String token, companyName;

    private int jobID;
    private List<JobApplication> applications;
    private JobApplicationsRVAdapter applicationAdapter;


    private int companyJobCount, currJob = 0;

    FragmentCandidateDashboardBinding binding;

    public DashboardJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CandidateDashboardFragment.
     */
    public static DashboardJobFragment newInstance(String param1, String param2) {
        DashboardJobFragment fragment = new DashboardJobFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCandidateDashboardBinding.inflate(inflater);

        setupAdapters();
        return binding.getRoot();
    }


    private void setupAdapters(){
        binding.viewAllPreferredCitiesJobs.setOnClickListener(this::onClick);
        binding.viewAllSavedJobs.setOnClickListener(this::onClick);
        binding.viewAllPriority.setOnClickListener(this::onClick);
        binding.viewAllSubscribedCompanies.setOnClickListener(this::onClick);
        binding.viewAllJobApplication.setOnClickListener(this::onClick);
        binding.btnCompany.setOnClickListener(this::onClick);

        token = "token "+ SharedPrefManager.getInstance(getActivity()).getToken();

        companyAdapter = new DashboardCompanyInfoRVAdapter(getContext(), companyList, companyListener);
        binding.subscribedCompaniesRv.setAdapter(companyAdapter);

        citiesRVAdapter = new DashboardJobItemsRVAdapter(getContext(), R.layout.job_cities_rv_item, popularCities, cityListener);
        binding.jobCitiesRv.setAdapter(citiesRVAdapter);

        tagAdapter = new DashboardCircleTagsRVAdapter(getContext(), R.layout.job_tags_rv_item, tagList, tagListener);
        binding.dashboardTagsRv.setAdapter(tagAdapter);

        prefCityAdapter = new JobRVAdapter(getContext(), jobsPrefCities, prefCityListener);
        binding.jobsPreferredCitiesRv.setAdapter(prefCityAdapter);

        savedJobAdapter = new JobRVAdapter(getContext(), savedJobList, savedJobListener);
        binding.savedJobsRvRemovable.setAdapter(savedJobAdapter);


        applicationAdapter = new JobApplicationsRVAdapter(getActivity(), applications, applicationsListener);
        binding.yourJobsApplicationsRv.setAdapter(applicationAdapter);
        binding.refreshLayout.setOnRefreshListener(this::getData);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {

        currJob = 0;

        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.prent.setVisibility(View.GONE);
        binding.prent.setAlpha(0);
        binding.errorView.setVisibility(View.GONE);
        binding.subsCompanyLayout.setVisibility(View.GONE);

        getPopularCities();
        getTags();

        if(SharedPrefManager.getInstance(getActivity()).isEmployer()) {
            //company jobs
            binding.prefCityJobsLayout.setVisibility(View.GONE);
            binding.savedJobLayout.setVisibility(View.GONE);
            binding.subsCompanyLayout.setVisibility(View.GONE);
            binding.btnCompany.setText("My Company");

            binding.priorityHeading.setText("Browse your recent jobs");
            companyJobAdapter = new JobRVAdapter(getContext(), companyJobs, companyJobsListener);
            binding.priorityJobRv.setAdapter(companyJobAdapter);
            getCompanyJobs();
        } else {
            getJobsByPrefCity();
            binding.prefCityJobsLayout.setVisibility(View.VISIBLE);
            binding.savedJobLayout.setVisibility(View.VISIBLE);
            binding.yourJobsApplicationsLayout.setVisibility(View.GONE);
            binding.btnCompany.setText("View all jobs");

            appliedJobsAdapter = new AppliedJobsRV(getContext(), appliedJobs, appliedJobListener);
            binding.priorityJobRv.setAdapter(appliedJobsAdapter);
            binding.priorityHeading.setText("Applied jobs");
            getAppliedJobs();
            getFollowedCompanies();
            getSavedJobs();
        }

    }

    private void getAppliedJobs() {
        appliedJobs = new ArrayList<>();
        appliedJobs.clear();
        Call<ApplicationByCandidate> userRequestCall = ApiClient.getUserService().getAppliedJobs(token);
        userRequestCall.enqueue(new Callback<ApplicationByCandidate>() {
            @Override
            public void onResponse(Call<ApplicationByCandidate> call, Response<ApplicationByCandidate> response) {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                if(response.isSuccessful()) {
                    binding.prent.setVisibility(View.VISIBLE);
                    binding.prent.animate().alpha(1).setDuration(300);
                    appliedJobs = response.body().getData();
                    if(appliedJobs.isEmpty())
                        binding.priorityCategory.setVisibility(View.GONE);
                    else {
                        Collections.sort(appliedJobs, (obj1, obj2) -> obj2.getApplicationDate().compareToIgnoreCase(obj1.getApplicationDate()));
                        if (appliedJobs.size() >= 2)
                            appliedJobs = appliedJobs.subList(0, 2);
                        appliedJobsAdapter.updateAdapter(appliedJobs);

                        binding.priorityCategory.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.priorityCategory.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ApplicationByCandidate> call, Throwable t) {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.priorityCategory.setVisibility(View.GONE);
            }
        });
    }


    private void getCompanyJobs() {
        companyJobs = new ArrayList<>();
        companyJobs.clear();
        currJob = 0;

        int companyId = SharedPrefManager.getInstance(getActivity()).getCompanyID();

        Call<CompanyJobsResponse> userRequestCall = ApiClient.getUserService().getJobsForCompany(companyId, token);
        userRequestCall.enqueue(new Callback<CompanyJobsResponse>() {
            @Override
            public void onResponse(Call<CompanyJobsResponse> call, Response<CompanyJobsResponse> response) {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                if(response.isSuccessful()) {
                    binding.prent.setVisibility(View.VISIBLE);
                    binding.prent.animate().alpha(1).setDuration(300);
                    companyJobs = response.body().getData();
                    if(companyJobs.isEmpty())
                        binding.priorityCategory.setVisibility(View.GONE);
                    else {
                        companyName = companyJobs.get(0).getCompany().getName();
                        companyJobCount = companyJobs.get(0).getCompany().getJobCount();
                        getApplicationForAJob(companyJobs.get(currJob++));
                        Collections.sort(companyJobs, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));
                        List<Job> reducedJobs = companyJobs;
                        if (companyJobs.size() >= 2)
                            reducedJobs = companyJobs.subList(0, 2);
                        companyJobAdapter.updateAdapter(reducedJobs);

                        binding.priorityCategory.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.priorityCategory.setVisibility(View.GONE);

                }
            }
            @Override
            public void onFailure(Call<CompanyJobsResponse> call, Throwable t) {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.refreshLayout.setRefreshing(false);
                binding.priorityCategory.setVisibility(View.GONE);
            }
        });
    }

    private void getApplicationForAJob(Job job) {
            String jobTitle = job.getTitle();
            binding.applicationForJob.setText("Applications for " + jobTitle);
            jobID = job.getJobId();
            getApplications(jobID);
    }


    private void getApplications(int jobID){
        binding.applicationProgress.setVisibility(View.VISIBLE);
        applications = new ArrayList<>();
        applications.clear();
        applicationAdapter.updateAdapter(applications);

        String token  = "token "+ SharedPrefManager.getInstance(getActivity()).getToken();
        Call<ApplicationsByJobResponse> userRequestCall = ApiClient.getUserService().getApplicationsForJob(jobID, token);
        userRequestCall.enqueue(new Callback<ApplicationsByJobResponse>() {
            @Override
            public void onResponse(Call<ApplicationsByJobResponse> call, Response<ApplicationsByJobResponse> response) {
                if(response.isSuccessful()) {
                    applications = response.body().getData();
                    if (applications.isEmpty()) {
                        if(currJob<companyJobCount && currJob<companyJobs.size())
                            getApplicationForAJob(companyJobs.get(currJob++));
                        else
                            binding.yourJobsApplicationsLayout.setVisibility(View.GONE);
                    } else {
                        Collections.sort(applications, (obj1, obj2) -> obj2.getApplicationDate().compareToIgnoreCase(obj1.getApplicationDate()));
                        if (applications.size() >= 2)
                            applications = applications.subList(0, 2);
                        applicationAdapter.updateAdapter(applications);
                        binding.applicationProgress.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<ApplicationsByJobResponse> call, Throwable t) {
                Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getTags() {
        tagList = new ArrayList<>();
        tagList.add(new TitleLinkObject("Female", FEMALE_URL));
        tagList.add(new TitleLinkObject("LGBTQI",LGBTQI_URL));
        tagList.add(new TitleLinkObject("Veteran",VETERAN_URL));
        tagList.add(new TitleLinkObject("Working Mother",WORKING_MOTHER_URL));
        tagList.add(new TitleLinkObject("Specially Abled",SPECIALLY_ABLED_URL));

        tagAdapter.updateAdapter(tagList);
    }

    private void getPopularCities() {
        popularCities = new ArrayList<>();

        popularCities.add(new TitleLinkObject("Delhi",DELHI_URL));
        popularCities.add(new TitleLinkObject("Mumbai",MUMBAI_URL));
        popularCities.add(new TitleLinkObject("Bangalore",BENGALURU_URL));
        popularCities.add(new TitleLinkObject("Pune",PUNE_URL));
        popularCities.add(new TitleLinkObject("Chennai",CHENNAI_URL));
        popularCities.add(new TitleLinkObject("Gurgaon",GURGAON_URL));

        citiesRVAdapter.updateAdapter(popularCities);
    }


    private void getSavedJobs() {
        savedJobList = new ArrayList<>();

        Call<LikedJobsResponse> userRequestCall = ApiClient.getUserService().getLikedJobs(token);
        userRequestCall.enqueue(new Callback<LikedJobsResponse>() {
            @Override
            public void onResponse(Call<LikedJobsResponse> call, Response<LikedJobsResponse> response) {
                if(response.isSuccessful()) {
                    List<LikedJobs> likedJobsResponse = response.body().getData();
                    for(LikedJobs object: likedJobsResponse){
                        object.getJobPost().setLiked(true);
                        savedJobList.add(object.getJobPost());
                    }
                    if(savedJobList.isEmpty())
                        binding.savedJobLayout.setVisibility(View.GONE);
                    else {
                        binding.savedJobLayout.setVisibility(View.VISIBLE);
                        Collections.sort(savedJobList, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));
                        savedJobAdapter.updateAdapter(savedJobList);
                    }
                } else {
                    Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LikedJobsResponse> call, Throwable t) {
                Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void getFollowedCompanies() {
        companyList = new ArrayList<>();
        companyList.clear();

        Call<FollowedCompaniesResponse> userRequestCall = ApiClient.getUserService().getFollowedCompanies(token);
        userRequestCall.enqueue(new Callback<FollowedCompaniesResponse>() {
            @Override
            public void onResponse(Call<FollowedCompaniesResponse> call, Response<FollowedCompaniesResponse> response) {
                if(response.isSuccessful()) {
                    List<FollowedCompanyData> companiesResponse = response.body().getData();
                    for(FollowedCompanyData data: companiesResponse){
                        companyList.add(data.getCompanyModel().getCompany());
                    }
                    if(companyList.isEmpty())
                        binding.subsCompanyLayout.setVisibility(View.GONE);
                    else {
                        if (companyList.size() >= 5)
                            companyList = companyList.subList(0, 5);
                        binding.subsCompanyLayout.setVisibility(View.VISIBLE);
                        companyAdapter.notifyItemRangeInserted(0, companyList.size());
                        companyAdapter.updateAdapter(companyList);
                    }
                } else {
                    Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<FollowedCompaniesResponse> call, Throwable t) {
                Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    private void getJobsByPrefCity() {
        jobsPrefCities = new ArrayList<>();

        Call<CompanyJobsResponse> userRequestCall = ApiClient.getUserService().getJobsByPrefCity(token);
        userRequestCall.enqueue(new Callback<CompanyJobsResponse>() {
            @Override
            public void onResponse(Call<CompanyJobsResponse> call, Response<CompanyJobsResponse> response) {
                if(response.isSuccessful()) {
                    jobsPrefCities = response.body().getData();
                    if(jobsPrefCities.isEmpty())
                        binding.prefCityJobsLayout.setVisibility(View.GONE);
                    else {
                        binding.prefCityJobsLayout.setVisibility(View.VISIBLE);
                        Collections.sort(jobsPrefCities, (obj1, obj2) -> obj2.getPostedOn().compareToIgnoreCase(obj1.getPostedOn()));
                        if (jobsPrefCities.size() >= 2)
                            jobsPrefCities = jobsPrefCities.subList(0, 2);
                        prefCityAdapter.updateAdapter(jobsPrefCities);
                    }
                } else {
                    Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CompanyJobsResponse> call, Throwable t) {
                Snackbar.make(binding.prent, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    private void onClick(View view){
        int id = view.getId();
        if(id==R.id.view_all__preferred_cities_jobs){
            startActivity(new Intent(getActivity(), JobListingActivity.class).putExtra("filter", "preferred_cities"));
        } else if(id==R.id.view_all_saved_jobs){
            startActivity(new Intent(getActivity(), SavedJobsActivity.class));
        } else if(id == R.id.view_all_subscribed_companies){
            startActivity(new Intent(getActivity(), FollowedCompaniesActivity.class));
        } else if(id == R.id.view_all_job_application){
            startActivity(new Intent(getActivity(), JobApplicationsActivity.class).putExtra("id", jobID));
        } else if(id == R.id.view_all_priority){
            if(!SharedPrefManager.getInstance(getActivity()).isEmployer())
                startActivity(new Intent(getActivity(), AppliedJobsActivity.class));
            else
                startActivity(new Intent(getActivity(), JobListingActivity.class).putExtra("company_name",  companyName));
        } else if(id == R.id.btnCompany){
            if(SharedPrefManager.getInstance(getActivity()).isEmployer())
                startActivity(new Intent(getActivity(), CompanyProfileActivity.class)
                        .putExtra("companyID", SharedPrefManager.getInstance(getActivity()).getCompanyID()));
            else
                startActivity(new Intent(getActivity(), JobListingActivity.class).putExtra("no_filter",  ""));
        }
    }


    DashboardCompanyInfoRVAdapter.onCompanyListener companyListener = new DashboardCompanyInfoRVAdapter.onCompanyListener() {
        @Override
        public void onCompanyClick(int position, View v) {
            startActivity(new Intent(getActivity(), JobListingActivity.class).putExtra("company_name", companyList.get(position).getName()));
        }

        @Override
        public void onCompanyLongClick(int position) {

        }
    };


    DashboardJobItemsRVAdapter.onCompanyListener cityListener = new DashboardJobItemsRVAdapter.onCompanyListener() {
        @Override
        public void onCompanyClick(int position, View v) {
            startActivity(new Intent(getActivity(), JobListingActivity.class).putExtra("locations", popularCities.get(position).getTitle()));
        }

        @Override
        public void onCompanyLongClick(int position) {

        }
    };


    DashboardCircleTagsRVAdapter.onCompanyListener tagListener = new DashboardCircleTagsRVAdapter.onCompanyListener() {
        @Override
        public void onCompanyClick(int position, View v) {
            startActivity(new Intent(getActivity(), JobListingActivity.class).putExtra("tags", tagList.get(position).getTitle()));
        }

        @Override
        public void onCompanyLongClick(int position) {

        }
    };


    JobRVAdapter.onJobListener prefCityListener = new JobRVAdapter.onJobListener() {
        @Override
        public void onJobClick(int position, View v) {
            startActivity(new Intent(getActivity(), JobDescriptionActivity.class).putExtra("id", jobsPrefCities.get(position).getJobId()));
        }

        @Override
        public void onJobLongClick(int position) {

        }
    };


    JobRVAdapter.onJobListener savedJobListener = new JobRVAdapter.onJobListener() {
        @Override
        public void onJobClick(int position, View v) {
            startActivity(new Intent(getActivity(), JobDescriptionActivity.class).putExtra("id", savedJobList.get(position).getJobId()));
        }

        @Override
        public void onJobLongClick(int position) {

        }
    };



    JobRVAdapter.onJobListener companyJobsListener = new JobRVAdapter.onJobListener() {
        @Override
        public void onJobClick(int position, View v) {
            startActivity(new Intent(getActivity(), JobDescriptionActivity.class).putExtra("id", companyJobs.get(position).getJobId()));
        }

        @Override
        public void onJobLongClick(int position) {

        }
    };



    AppliedJobsRV.onJobListener appliedJobListener = new AppliedJobsRV.onJobListener() {
        @Override
        public void onJobClick(int position, View v) {
            startActivity(new Intent(getActivity(), JobDescriptionActivity.class).putExtra("id", appliedJobs.get(position).getJob().getJobId()));
        }

        @Override
        public void onJobLongClick(int position) {

        }
    };

    JobApplicationsRVAdapter.onClickListener applicationsListener = new JobApplicationsRVAdapter.onClickListener() {
        @Override
        public void onClick(int position, View v) {
            startActivity(new Intent(getActivity(), ViewJobApplicationActivity.class).putExtra("applicationId", applications.get(position).getId()));
        }

        @Override
        public void onLongClick(int position) {

        }
    };

}