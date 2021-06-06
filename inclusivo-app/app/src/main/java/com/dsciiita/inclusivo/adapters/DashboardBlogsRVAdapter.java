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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.models.Blog;
import com.dsciiita.inclusivo.models.Diversity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class DashboardBlogsRVAdapter extends RecyclerView.Adapter<DashboardBlogsRVAdapter.ViewHolder>{

    List<Blog> objectList;
    Context context;
    private final OnclickListener clickListener;
    LayoutInflater inflater;
    RecyclerView recyclerView;

    public DashboardBlogsRVAdapter(@NonNull Context context, List<Blog> objectList, OnclickListener clickListener) {
        this.context = context;
        this.objectList = objectList;
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_blogs_rv_item, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(objectList.get(position).getName());
        holder.description.setText(objectList.get(position).getDescription());

        if(objectList.get(position).getPhotoUrl()!=null)
            Glide.with(context).load(objectList.get(position).getPhotoUrl())
                .placeholder(R.drawable.ic_stories)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                .into(holder.blogImg);
        else
            holder.blogImg.setBackgroundResource(R.drawable.ic_stories);


        List<Diversity> tags = objectList.get(position).getTags();
        int size = tags.size();
        holder.chipGroup.removeAllViews();
        for (int i = 0; i < size; i++) {
            String text = tags.get(i).getName();
            addTags(holder, text);
            if (i == 2)
                break;
        }
        if (size > 3) {
            String remain = "+ " + (size - 3);
            holder.remainChip.setVisibility(View.VISIBLE);
            holder.remainChip.setText(remain);
        }

    }

    private void addTags(DashboardBlogsRVAdapter.ViewHolder holder, String selected){
        Chip chip = (Chip) LayoutInflater.from(context)
                .inflate(R.layout.tag_chip_layout, holder.chipGroup, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        holder.chipGroup.addView(chip);
    }

    @Override
    public int getItemCount() {
        return objectList == null ? 0 : objectList.size();
    }

    public void updateAdapter(List<Blog> mDataList) {
        this.objectList = mDataList;
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, description, remainChip;
        ImageView blogImg;
        ChipGroup chipGroup;
        OnclickListener clickListener;

        public ViewHolder(View itemView, OnclickListener clickListener) {
            super(itemView);
            this.blogImg = itemView.findViewById(R.id.blog_img);
            this.title = itemView.findViewById(R.id.blog_title);
            this.description = itemView.findViewById(R.id.blog_desc);
            this.chipGroup = itemView.findViewById(R.id.video_tags_chip_grp);
            this.remainChip = itemView.findViewById(R.id.chip_remain);
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
