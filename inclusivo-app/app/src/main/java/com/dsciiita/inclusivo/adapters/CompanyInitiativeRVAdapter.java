package com.dsciiita.inclusivo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.models.Initiative;
import com.dsciiita.inclusivo.models.Job;

import java.util.List;

public class CompanyInitiativeRVAdapter extends RecyclerView.Adapter<CompanyInitiativeRVAdapter.ViewHolder>{

    List<Initiative> initiativeList;
    Context context;
    private final onInitiativeListener mOnNoteListener;
    LayoutInflater inflater;

    public CompanyInitiativeRVAdapter(@NonNull Context context, List<Initiative> initiativeList, onInitiativeListener mOnNoteListener) {
        this.context = context;
        this.initiativeList = initiativeList;
        inflater = LayoutInflater.from(context);
        this.mOnNoteListener = mOnNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.initiatives_rv_item, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.initiativeTitle.setText(initiativeList.get(position).getName());
        holder.initiativeDesc.setText(initiativeList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return initiativeList == null ? 0 : initiativeList.size();
    }

    public void updateAdapter(List<Initiative> mDataList) {
        this.initiativeList = mDataList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView initiativeTitle, initiativeDesc;
        onInitiativeListener noteListener;

         public ViewHolder(View itemView, onInitiativeListener onInitiativeListener){
             super(itemView);
             this.initiativeTitle = itemView.findViewById(R.id.initiative_title);
             this.initiativeDesc = itemView.findViewById(R.id.initiative_description);
             noteListener = onInitiativeListener;
             itemView.setOnClickListener(this);
             itemView.setOnLongClickListener(this);
         }

        @Override
        public void onClick(View v) {
            noteListener.onInitiativeClick(getLayoutPosition(), v);
        }

        @Override
        public boolean onLongClick(View view) {
            noteListener.onInitiativeLongClick(getLayoutPosition());
            return true;
        }
    }

    public interface onInitiativeListener{
        void onInitiativeClick(int position, View v);
        void onInitiativeLongClick(int position);
    }
}
