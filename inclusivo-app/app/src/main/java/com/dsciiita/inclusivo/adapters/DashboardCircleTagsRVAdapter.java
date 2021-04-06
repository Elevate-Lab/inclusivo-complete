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
import com.dsciiita.inclusivo.models.TitleLinkObject;

import java.util.List;

public class DashboardCircleTagsRVAdapter extends RecyclerView.Adapter<DashboardCircleTagsRVAdapter.ViewHolder>{

    List<TitleLinkObject> companyList;
    Context context;
    private final onCompanyListener mOnNoteListener;
    LayoutInflater inflater;
    int res;


    public DashboardCircleTagsRVAdapter(@NonNull Context context, int res, List<TitleLinkObject> companyList, onCompanyListener mOnNoteListener) {
        this.context = context;
        this.companyList = companyList;
        this.res = res;
        inflater = LayoutInflater.from(context);
        this.mOnNoteListener = mOnNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(companyList.get(position).getTitle());

        if(companyList.get(position).getLink()!=null)
            Glide.with(context).load(companyList.get(position).getLink())
                .placeholder(R.drawable.placeholder_circle)
                .circleCrop()
                .into(holder.image);
        else
            Glide.with(context).load(R.drawable.placeholder_circle)
                    .circleCrop()
                    .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return companyList == null ? 0 : companyList.size();
    }

    public void updateAdapter(List<TitleLinkObject> mDataList) {
        this.companyList = mDataList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title;
        ImageView image;
        onCompanyListener companyListener;

         public ViewHolder(View itemView, onCompanyListener onCompanyListener){
             super(itemView);
             this.title = itemView.findViewById(R.id.title);
             this.image = itemView.findViewById(R.id.display_img);
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
