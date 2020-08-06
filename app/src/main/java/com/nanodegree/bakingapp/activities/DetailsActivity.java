package com.nanodegree.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.model.Recipe;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    ArrayList<Recipe> mRecipeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get recipe from intent extra
        Intent recipeIntent = getIntent();
        mRecipeArrayList = recipeIntent.getParcelableArrayListExtra(AppUtils.RECIPE_INTENT_EXTRA);
    }
}
