package com.nanodegree.bakingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    private final Context mContext;
    private final List<Ingredient> mIngredientList;

    public DetailsAdapter(Context context, List<Ingredient> ingredientList) {
        this.mContext = context;
        this.mIngredientList = ingredientList;
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_ingredient_name)
        TextView ingredientName;

        @Nullable
        @BindView(R.id.tv_unit_number)
        TextView unitNumber;

        @Nullable
        @BindView(R.id.tv_unit_long_name)
        TextView ingredientUnitLongName;

        public DetailsViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ingredient_list_item, parent, false);

        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailsViewHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);

        holder.ingredientName.setText(ingredient.getIngredient());
        holder.unitNumber.setText(String.valueOf(ingredient.getQuantity()));

        String measure = ingredient.getMeasure();
        int unitNo = 0;

        for (int i = 0; i < AppUtils.units.length; i++) {
            if (measure.equals(AppUtils.units[i])) {
                unitNo = i;
                break;
            }
        }
        String unitLongName = AppUtils.unitName[unitNo];
        holder.ingredientUnitLongName.setText(unitLongName);
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }
}