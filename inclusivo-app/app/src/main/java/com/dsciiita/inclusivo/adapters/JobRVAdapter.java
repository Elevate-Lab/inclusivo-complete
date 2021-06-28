package com.dsciiita.inclusivo.adapters;

import android.content.Context;
import android.util.Log;
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
import com.dsciiita.inclusivo.activities.JobDescriptionActivity;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.responses.CityResponse;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobRVAdapter extends RecyclerView.Adapter<JobRVAdapter.ViewHolder>{

    private List<Job> jobList;
    private List<List<CityResponse>> locationResponses;
    private Context context;
    private Date date;
    private final onJobListener mOnNoteListener;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;


    public JobRVAdapter(@NonNull Context context, List<Job> jobList, onJobListener mOnNoteListener) {
        this.context = context;
        this.jobList = jobList;
        this.date = new Date();
        inflater = LayoutInflater.from(context);
        locationResponses = new ArrayList<>();
        this.mOnNoteListener = mOnNoteListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_list_rv_item, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    public void updateAdapter(List<Job> mDataList) {
        this.jobList = mDataList;
        locationResponses.clear();
        for(Job job: mDataList)
            locationResponses.add(job.getLocations());
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        jobList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, jobList.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.jobTitle.setText(jobList.get(position).getTitle());
        holder.jobVacancy.setText(String.valueOf(jobList.get(position).getVacancies()));
        holder.jobRole.setText(jobList.get(position).getJobRole());
        holder.jobType.setText(jobList.get(position).getJobType());

        if(jobList.get(position).getCompany().getLogoUrl()!=null)
            Glide.with(context).load(jobList.get(position).getCompany().getLogoUrl())
                .placeholder(R.drawable.ic_companies)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(holder.companyImg);
        else
            holder.companyImg.setBackgroundResource(R.drawable.ic_companies);

        String location = "";
        List<CityResponse> response = locationResponses.get(position);
        if(response!=null) {
            for (CityResponse cityResponse : response)
                location = cityResponse.getName() + ", " + location;
        }
        holder.jobLocations.setText(location);


        List<Diversity> tags = jobList.get(position).getDiversities();
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

        String date = jobList.get(position).getPostedOn().split("T")[0];
        String time = jobList.get(position).getPostedOn().split("T")[1].split("\\.")[0];
        String formattedDT = date+" "+time;
        String diff = null;
        try {
            diff = computeDifference(formattedDT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.timeDiff.setText(diff);

        if(SharedPrefManager.getInstance(context).isEmployer())
            holder.saveAnim.setVisibility(View.GONE);
        else if(jobList.get(position).getStatus().equals("Hired") || jobList.get(position).getStatus().equals("Expired"))
            holder.saveAnim.setVisibility(View.GONE);
        else {
            if (jobList.get(position).isLiked()) {
                holder.saveAnim.setFrame(50);
                holder.saveAnim.setTag(R.drawable.ic_save_red_filled);
            } else {
                holder.saveAnim.setFrame(0);
                holder.saveAnim.setTag(R.drawable.ic_save_red);
            }
        }

        holder.saveAnim.setOnClickListener(view->{
            if(holder.saveAnim.getTag().equals(R.drawable.ic_save_red_filled)) {
                unlikeJob(jobList.get(position).getJobId(), holder, position);
            }
            else {
                likeJob(jobList.get(position).getJobId(), holder);
            }
            holder.saveAnim.setEnabled(false);
            holder.saveProgress.setVisibility(View.VISIBLE);
        });
    }


    private String computeDifference(String formattedDT) throws ParseException {

        SimpleDateFormat formatGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat formatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formatLocal.setTimeZone(TimeZone.getDefault());

        Date postedGMTDate = formatGMT.parse(formattedDT);

        String localDate = formatLocal.format(postedGMTDate);
        Date postedDate = formatLocal.parse(localDate);

        long diff = date.getTime() - postedDate.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
        int weeks = (int) difference_In_Days/7;
        if(weeks==0) {
            if (difference_In_Days == 0) {
                if (diffHours == 0) {
                    if(diffMinutes==1)
                        return "a min ago";
                    return diffMinutes + " mins ago";
                }
                if(diffHours==1)
                    return "an hour ago";
                return diffHours + " hours ago";
            }
            if(difference_In_Days==1)
                return "a day ago";
            return difference_In_Days + " days ago";
        }
        if(weeks==1)
            return "a week ago";
        return weeks+" weeks ago";
    }

    @Override
    public int getItemCount() {
        return jobList == null ? 0 : jobList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView jobTitle, jobRole, jobType, jobLocations, jobVacancy, remainChip, timeDiff;
        ImageView companyImg;
        LottieAnimationView saveAnim;
        ProgressBar saveProgress;
        ChipGroup chipGroup;
        onJobListener noteListener;

         public ViewHolder(View itemView, onJobListener onJobListener){
             super(itemView);
             this.jobLocations = itemView.findViewById(R.id.location_txt);
             this.jobTitle = itemView.findViewById(R.id.job_title);
             this.saveProgress = itemView.findViewById(R.id.saveProgress);
             this.jobType = itemView.findViewById(R.id.job_type_txt);
             this.jobRole = itemView.findViewById(R.id.job_role_txt);
             this.companyImg = itemView.findViewById(R.id.job_company_img);
             this.jobVacancy = itemView.findViewById(R.id.vacancy_value_txt);
             this.chipGroup = itemView.findViewById(R.id.job_tags_chip_grp);
             this.remainChip = itemView.findViewById(R.id.chip_remain);
             this.timeDiff = itemView.findViewById(R.id.published_time_txt);
             this.saveAnim = itemView.findViewById(R.id.save_anim);
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

    private void addTags(ViewHolder holder, String selected){
        Chip chip = (Chip) LayoutInflater.from(context)
                .inflate(R.layout.tag_chip_layout, holder.chipGroup, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        holder.chipGroup.addView(chip);
    }

    public interface onJobListener{
        void onJobClick(int position, View v);
        void onJobLongClick(int position);
    }



    private void likeJob(int jobId, ViewHolder holder) {
        String token = "token "+ SharedPrefManager.getInstance(context).getToken();
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().likeJob(jobId, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.isSuccessful()) {
                    holder.saveAnim.setSpeed(1);
                    holder.saveAnim.playAnimation();
                    holder.saveAnim.setTag(R.drawable.ic_save_red_filled);
                    Snackbar.make(recyclerView, "Added to saved jobs", Snackbar.LENGTH_SHORT).show();
                }
                holder.saveProgress.setVisibility(View.GONE);
                holder.saveAnim.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                holder.saveProgress.setVisibility(View.GONE);
                holder.saveAnim.setEnabled(true);
                Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void unlikeJob(int jobId, ViewHolder holder, int position) {
        String token = "token "+ SharedPrefManager.getInstance(context).getToken();
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().unlikeJob(jobId, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response){
                if(recyclerView.getId()==R.id.saved_jobs_rv_removable){
                    removeAt(position);
                }
                holder.saveAnim.setSpeed(-2);
                holder.saveAnim.setFrame(25);
                holder.saveAnim.playAnimation();
                holder.saveAnim.setTag(R.drawable.ic_save_red);
                holder.saveProgress.setVisibility(View.GONE);
                Snackbar.make(recyclerView, "Removed from saved jobs", Snackbar.LENGTH_SHORT).show();
                holder.saveAnim.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                holder.saveProgress.setVisibility(View.GONE);
                holder.saveAnim.setEnabled(true);
                Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
