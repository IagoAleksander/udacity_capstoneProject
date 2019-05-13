package com.iaz.findyourway.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.iaz.findyourway.R;
import com.iaz.findyourway.domain.entity.ResultModel;
import com.iaz.findyourway.domain.manager.AppDatabase;
import com.iaz.findyourway.presentation.ui.activity.HomeActivity;

import io.reactivex.schedulers.Schedulers;

/**
 * Implementation of App Widget functionality.
 */
public class FindYourWayWidget extends AppWidgetProvider {

    static ResultModel result;

    @SuppressLint("CheckResult")
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        if (result == null) {
            AppDatabase mDb = AppDatabase.getInstance(context);
            mDb.resultDao().loadAllResultsSync().subscribeOn(Schedulers.computation()).subscribe(results -> {
                if (results != null && !results.isEmpty())
                    result = results.get(results.size() - 1);
                updateWidget(context, appWidgetManager, appWidgetId);
            }, Throwable::printStackTrace);

        }
        else {
            updateWidget(context, appWidgetManager, appWidgetId);
        }

    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent appIntent;
        appIntent = new Intent(context, HomeActivity.class);

        appIntent.addCategory(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views;
        if (result == null) {
            views = new RemoteViews(context.getPackageName(), R.layout.widget_without_result);
            views.setOnClickPendingIntent(R.id.ll_before_result, appPendingIntent);
        } else {
            views = new RemoteViews(context.getPackageName(), R.layout.widget_with_result);
            views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
            views.setOnClickPendingIntent(R.id.ll_after_result, appPendingIntent);
        }

        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent2 = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent2);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateResultWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, ResultModel resultForWidget) {
        result = resultForWidget;
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

