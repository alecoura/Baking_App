package com.nanodegree.bakingapp;

import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    //Get list of recipes
    @GET(AppUtils.BASE_URL)
    Call<ArrayList<Recipe>> getRecipes();
}
