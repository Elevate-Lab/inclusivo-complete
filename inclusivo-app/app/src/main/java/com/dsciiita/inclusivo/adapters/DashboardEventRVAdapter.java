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
import com.dsciiita.inclusivo.models.Event;

import java.util.List;

public class DashboardEventRVAdapter extends RecyclerView.Adapter<DashboardEventRVAdapter.ViewHolder>{

    List<Event> objectList;
    Context context;
    private final OnclickListener clickListener;
    LayoutInflater inflater;
    RecyclerView recyclerView;

    public DashboardEventRVAdapter(@NonNull Context context, List<Event> objectList, OnclickListener clickListener) {
        this.context = context;
        this.objectList = objectList;
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_rv_items, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(objectList.get(position).getTitle());
        holder.location.setText(objectList.get(position).getLocation());
        holder.time.setText(objectList.get(position).getTime());


        if(objectList.get(position).getThumbnail_url()!=null)
            Glide.with(context).load(objectList.get(position).getThumbnail_url())
                .placeholder(R.drawable.login_bg_img)
                .into(holder.thumbnail);
        else
            Glide.with(context).load(R.drawable.login_bg_img)
                    .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return objectList == null ? 0 : objectList.size();
    }

    public void updateAdapter(List<Event> mDataList) {
        this.objectList = mDataList;
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, location, time;
        ImageView thumbnail;
        OnclickListener clickListener;

         public ViewHolder(View itemView, OnclickListener clickListener){
             super(itemView);
             this.title = itemView.findViewById(R.id.title);
             this.location = itemView.findViewById(R.id.location);
             this.thumbnail = itemView.findViewById(R.id.display_img);
             this.time = itemView.findViewById(R.id.time);
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
