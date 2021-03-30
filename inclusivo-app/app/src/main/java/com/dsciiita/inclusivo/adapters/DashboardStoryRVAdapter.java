package com.dsciiita.inclusivo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.storage.Constants;

import java.util.List;

public class DashboardStoryRVAdapter extends RecyclerView.Adapter<DashboardStoryRVAdapter.ViewHolder>{

    List<Story> objectList;
    Context context;
    private final OnclickListener clickListener;
    LayoutInflater inflater;
    RecyclerView recyclerView;

    public DashboardStoryRVAdapter(@NonNull Context context, List<Story> objectList, OnclickListener clickListener) {
        this.context = context;
        this.objectList = objectList;
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_stories_rv_item, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(objectList.get(position).getName());
        holder.companyName.setText(objectList.get(position).getCompany().getName());
        holder.description.setText(objectList.get(position).getDescription());

        Glide.with(context).load(objectList.get(position).getPhotoUrl())
                .placeholder(R.drawable.ic_stories)
                .optionalCenterInside()
                .into(holder.storyImage);

    }

    @Override
    public int getItemCount() {
        return objectList == null ? 0 : objectList.size();
    }

    public void updateAdapter(List<Story> mDataList) {
        this.objectList = mDataList;
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, companyName, description;
        ImageView storyImage;
        OnclickListener clickListener;

        public ViewHolder(View itemView, OnclickListener clickListener) {
            super(itemView);
            this.companyName = itemView.findViewById(R.id.company_name);
            this.storyImage = itemView.findViewById(R.id.story_img);
            this.title = itemView.findViewById(R.id.story_name);
            this.description = itemView.findViewById(R.id.story_description);
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
