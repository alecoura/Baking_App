package com.nanodegree.bakingapp.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nanodegree.bakingapp.VideoPlayerFragment;
import com.nanodegree.bakingapp.model.Step;

import java.util.ArrayList;

public class StepPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Step> mStepList;
    Bundle stepsBundle = new Bundle();

    public StepPagerAdapter(FragmentManager fm,  ArrayList<Step> stepList) {
        super(fm);
        this.mStepList = stepList;
    }

    @Override
    public Fragment getItem(int position) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        stepsBundle.putParcelableArrayList("steps", mStepList);
        stepsBundle.putInt("page",position+1);
        stepsBundle.putBoolean("isLastPage",position == getCount() - 1);
        videoPlayerFragment.setArguments(stepsBundle);

        return videoPlayerFragment;
    }

    @Override
    public int getCount() {
        return mStepList.size();
    }
}
