package com.nanodegree.bakingapp.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.model.Ingredient;
import com.nanodegree.bakingapp.model.Recipe;

import java.util.List;

public class RecipeWidgetService extends IntentService {

    public static final String ACTION_OPEN_RECIPE =
            "com.nanodegree.bakinapp.widget.recipe_widget_service";


    public RecipeWidgetService(String name) {
        super(name);
    }

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Baking App service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (ACTION_OPEN_RECIPE.equals(action)) {
            handleActionOpenRecipe();
        }
    }

    private void handleActionOpenRecipe() {
        SharedPreferences sharedpreferences =
                getSharedPreferences(AppUtils.SHARED_PREFERENCES, MODE_PRIVATE);
        String jsonRecipe = sharedpreferences.getString(AppUtils.JSON_RESULT_EXTRA, "");

        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();

        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);

        List<Ingredient> ingredientList = recipe.getIngredients();
        for (Ingredient ingredient : ingredientList) {
            String quantity = String.valueOf(ingredient.getQuantity());
            String measure = ingredient.getMeasure();
            String ingredientName = ingredient.getIngredient();
            String line = quantity + " " + measure + " " + ingredientName;
            stringBuilder.append(line + "\n");
        }
        String ingredientsString = stringBuilder.toString();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateWidgetRecipe(this, ingredientsString,
                R.drawable.recipe_icon, appWidgetManager, appWidgetIds);
    }

    public static void startActionOpenRecipe(Context context) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }

    public static void startActionOpenRecipeO(Context context) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        ContextCompat.startForegroundService(context, intent);
    }
}