package com.dsciiita.inclusivo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.storage.Constants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyStoryRVAdapter extends RecyclerView.Adapter<CompanyStoryRVAdapter.ViewHolder>{

    List<Story> storyList;
    Context context;
    private final onStoryListener mOnStoryListener;
    LayoutInflater inflater;

    public CompanyStoryRVAdapter(@NonNull Context context, List<Story> storyList, onStoryListener mOnStoryListener) {
        this.context = context;
        this.storyList = storyList;
        inflater = LayoutInflater.from(context);
        this.mOnStoryListener = mOnStoryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_rv_item, parent, false);
        return new ViewHolder(view, mOnStoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.storyTitle.setText(storyList.get(position).getName());
        holder.storyDesc.setText(storyList.get(position).getDescription());

        holder.storyCompanyName.setText(storyList.get(position).getCompany().getName());
        if(storyList.get(position).getPhotoUrl()!=null)
            Glide.with(context).load(storyList.get(position).getPhotoUrl())
                .placeholder(Constants.PLACEHOLDER_IMAGE)
                .into(holder.storyImage);
        else
            holder.storyImage.setImageResource(Constants.PLACEHOLDER_IMAGE);
    }

    @Override
    public int getItemCount() {
        return storyList == null ? 0 : storyList.size();
    }

    public void updateAdapter(List<Story> mDataList) {
        this.storyList = mDataList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView storyTitle, storyDesc, storyCompanyName;
        CircleImageView storyImage;
        onStoryListener storyListener;

        public ViewHolder(View itemView, onStoryListener onStoryListener) {
            super(itemView);
            this.storyTitle = itemView.findViewById(R.id.story_title);
            this.storyDesc = itemView.findViewById(R.id.story_description);
            this.storyImage = itemView.findViewById(R.id.storyImageView);
            this.storyCompanyName = itemView.findViewById(R.id.name_txt);
            storyListener = onStoryListener;
             itemView.setOnClickListener(this);
             itemView.setOnLongClickListener(this);
         }

        @Override
        public void onClick(View v) {
            storyListener.onStoryClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View view) {
            storyListener.onStoryLongClick(getLayoutPosition());
            return true;
        }
    }

    public interface onStoryListener{
        void onStoryClick(int position, View v);
        void onStoryLongClick(int position);
    }
}
