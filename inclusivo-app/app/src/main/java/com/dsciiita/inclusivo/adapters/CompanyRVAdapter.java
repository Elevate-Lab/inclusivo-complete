package com.dsciiita.inclusivo.adapters;

import android.content.Context;
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
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class CompanyRVAdapter extends RecyclerView.Adapter<CompanyRVAdapter.ViewHolder>{

    List<Company> list;
    Context context;
    private final onCompanyListener listener;
    LayoutInflater inflater;


    public CompanyRVAdapter(@NonNull Context context, List<Company> list, onCompanyListener listener) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.companies_rv_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getName());
        holder.size.setText(String.valueOf(list.get(position).getSize()));
        holder.profile.setText(list.get(position).getProfile());
        holder.shortCode.setText(list.get(position).getShortCode());

        Glide.with(context).load(list.get(position).getLogoUrl())
                .placeholder(Constants.PLACEHOLDER_IMAGE)
                .into(holder.companyImg);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void updateAdapter(List<Company> mDataList) {
        this.list = mDataList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, profile, shortCode, size;
        ImageView companyImg;
        onCompanyListener companyListener;

         public ViewHolder(View itemView, onCompanyListener onCompanyListener){
             super(itemView);
             this.size = itemView.findViewById(R.id.company_size);
             this.title = itemView.findViewById(R.id.company_title);
             this.shortCode = itemView.findViewById(R.id.company_short_code);
             this.profile = itemView.findViewById(R.id.company_profile);
             this.companyImg = itemView.findViewById(R.id.company_logo);
             this.companyListener = onCompanyListener;
             itemView.setOnClickListener(this);
             itemView.setOnLongClickListener(this);
         }

        @Override
        public void onClick(View v) {
            companyListener.onCompanyClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View view) {
            companyListener.onCompanyLongClick(getLayoutPosition());
            return true;
        }
    }

    public interface onCompanyListener{
        void onCompanyClick(int position, View v);
        void onCompanyLongClick(int position);
    }
}
