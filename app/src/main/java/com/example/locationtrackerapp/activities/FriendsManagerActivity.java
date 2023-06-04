package com.example.locationtrackerapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.locationtrackerapp.R;
import com.example.locationtrackerapp.adapters.FriendsManagerTabAdapter;
import com.google.android.material.tabs.TabLayout;

public class FriendsManagerActivity extends AppCompatActivity {

    private ImageView toolbarBack;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_manager);

        toolbarBack = findViewById(R.id.toolbar_back);
        toolbarBack.setVisibility(View.VISIBLE);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);

        FriendsManagerTabAdapter reportsMenuAdapter = new FriendsManagerTabAdapter(this);
        viewPager2.setAdapter(reportsMenuAdapter);
        viewPager2.setUserInputEnabled(false);

        loadListeners();
    }

    private void loadListeners() {
        toolbarBack.setOnClickListener(v -> finish());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

    }

}