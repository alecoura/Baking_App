package com.nanodegree.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.VideoPlayerFragment;
import com.nanodegree.bakingapp.adapters.StepNumberAdapter;
import com.nanodegree.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreparationActivity extends AppCompatActivity implements View.OnClickListener
        , StepNumberAdapter.OnStepClick {

    private static final String STEP_LIST_STATE = "step_list_state";
    private static final String STEP_NUMBER_STATE = "step_number_state";
    private static final String STEP_LIST_JSON_STATE = "step_list_json_state";

    private boolean mTwoPane;
    private int mVideoNumber = 0;

    @BindView(R.id.fl_player_container)
    FrameLayout mFragmentContainer;

    @BindView(R.id.btn_previous_step)
    Button mPreviousButton;

    @BindView(R.id.btn_next_step)
    Button mNextButton;

    @Nullable
    @BindView(R.id.rv_recipe_steps)
    RecyclerView mRecyclerView;

    ArrayList<Step> mStepArrayList = new ArrayList<>();
    String mJsonResult;
    boolean isFromWidget;
    StepNumberAdapter mStepNumberAdapter;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation);

        ButterKnife.bind(this);
        mTwoPane = findViewById(R.id.cooking_tablet) != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(AppUtils.STEP_INTENT_EXTRA)) {
                mStepArrayList = getIntent().getParcelableArrayListExtra(AppUtils.STEP_INTENT_EXTRA);
            }
            if (intent.hasExtra(AppUtils.JSON_RESULT_EXTRA)) {
                mJsonResult = getIntent().getStringExtra(AppUtils.JSON_RESULT_EXTRA);
            }
            isFromWidget = intent.getStringExtra(AppUtils.WIDGET_EXTRA) != null;
        }
        if (savedInstanceState == null) {
            playVideo(mStepArrayList.get(mVideoNumber));
        }

        handleUiForDevice();
    }

    public void playVideo(Step step) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(AppUtils.STEP_SINGLE, step);
        videoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fl_player_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    public void playVideoReplace(Step step) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(AppUtils.STEP_SINGLE, step);
        videoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_player_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (mVideoNumber == mStepArrayList.size() - 1) {
            Toast.makeText(this, "Finish!", Toast.LENGTH_SHORT).show();
        } else {
            if (v.getId() == mPreviousButton.getId()) {
                mVideoNumber--;
                if (mVideoNumber < 0) {
                    Toast.makeText(this, "There are more steps!", Toast.LENGTH_SHORT).show();
                } else
                    playVideoReplace(mStepArrayList.get(mVideoNumber));
            } else if (v.getId() == mNextButton.getId()) {
                mVideoNumber++;
                playVideoReplace(mStepArrayList.get(mVideoNumber));
            }
        }
    }

    @Override
    public void onStepClick(int position) {
        mVideoNumber = position;
        playVideoReplace(mStepArrayList.get(position));
    }

    public void handleUiForDevice() {
        if (mTwoPane) {
            mStepNumberAdapter = new StepNumberAdapter(this, mStepArrayList, this, mVideoNumber);
            mLinearLayoutManager = new LinearLayoutManager(this);
            if (mRecyclerView != null) {
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mRecyclerView.setAdapter(mStepNumberAdapter);
            }
        } else {
            mPreviousButton.setOnClickListener(this);
            mNextButton.setOnClickListener(this);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST_STATE, mStepArrayList);
        outState.putString(STEP_LIST_JSON_STATE, mJsonResult);
        outState.putInt(STEP_NUMBER_STATE, mVideoNumber);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStepArrayList = savedInstanceState.getParcelableArrayList(STEP_LIST_STATE);
        mJsonResult = savedInstanceState.getString(STEP_LIST_JSON_STATE);
        mVideoNumber = savedInstanceState.getInt(STEP_NUMBER_STATE);
    }
}
