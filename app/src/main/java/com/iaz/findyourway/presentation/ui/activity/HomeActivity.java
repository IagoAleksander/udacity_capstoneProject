package com.iaz.findyourway.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.iaz.findyourway.R;
import com.iaz.findyourway.presentation.ui.adapter.StatePagerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mPager;
    public boolean resultsWereUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-7571018804408660~4658343545");

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new StatePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setViewPagerPage(int page) {

        if (resultsWereUpdated) {
            Toast.makeText(this, getString(R.string.destinations_updated), Toast.LENGTH_SHORT).show();
            resultsWereUpdated = false;
            mPager.setCurrentItem(page);
        }
    }
}