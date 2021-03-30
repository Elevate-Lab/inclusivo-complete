package com.dsciiita.inclusivo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dsciiita.inclusivo.fragments.JobDescriptionFragments.JobAboutFragment;
import com.dsciiita.inclusivo.fragments.JobDescriptionFragments.JobMoreInfoFragment;
import com.dsciiita.inclusivo.fragments.JobDescriptionFragments.JobOverviewFragment;
import com.dsciiita.inclusivo.fragments.JobDescriptionFragments.JobRecruitmentFragment;

public class JobDescriptionViewPagerAdapter extends FragmentStateAdapter {


    public JobDescriptionViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new JobOverviewFragment();
                break;
            case 1:
                fragment = new JobAboutFragment();
                break;
            case 2:
                fragment = new JobRecruitmentFragment();
                break;
            case 3:
                fragment = new JobMoreInfoFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
