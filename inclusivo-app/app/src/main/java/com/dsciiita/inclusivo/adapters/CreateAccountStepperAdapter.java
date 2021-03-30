package com.dsciiita.inclusivo.adapters;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.dsciiita.inclusivo.fragments.LoginFragment;
import com.dsciiita.inclusivo.fragments.RegisterFragment;
import com.dsciiita.inclusivo.fragments.TellUsMoreCandidateFragment;
import com.dsciiita.inclusivo.fragments.TellUsMoreEmployeeFragment;
import com.dsciiita.inclusivo.fragments.UpdateProfileFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class CreateAccountStepperAdapter extends AbstractFragmentStepAdapter {

    //dynamically adding fragment based upon role
    int count = 4;

    public CreateAccountStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        Step fragment = null;
        count = 4;
        switch (position) {
            case 0:
                fragment = new RegisterFragment();
                break;
            case 1:
                fragment = new LoginFragment();
                break;
            case 2:
                fragment = new UpdateProfileFragment();
                break;
            case 3:
                fragment = new TellUsMoreCandidateFragment();
                break;
            case 4:
                //add employee fragment instead of candidate
                count = 5;
                fragment = new TellUsMoreEmployeeFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle(null) //can be a CharSequence instead
                .create();
    }
}
