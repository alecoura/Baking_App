package com.nanodegree.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nanodegree.bakingapp.activities.DetailsActivity;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context mContext;
    private final ArrayList<Recipe> mRecipeList;
    private String mJsonResult;
    String mRecipeJson;

    public RecipeAdapter(Context mContext, ArrayList<Recipe> mRecipeList, String mJsonResult) {
        this.mContext = mContext;
        this.mRecipeList = mRecipeList;
        this.mJsonResult = mJsonResult;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTextView;

        @Nullable
        @BindView(R.id.iv_recipe_image)
        ImageView recipeImageView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeAdapter.RecipeViewHolder holder, int position) {

        // Set recipe name
        holder.recipeNameTextView.setText(mRecipeList.get(position).getName());

        /*
        // Set recipe image
        String recipeImage = mRecipeList.get(position).getImage();
        if (recipeImage != null) {
            Picasso.get()
                    .load(recipeImage)
                    .into(holder.recipeImageView);
        } else {
            // Icons made by <a href="https://www.flaticon.com/authors/pixelmeetup"
            // title="Pixelmeetup">Pixelmeetup</a> from <a href="https://www.flaticon.com/"
            // title="Flaticon">www.flaticon.com</a>
                holder.recipeImageView.setImageResource(R.drawable.not_found);
        } */

        holder.itemView.setOnClickListener(v -> {
            Recipe recipe = mRecipeList.get(holder.getAdapterPosition());
            mRecipeJson = jsonToString(mJsonResult, holder.getAdapterPosition());

            ArrayList<Recipe> recipeArrayList = new ArrayList<>();
            recipeArrayList.add(recipe);

            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putParcelableArrayListExtra(AppUtils.RECIPE_INTENT_EXTRA, recipeArrayList);
            intent.putExtra(AppUtils.JSON_RESULT_EXTRA, mRecipeJson);
            mContext.startActivity(intent);

            SharedPreferences.Editor editor = mContext.getSharedPreferences
                    (AppUtils.SHARED_PREFERENCES, MODE_PRIVATE).edit();
            editor.putString(AppUtils.JSON_RESULT_EXTRA, mRecipeJson);
            editor.apply();
        });
    }

    private String jsonToString(String jsonResult, int position) {
        JsonElement jsonElement = new JsonParser().parse(jsonResult);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonElement recipeElement = jsonArray.get(position);

        return recipeElement.toString();
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }
}
