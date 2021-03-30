package com.dsciiita.inclusivo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dsciiita.inclusivo.fragments.ScholarshipDescriptionFragments.ScholarshipAboutFragment;
import com.dsciiita.inclusivo.fragments.ScholarshipDescriptionFragments.ScholarshipMoreInfoFragment;
import com.dsciiita.inclusivo.fragments.ScholarshipDescriptionFragments.ScholarshipOverviewFragment;
import com.dsciiita.inclusivo.fragments.ScholarshipDescriptionFragments.ScholarshipSelectionFragment;

public class ScholarshipDescriptionViewPagerAdapter extends FragmentStateAdapter {
    public ScholarshipDescriptionViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ScholarshipOverviewFragment();
                break;
            case 1:
                fragment = new ScholarshipAboutFragment();
                break;
            case 2:
                fragment = new ScholarshipSelectionFragment();
                break;
            case 3:
                fragment = new ScholarshipMoreInfoFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
