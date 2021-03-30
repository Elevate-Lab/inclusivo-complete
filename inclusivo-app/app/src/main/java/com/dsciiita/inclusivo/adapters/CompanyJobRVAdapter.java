package com.dsciiita.inclusivo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.responses.CityResponse;

import java.util.ArrayList;
import java.util.List;

public class CompanyJobRVAdapter extends RecyclerView.Adapter<CompanyJobRVAdapter.ViewHolder>{

    List<Job> jobList;
    List<List<CityResponse>> locationResponses;
    Context context;
    private final onJobListener mOnNoteListener;
    LayoutInflater inflater;

    public CompanyJobRVAdapter(@NonNull Context context, List<Job> jobList, onJobListener mOnNoteListener) {
        this.context = context;
        this.jobList = new ArrayList<>();
        this.jobList = jobList;
        inflater = LayoutInflater.from(context);
        locationResponses = new ArrayList<>();
        this.mOnNoteListener = mOnNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_jobs_rv_item, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    public void updateAdapter(List<Job> mDataList) {
        this.jobList = mDataList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.jobTitle.setText(jobList.get(position).getTitle());
        holder.jobVacancy.setText(String.valueOf(jobList.get(position).getVacancies()));
        holder.jobRole.setText(jobList.get(position).getJobRole());
        holder.jobType.setText(jobList.get(position).getJobType());
        String location = "";
        for(Job job: jobList)
            locationResponses.add(job.getLocations());
        List<CityResponse> response = locationResponses.get(position);
        if(response!=null) {
            for (CityResponse cityResponse : response)
                location = cityResponse.getName() + ", " + location;
        }
        holder.jobLocations.setText(location);
    }

    @Override
    public int getItemCount() {
        return jobList == null ? 0 : jobList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView jobTitle, jobRole, jobType, jobLocations, jobVacancy;
        onJobListener noteListener;

         public ViewHolder(View itemView, onJobListener onJobListener){
             super(itemView);
             this.jobLocations = itemView.findViewById(R.id.location_txt);
             this.jobTitle = itemView.findViewById(R.id.job_title);
             this.jobType = itemView.findViewById(R.id.job_type_txt);
             this.jobRole = itemView.findViewById(R.id.job_role_txt);
             this.jobVacancy = itemView.findViewById(R.id.vacancy_value_txt);
             this.noteListener = onJobListener;
             itemView.setOnClickListener(this);
             itemView.setOnLongClickListener(this);
         }

        @Override
        public void onClick(View v) {
            noteListener.onJobClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View view) {
            noteListener.onJobLongClick(getLayoutPosition());
            return true;
        }
    }

    public interface onJobListener{
        void onJobClick(int position, View v);
        void onJobLongClick(int position);
    }
}
