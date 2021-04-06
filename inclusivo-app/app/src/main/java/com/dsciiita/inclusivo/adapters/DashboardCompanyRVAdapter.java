package com.dsciiita.inclusivo.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.storage.Constants;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardCompanyRVAdapter extends RecyclerView.Adapter<DashboardCompanyRVAdapter.ViewHolder>{

    List<Company> objectList;
    Context context;
    private final OnclickListener clickListener;
    LayoutInflater inflater;
    RecyclerView recyclerView;

    public DashboardCompanyRVAdapter(@NonNull Context context, List<Company> objectList, OnclickListener clickListener) {
        this.context = context;
        this.objectList = objectList;
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_company_rv_item, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(objectList.get(position).getName());
        holder.size.setText(objectList.get(position).getSize());
        int jobs = objectList.get(position).getJobCount();
        if(jobs==1)
            holder.jobCount.setText("1 live job");
        else
            holder.jobCount.setText(jobs+" live jobs");


        if(objectList.get(position).getLogoUrl()!=null)
            Glide.with(context).load(objectList.get(position).getLogoUrl())
                .placeholder(R.drawable.ic_companies)
                .circleCrop()
                .into(holder.profile);
        else
            Glide.with(context).load(R.drawable.ic_companies)
                    .circleCrop()
                    .into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return objectList == null ? 0 : objectList.size();
    }

    public void updateAdapter(List<Company> mDataList) {
        this.objectList = mDataList;
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, jobCount, size;
        ImageView profile;
        OnclickListener clickListener;

         public ViewHolder(View itemView, OnclickListener clickListener){
             super(itemView);
             this.title = itemView.findViewById(R.id.title);
             this.size = itemView.findViewById(R.id.size);
             this.profile = itemView.findViewById(R.id.display_img);
             this.jobCount = itemView.findViewById(R.id.job_count);
             this.clickListener = clickListener;
             itemView.setOnClickListener(this);
             itemView.setOnLongClickListener(this);
         }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onLongClick(getLayoutPosition());
            return true;
        }
    }

    public interface OnclickListener{
        void onClick(int position, View v);
        void onLongClick(int position);
    }

}
