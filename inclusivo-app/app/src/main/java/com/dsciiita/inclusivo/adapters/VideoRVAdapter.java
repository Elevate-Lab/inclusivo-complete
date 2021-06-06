package com.dsciiita.inclusivo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Video;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoRVAdapter extends RecyclerView.Adapter<VideoRVAdapter.ViewHolder>{

    private List<Video> videoList;
    private Context context;
    private RecyclerView recyclerView;
    private final OnClickListener onclickListener;

    public VideoRVAdapter(@NonNull Context context, List<Video> videoList, OnClickListener onclickListener) {
        this.context = context;
        this.videoList = videoList;
        this.onclickListener = onclickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_list_rv_item, parent, false);
        return new ViewHolder(view, onclickListener);
    }

    public void updateAdapter(List<Video> mDataList) {
        this.videoList = mDataList;
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        videoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, videoList.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.videoTitle.setText(videoList.get(position).getName());
        holder.videoAuthor.setText(String.valueOf(videoList.get(position).getAuthorCredits()));
        holder.videoDesc.setText(videoList.get(position).getDescription());

        List<Diversity> tags = videoList.get(position).getTags();
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


    @Override
    public int getItemCount() {
        return videoList == null ? 0 : videoList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView videoTitle, videoAuthor, remainChip, videoDesc;
        ChipGroup chipGroup;
        OnClickListener clickListener;

         public ViewHolder(View itemView, OnClickListener OnClickListener){
             super(itemView);
             this.videoTitle = itemView.findViewById(R.id.video_title);
             this.videoAuthor = itemView.findViewById(R.id.video_author);
             this.videoDesc = itemView.findViewById(R.id.video_desc);
             this.chipGroup = itemView.findViewById(R.id.video_tags_chip_grp);
             this.remainChip = itemView.findViewById(R.id.chip_remain);
             this.clickListener = OnClickListener;
             itemView.setOnClickListener(this);
             itemView.setOnLongClickListener(this);
         }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.OnLongClick(getLayoutPosition());
            return true;
        }
    }

    private void addTags(ViewHolder holder, String selected){
        Chip chip = (Chip) LayoutInflater.from(context)
                .inflate(R.layout.tag_chip_layout, holder.chipGroup, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        holder.chipGroup.addView(chip);
    }

    public interface OnClickListener{
        void onClick(int position, View v);
        void OnLongClick(int position);
    }
}
