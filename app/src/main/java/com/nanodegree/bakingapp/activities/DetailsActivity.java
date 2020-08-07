package com.nanodegree.bakingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.adapters.DetailsAdapter;
import com.nanodegree.bakingapp.model.Ingredient;
import com.nanodegree.bakingapp.model.Recipe;
import com.nanodegree.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    public static final String RECIPE_LIST_STATE = "recipe_details_list";
    public static final String RECIPE_JSON_STATE = "recipe_json_list";

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_preparation)
    Button mButtonStartCooking;

    DetailsAdapter mRecipeDetailsAdapter;
    ArrayList<Recipe> mRecipeArrayList;
    ArrayList<Step> mStepArrayList;
    String mJsonResult;
    List<Ingredient> mIngredientList;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);
        mTwoPane = findViewById(R.id.recipe_details_tablet) != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(AppUtils.WIDGET_EXTRA) != null) {
            SharedPreferences sharedpreferences =
                    getSharedPreferences(AppUtils.SHARED_PREFERENCES, MODE_PRIVATE);
            String jsonRecipe = sharedpreferences.getString(AppUtils.JSON_RESULT_EXTRA, "");
            mJsonResult = jsonRecipe;

            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);

            mStepArrayList = (ArrayList<Step>) recipe.getSteps();
            mIngredientList = recipe.getIngredients();
        } else {
            if (savedInstanceState != null) {
                mRecipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);
                mJsonResult = savedInstanceState.getString(RECIPE_JSON_STATE);
            } else {
                Intent recipeIntent = getIntent();
                mRecipeArrayList = recipeIntent.getParcelableArrayListExtra(AppUtils.RECIPE_INTENT_EXTRA);
                mJsonResult = recipeIntent.getStringExtra(AppUtils.JSON_RESULT_EXTRA);
            }
            mStepArrayList = (ArrayList<Step>) mRecipeArrayList.get(0).getSteps();
            mIngredientList = mRecipeArrayList.get(0).getIngredients();
        }

        mRecipeDetailsAdapter = new DetailsAdapter(this, mIngredientList);

        RecyclerView.LayoutManager mLayoutManager;
        if (mTwoPane) {
            mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mLayoutManager = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecipeDetailsAdapter);

        mButtonStartCooking.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
            intent.putParcelableArrayListExtra(AppUtils.STEP_INTENT_EXTRA, mStepArrayList);
            intent.putExtra(AppUtils.JSON_RESULT_EXTRA, mJsonResult);
            startActivity(intent);
        });
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_STATE, mRecipeArrayList);
        outState.putString(RECIPE_JSON_STATE, mJsonResult);
    }
}

