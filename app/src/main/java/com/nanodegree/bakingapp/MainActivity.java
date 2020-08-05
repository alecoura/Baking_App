package com.nanodegree.bakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.model.Recipe;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecipeService mRecipeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchRecipes();
    }

    private void fetchRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeService request = retrofit.create(RecipeService.class);
        Call<ArrayList<Recipe>> call = request.getRecipes();
        Log.d("BAKING_APP", "RETROFIT CALL");
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recipe = response.body();
                Log.d("BAKING_APP", recipe.get(0).getName());
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d("FAILURE", t.toString());
            }
        });
    }
}