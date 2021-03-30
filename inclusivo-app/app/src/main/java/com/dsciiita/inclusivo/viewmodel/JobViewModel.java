package com.dsciiita.inclusivo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dsciiita.inclusivo.models.Job;


public class JobViewModel extends ViewModel {

    private MutableLiveData<Job> job = new MutableLiveData<>();

    public void setJob(Job job) {
        this.job.setValue(job);
    }
    public Job getJob() {
        return job.getValue();
    }

}