package com.dsciiita.inclusivo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dsciiita.inclusivo.models.Scholarship;

public class ScholarshipViewModel extends ViewModel {
    private final MutableLiveData<Scholarship> scholarship = new MutableLiveData<>();

    public Scholarship getScholarship() {
        return scholarship.getValue();
    }

    public void setScholarship(Scholarship scholarship) {
        this.scholarship.setValue(scholarship);
    }
}
