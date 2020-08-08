package com.nanodegree.bakingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.RecipeService;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.Utils.NetworkUtils;
import com.nanodegree.bakingapp.adapters.RecipeAdapter;
import com.nanodegree.bakingapp.model.Recipe;
import com.nanodegree.bakingapp.uitest.SimpleIdlingResource;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String mJsonResult;
    ArrayList<Recipe> mRecipeArrayList = new ArrayList<>();
    RecipeAdapter mRecipeAdapter;

    private boolean mTwoPane;

    @BindView(R.id.rv_recipe)
    RecyclerView mRecyclerView;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mTwoPane = findViewById(R.id.recipe_tablet) != null;
        getIdlingResource();

        if (savedInstanceState != null) {
            mJsonResult = savedInstanceState.getString(AppUtils.RECIPE_JSON_STATE);
            mRecipeArrayList = savedInstanceState.getParcelableArrayList(AppUtils.RECIPE_ARRAY_LIST_STATE);

            mRecipeAdapter = new RecipeAdapter(MainActivity.this, mRecipeArrayList, mJsonResult);
            RecyclerView.LayoutManager mLayoutManager;
            if (mTwoPane) {
                mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
            } else {
                mLayoutManager = new LinearLayoutManager(MainActivity.this);
            }
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mRecipeAdapter);

        } else {
            if (NetworkUtils.isConnected(this)) {
                fetchRecipes();
            }
        }
    }

    private void fetchRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeService request = retrofit.create(RecipeService.class);

        Call<ArrayList<Recipe>> call = request.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                mRecipeArrayList = response.body();
                mJsonResult = new Gson().toJson(mRecipeArrayList);

                mRecipeAdapter = new RecipeAdapter(MainActivity.this, mRecipeArrayList, mJsonResult);
                RecyclerView.LayoutManager mLayoutManager;
                if (mTwoPane) {
                    mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                } else {
                    mLayoutManager = new LinearLayoutManager(MainActivity.this);
                }
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mRecipeAdapter);

                Log.d("BAKING_APP", mRecipeArrayList.get(3).getName());
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d("BAKING_APP", t.toString());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppUtils.RECIPE_JSON_STATE, mJsonResult);
        outState.putParcelableArrayList(AppUtils.RECIPE_ARRAY_LIST_STATE, mRecipeArrayList);
    }
}