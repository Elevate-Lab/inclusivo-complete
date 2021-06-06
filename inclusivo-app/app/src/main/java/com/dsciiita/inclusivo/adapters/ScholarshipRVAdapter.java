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
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.api.ApiClient;
import com.dsciiita.inclusivo.models.Diversity;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.storage.Constants;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScholarshipRVAdapter extends RecyclerView.Adapter<ScholarshipRVAdapter.ViewHolder>{

    List<Scholarship> objectList;
    Context context;
    private final OnclickListener clickListener;
    LayoutInflater inflater;
    int res;
    RecyclerView recyclerView;

    public ScholarshipRVAdapter(@NonNull Context context, int res, List<Scholarship> objectList, OnclickListener clickListener) {
        this.context = context;
        this.objectList = objectList;
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        this.res = res;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(objectList.get(position).getTitle());

        if(objectList.get(position).getCompany()!=null) {
            holder.company.setText(objectList.get(position).getCompany().getName());
            holder.separator.setVisibility(View.VISIBLE);

            if(objectList.get(position).getCompany().getLogoUrl()!=null)
                Glide.with(context).load(objectList.get(position).getCompany().getLogoUrl())
                    .placeholder(R.drawable.ic_companies)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(holder.profile);
            else
                holder.profile.setBackgroundResource(R.drawable.ic_companies);
        }

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


        String date = objectList.get(position).getPostedOn().split("T")[0];
        String time = objectList.get(position).getPostedOn().split("T")[1].split("\\.")[0];
        String formattedDT = date+" "+time;
        String diff = null;
        try {
            diff = computeDifference(formattedDT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.expiry.setText(diff);


        if(SharedPrefManager.getInstance(context).isEmployer())
            holder.saveAnim.setVisibility(View.GONE);
        else {
            if (objectList.get(position).isLiked()) {
                holder.saveAnim.setSpeed(-2);
                holder.saveAnim.setFrame(25);
                holder.saveAnim.setTag(R.drawable.ic_save_red_filled);
            } else {
                holder.saveAnim.setFrame(0);
                holder.saveAnim.setSpeed(1);
                holder.saveAnim.setTag(R.drawable.ic_save_red);
            }
        }

        holder.saveAnim.setOnClickListener(view->{
            if(holder.saveAnim.getTag().equals(R.drawable.ic_save_red_filled)) {
                unlikeScholarship(objectList.get(position).getId(), holder, position);
            }
            else {
                likeScholarship(objectList.get(position).getId(), holder);
            }
            holder.saveAnim.setEnabled(false);
            holder.progressBar.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return objectList == null ? 0 : objectList.size();
    }

    public void updateAdapter(List<Scholarship> mDataList) {
        this.objectList = mDataList;
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, desc, expiry, remainChip, company;
        ImageView profile;
        LottieAnimationView saveAnim;
        ChipGroup chipGroup;
        ProgressBar progressBar;
        CardView separator;
        OnclickListener clickListener;

        public ViewHolder(View itemView, OnclickListener clickListener) {
            super(itemView);
            this.title = itemView.findViewById(R.id.job_title);
            this.desc = itemView.findViewById(R.id.aa);
            this.saveAnim = itemView.findViewById(R.id.save_img);
            this.progressBar = itemView.findViewById(R.id.saveProgress);
            this.profile = itemView.findViewById(R.id.job_company_img);
            this.expiry = itemView.findViewById(R.id.published_time_txt);
            this.remainChip = itemView.findViewById(R.id.chip_remain);
            this.chipGroup = itemView.findViewById(R.id.job_tags_chip_grp);
            this.company = itemView.findViewById(R.id.company_name);
            this.separator = itemView.findViewById(R.id.company_separator);
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

    private void addTags(ScholarshipRVAdapter.ViewHolder holder, String selected){
        Chip chip = (Chip) LayoutInflater.from(context)
                .inflate(R.layout.tag_chip_layout, holder.chipGroup, false);
        chip.setText(selected);
        chip.setId(ViewCompat.generateViewId());
        holder.chipGroup.addView(chip);
    }

    private String computeDifference(String formattedDT) throws ParseException {

        SimpleDateFormat formatGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat formatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        formatLocal.setTimeZone(TimeZone.getDefault());

        Date postedGMTDate = formatGMT.parse(formattedDT);

        String localDate = formatLocal.format(postedGMTDate);
        Date postedDate = formatLocal.parse(localDate);

        Date date = new Date();
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

    private void likeScholarship(int id, ViewHolder holder) {
        String token = "token "+ SharedPrefManager.getInstance(context).getToken();
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().likeScholarship(id, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.isSuccessful()) {
                    holder.saveAnim.setSpeed(1);
                    holder.saveAnim.playAnimation();
                    holder.saveAnim.setTag(R.drawable.ic_save_red_filled);
                    Snackbar.make(recyclerView, "Added to saved scholarships", Snackbar.LENGTH_SHORT).show();
                }
                holder.progressBar.setVisibility(View.GONE);
                holder.saveAnim.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
                holder.saveAnim.setEnabled(true);
                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void removeAt(int position) {
        objectList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, objectList.size());
    }

    private void unlikeScholarship(int id, ViewHolder holder, int position) {
        String token = "token "+ SharedPrefManager.getInstance(context).getToken();
        Call<DefaultResponse> userRequestCall = ApiClient.getUserService().unlikeScholarship(id, token);
        userRequestCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response){
                if(recyclerView.getId()==R.id.saved_scholarships_rv){
                    removeAt(position);
                }
                holder.progressBar.setVisibility(View.GONE);
                holder.saveAnim.setSpeed(-2);
                holder.saveAnim.setFrame(25);
                holder.saveAnim.playAnimation();
                holder.saveAnim.setTag(R.drawable.ic_save_red);
                Snackbar.make(recyclerView, "Removed from saved scholarships", Snackbar.LENGTH_SHORT).show();
                holder.saveAnim.setEnabled(true);
            }
            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(context, "Request failed", Toast.LENGTH_SHORT).show();
                holder.saveAnim.setEnabled(true);
                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }

}
