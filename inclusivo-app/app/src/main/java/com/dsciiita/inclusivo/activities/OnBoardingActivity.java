package com.dsciiita.inclusivo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.dsciiita.inclusivo.R;
import com.dsciiita.inclusivo.adapters.OnBoardingViewPagerAdapter;
import com.dsciiita.inclusivo.storage.SharedPrefManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager2 OnBoardingViewPager;
    private TabLayout onBoardingTabLayout;
    private MaterialButton getStartedBtn;
    private TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        if(!SharedPrefManager.getInstance(this).isNew()){
            startActivity(new Intent(OnBoardingActivity.this, EmailActivity.class));
            finish();
        }

        initViews();
        skip.setOnClickListener(this::onClick);
        OnBoardingViewPager.setAdapter(new OnBoardingViewPagerAdapter());
        getStartedBtn.setOnClickListener(this::onClick);
        new TabLayoutMediator(onBoardingTabLayout, OnBoardingViewPager, (tab, position) -> {

        }).attach();
        OnBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == 2) {
                    getStartedBtn.setText("Get Started");
                } else {
                    getStartedBtn.setText("Next");
                }
            }
        });

    }

    private void initViews() {
        OnBoardingViewPager = findViewById(R.id.on_boarding_view_pager);
        onBoardingTabLayout = findViewById(R.id.tab_indicator);
        getStartedBtn = findViewById(R.id.next_get_started_btn);
        skip = findViewById(R.id.skip_txt_btn);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip_txt_btn:
                startActivity(new Intent(OnBoardingActivity.this, EmailActivity.class));
                SharedPrefManager.getInstance(this).setNew(false);
                finish();
                break;
            case R.id.next_get_started_btn:
                if (OnBoardingViewPager.getCurrentItem() == 2) {
                    startActivity(new Intent(OnBoardingActivity.this, EmailActivity.class));
                    SharedPrefManager.getInstance(this).setNew(false);
                    finish();
                } else {
                    OnBoardingViewPager.setCurrentItem(OnBoardingViewPager.getCurrentItem() + 1);
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}