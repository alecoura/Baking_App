package com.nanodegree.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Utils.AppUtils;
import com.nanodegree.bakingapp.activities.IngredientsActivity;

public class WidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, String jsonRecipeIngredients, int imageId,
                                AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);

        Intent intent = new Intent(context, IngredientsActivity.class);
        intent.putExtra(AppUtils.WIDGET_EXTRA, "CAME_FROM_WIDGET");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        if (jsonRecipeIngredients.equals("")) {
            jsonRecipeIngredients = "There are no ingredients!";
        }

        views.setTextViewText(R.id.widget_ingredients, jsonRecipeIngredients);
        views.setImageViewResource(R.id.iv_widget_recipe_icon, imageId);
        views.setOnClickPendingIntent(R.id.widget_ingredients, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.startActionOpenRecipe(context);
    }

    public static void updateWidgetRecipe(Context context, String jsonRecipe, int imageId,
                                          AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, jsonRecipe, imageId, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

