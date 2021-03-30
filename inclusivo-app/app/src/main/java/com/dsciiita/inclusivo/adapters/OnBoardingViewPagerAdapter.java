package com.dsciiita.inclusivo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsciiita.inclusivo.R;

public class OnBoardingViewPagerAdapter extends RecyclerView.Adapter<OnBoardingViewPagerAdapter.ViewHolder> {
    private final int[] OnBoardingImgArray = {R.drawable.on_boarding_img_1, R.drawable.on_boarding_img_2, R.drawable.on_boarding_img_3};
    private final String[] onBoardingTxtArray = {"Get number of opportunities and find your dream job..", "Find the perfect candidate to fit into your team", "Grow in your career with endless opportunities"};

    @NonNull
    @Override
    public OnBoardingViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.on_boarding_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewPagerAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(OnBoardingImgArray[position]);
        holder.textView.setText(onBoardingTxtArray[position]);
    }

    @Override
    public int getItemCount() {
        return onBoardingTxtArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.onBoarding_img);
            textView = itemView.findViewById(R.id.on_boarding_txt);
        }
    }
}
