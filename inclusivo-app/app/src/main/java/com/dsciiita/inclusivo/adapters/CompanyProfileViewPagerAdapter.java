package com.dsciiita.inclusivo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dsciiita.inclusivo.fragments.CompanyProfileFragments.CompanyAboutFragment;
import com.dsciiita.inclusivo.fragments.CompanyProfileFragments.CompanyInitiativesFragment;
import com.dsciiita.inclusivo.fragments.CompanyProfileFragments.CompanyJobsFragment;
import com.dsciiita.inclusivo.fragments.CompanyProfileFragments.CompanyOverviewFragment;
import com.dsciiita.inclusivo.fragments.CompanyProfileFragments.CompanyScholarshipFragment;
import com.dsciiita.inclusivo.fragments.CompanyProfileFragments.CompanyStoriesFragment;

public class CompanyProfileViewPagerAdapter extends FragmentStateAdapter {
    public CompanyProfileViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new CompanyOverviewFragment();
                break;
            case 1:
                fragment = new CompanyAboutFragment();
                break;
            case 2:
                fragment = new CompanyInitiativesFragment();
                break;
            case 3:
                fragment = new CompanyJobsFragment();
                break;
            case 4:
                fragment = new CompanyStoriesFragment();
                break;
            case 5:
                fragment = new CompanyScholarshipFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
