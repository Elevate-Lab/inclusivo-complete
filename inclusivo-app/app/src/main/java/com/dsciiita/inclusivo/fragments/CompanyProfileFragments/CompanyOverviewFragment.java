package com.dsciiita.inclusivo.fragments.CompanyProfileFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.activities.CompanyProfileActivity;
import com.dsciiita.inclusivo.activities.InitiativeInfoActivity;
import com.dsciiita.inclusivo.activities.JobDescriptionActivity;
import com.dsciiita.inclusivo.activities.ScholarshipDescriptionActivity;
import com.dsciiita.inclusivo.activities.StoryInfoActivity;
import com.dsciiita.inclusivo.adapters.CompanyInitiativeRVAdapter;
import com.dsciiita.inclusivo.adapters.CompanyJobRVAdapter;
import com.dsciiita.inclusivo.adapters.CompanyStoryRVAdapter;
import com.dsciiita.inclusivo.adapters.ScholarshipRVAdapter;
import com.dsciiita.inclusivo.databinding.FragmentCompanyOverviewBinding;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.Initiative;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.viewmodel.CompanyViewModel;

import java.util.List;

public class CompanyOverviewFragment extends Fragment implements CompanyJobRVAdapter.onJobListener, CompanyInitiativeRVAdapter.onInitiativeListener,
        CompanyStoryRVAdapter.onStoryListener, ScholarshipRVAdapter.OnclickListener {

    private CompanyViewModel viewModel;
    private Company company;
    private FragmentCompanyOverviewBinding binding;

    private CompanyJobRVAdapter jobAdapter;
    private List<Job> jobsList;

    private CompanyInitiativeRVAdapter initiativeAdapter;
    private List<Initiative> initiativeList;

    private CompanyStoryRVAdapter storyAdapter;
    private List<Story> storyList;

    private ScholarshipRVAdapter scholarshipRVAdapter;
    private List<Scholarship> scholarshipList;

    public CompanyOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompanyOverviewBinding.inflate(inflater, container, false);

        binding.seeAllJobs.setOnClickListener(this::onClick);
        binding.seeAllInitiatives.setOnClickListener(this::onClick);
        binding.readMoreTxt.setOnClickListener(this::onClick);
        binding.seeAllStories.setOnClickListener(this::onClick);
        binding.seeAllScholarships.setOnClickListener(this::onClick);
        return binding.getRoot();
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(CompanyViewModel.class);
        viewModel.getText().observe(getActivity(), company -> {
            this.company = company;
        });

        jobAdapter = new CompanyJobRVAdapter(getContext(), jobsList, this);
        binding.jobsRv.setAdapter(jobAdapter);

        initiativeAdapter = new CompanyInitiativeRVAdapter(getContext(), initiativeList, this);
        binding.initiativesRv.setAdapter(initiativeAdapter);

        storyAdapter = new CompanyStoryRVAdapter(getContext(), storyList, this);
        binding.storiesRv.setAdapter(storyAdapter);

        scholarshipRVAdapter = new ScholarshipRVAdapter(getContext(), R.layout.company_overview_scholarship_rv_item, scholarshipList, this);
        binding.scholarshipsRv.setAdapter(scholarshipRVAdapter);


        viewModel.getJobList().observe(getActivity(), jobs -> {
            jobsList = jobs;
            if (jobsList != null) {
                if(jobsList.isEmpty())
                    setGone(binding.jobTxt, binding.jobsRv, binding.seeAllJobs);
                else if(jobsList.size() >= 3)
                    jobsList = jobsList.subList(0, 3);
            }
            jobAdapter.updateAdapter(jobsList);
        });



        viewModel.getInitiatives().observe(getActivity(), initiatives -> {
            initiativeList = initiatives;
            if(initiativeList != null) {
                if(initiativeList.isEmpty())
                    setGone(binding.initiativetxt, binding.initiativesRv, binding.seeAllInitiatives);
                else if(initiativeList.size()>=3)
                    initiativeList = initiativeList.subList(0, 3);

            }
            initiativeAdapter.updateAdapter(initiativeList);
        });


        viewModel.getStories().observe(getActivity(), stories -> {
            storyList = stories;
            if (storyList != null) {
                if(storyList.isEmpty())
                    setGone(binding.storyTxt, binding.storiesRv, binding.seeAllStories);
                else if(storyList.size() >= 3)
                    storyList = storyList.subList(0, 3);
            }
            storyAdapter.updateAdapter(storyList);
        });


        viewModel.getScholarships().observe(getActivity(), scholarships -> {
            scholarshipList = scholarships;
            if (scholarshipList != null) {
                if(scholarshipList.isEmpty())
                    setGone(binding.scholarshipTxt, binding.scholarshipsRv, binding.seeAllScholarships);
                else if(scholarshipList.size() >= 3)
                    scholarshipList = scholarshipList.subList(0, 3);
            }
            scholarshipRVAdapter.updateAdapter(scholarshipList);
        });


        setValues();

    }


    public void onClick(View view){
        if( view.getId() == R.id.see_all_jobs) {
            ((CompanyProfileActivity)getActivity()).goto_fragment(3);
        } else if( view.getId() == R.id.read_more_txt) {
            ((CompanyProfileActivity)getActivity()).goto_fragment(1);
        } else if( view.getId() == R.id.see_all_initiatives) {
            ((CompanyProfileActivity)getActivity()).goto_fragment(2);
        } else if (view.getId() == R.id.see_all_stories) {
            ((CompanyProfileActivity) getActivity()).goto_fragment(4);
        } else if (view.getId() == R.id.see_all_scholarships) {
            ((CompanyProfileActivity) getActivity()).goto_fragment(5);
        }
    }


    public void openLink(View view){
        String url = (String) view.getTag();
        if(!url.isEmpty()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    private void setValues() {
        binding.companyDescription.setText(company.getDescription());
        binding.companyWebsiteTxt.setText(company.getWebsite());
        binding.companySizeTxt.setText(company.getSize());
        binding.companyAddressTxt.setText(company.getAddress());
        binding.contactNumTxt.setText(company.getPhoneNumber());
        binding.emailTxt.setText(company.getEmail());

        if (company.getFacebook() != null)
            addLink(binding.facebook, company.getFacebook());

        if (company.getTwitter() != null)
            addLink(binding.twitter, company.getTwitter());

        if (company.getLinkedin() != null)
            addLink(binding.linkedIn, company.getLinkedin());

        if (company.getInstagram() != null)
            addLink(binding.instagram, company.getInstagram());

    }


    private void addLink(View view, String url){
        view.setVisibility(View.VISIBLE);
        if(url.isEmpty()) {
            view.setVisibility(View.GONE);
            return;
        }
        if(!url.startsWith("https://"))
            url = "https://"+url;
        view.setTag(url);
        view.setOnClickListener(this::openLink);
    }


    private void setGone(View... views){
        for(View view: views)
            view.setVisibility(View.GONE);
    }

    @Override
    public void onJobClick(int position, View v) {
        startActivity(new Intent(getActivity(), JobDescriptionActivity.class).putExtra("id", jobsList.get(position).getJobId()));
    }

    @Override
    public void onJobLongClick(int position) {

    }

    @Override
    public void onInitiativeClick(int position, View v) {
        startActivity(new Intent(getActivity(), InitiativeInfoActivity.class).putExtra("id", initiativeList.get(position).getId()));
    }

    @Override
    public void onInitiativeLongClick(int position) {

    }

    @Override
    public void onStoryClick(int position, View v) {
        startActivity(new Intent(getActivity(), StoryInfoActivity.class).putExtra("id", storyList.get(position).getId()));
    }

    @Override
    public void onStoryLongClick(int position) {

    }

    @Override
    public void onClick(int position, View v) {
        startActivity(new Intent(getContext(), ScholarshipDescriptionActivity.class).putExtra("id", scholarshipList.get(position).getId()));
    }

    @Override
    public void onLongClick(int position) {

    }
}