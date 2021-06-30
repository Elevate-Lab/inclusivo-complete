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
import com.dsciiita.inclusivo.models.JobApplication;
import com.dsciiita.inclusivo.models.UserCandidate;

import java.util.List;
import java.util.List;

public class JobApplicationsRVAdapter extends RecyclerView.Adapter<JobApplicationsRVAdapter.ViewHolder>{

    List<JobApplication> objectList;
    Context context;
    private final onClickListener listener;
    LayoutInflater inflater;

    public JobApplicationsRVAdapter(@NonNull Context context, List<JobApplication> objectList, onClickListener listener) {
        this.context = context;
        this.objectList = objectList;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applications_rv_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        UserCandidate candidate = objectList.get(position).getCandidate();
        String candName = candidate.getUser().getFirstName()+" "+ candidate.getUser().getLastName();
        holder.name.setText(candName);
        holder.jobRole.setText(candidate.getJobRole());
        holder.desc.setText(candidate.getProfileDesc());

        String status = objectList.get(position).getStatus();
        holder.status.setText(status);
        switch (status) {
            case "Selected":
                holder.status.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case "Shortlisted":
                holder.status.setTextColor(context.getResources().getColor(R.color.shortlisted_name));
                break;
            case "Pending":
                holder.status.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case "Process":
                holder.status.setTextColor(context.getResources().getColor(R.color.process_text));
                break;
            case "Rejected":
                holder.status.setTextColor(context.getResources().getColor(R.color.rejected_text));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return objectList == null ? 0 : objectList.size();
    }

    public void updateAdapter(List<JobApplication> mDataList) {
        this.objectList = mDataList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView name, jobRole, desc, status;
        onClickListener listener;

         public ViewHolder(View itemView, onClickListener onClickListener){
             super(itemView);
             this.name = itemView.findViewById(R.id.candi_name);
             this.jobRole = itemView.findViewById(R.id.candi_job_role);
             this.desc = itemView.findViewById(R.id.candi_profile_desc);
             this.status = itemView.findViewById(R.id.status);
             listener = onClickListener;
             itemView.setOnClickListener(this);
             itemView.setOnLongClickListener(this);
         }

        @Override
        public void onClick(View v) {
            listener.onClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onLongClick(getLayoutPosition());
            return true;
        }
    }

    public interface onClickListener{
        void onClick(int position, View v);
        void onLongClick(int position);
    }
}
