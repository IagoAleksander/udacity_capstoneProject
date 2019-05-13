package com.iaz.findyourway.presentation.ui.adapter;

import com.iaz.findyourway.presentation.ui.fragment.MainPageFragment;
import com.iaz.findyourway.presentation.ui.fragment.ResultsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class StatePagerAdapter extends androidx.fragment.app.FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 2;

    public StatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainPageFragment();
            case 1:
                return new ResultsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "New";
            case 1:
                return "Results";
            default:
                return null;
        }
    }
}