package com.nanodegree.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.nanodegree.bakingapp.activities.IngredientsActivity;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;

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
        TextView mRecipeNameTextView;

        @Nullable
        @BindView(R.id.iv_recipe_image)
        ImageView mRecipeImageView;

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
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {

        // Set recipe name
        holder.mRecipeNameTextView.setText(mRecipeList.get(position).getName());

        // Set recipe image
        String recipeImage = mRecipeList.get(position).getImage();
        if (recipeImage.trim().equals("")) {
            // not_found icon made by <a href="https://www.flaticon.com/authors/pixel-perfect"
            // title="Pixel perfect">Pixel perfect</a> from <a href="https://www.flaticon.com/"
            // title="Flaticon"> www.flaticon.com</a>
            holder.mRecipeImageView.setImageResource(R.drawable.recipe_icon);
        } else {
            Picasso.get()
                    .load(recipeImage)
                    .into(holder.mRecipeImageView);
        }

        holder.itemView.setOnClickListener(v -> {
            Recipe recipe = mRecipeList.get(holder.getAdapterPosition());
            ArrayList<Recipe> recipeArrayList = new ArrayList<>();
            recipeArrayList.add(recipe);

            mRecipeJson = jsonToString(mJsonResult, holder.getAdapterPosition());

            Intent intent = new Intent(mContext, IngredientsActivity.class);
            intent.putParcelableArrayListExtra(AppUtils.RECIPE_INTENT_EXTRA, recipeArrayList);
            intent.putExtra(AppUtils.JSON_RESULT_EXTRA, mRecipeJson);
            mContext.startActivity(intent);
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
