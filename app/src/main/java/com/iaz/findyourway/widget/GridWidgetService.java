package com.iaz.findyourway.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.iaz.findyourway.R;
import com.iaz.findyourway.domain.entity.PlaceModel;
import com.iaz.findyourway.domain.manager.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.iaz.findyourway.widget.FindYourWayWidget.result;

public class GridWidgetService extends RemoteViewsService {

    private List<String> remoteViewPlacesList;
    final private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class GridRemoteViewsFactory implements RemoteViewsFactory {

        final Context mContext;

        private GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            ArrayList<String> destinationsForWidgets = new ArrayList<>();

            if (result == null) {
                AppDatabase mDb = AppDatabase.getInstance(mContext);
                compositeDisposable.add(mDb.resultDao().loadAllResultsSync().subscribeOn(Schedulers.computation()).subscribe(results -> {
                    if (results != null && !results.isEmpty())
                        result = results.get(results.size() - 1);

                    if (result != null) {
                        for (PlaceModel place : result.getPlaces()) {
                            destinationsForWidgets.add(place.getName());
                        }
                    }

                    remoteViewPlacesList = destinationsForWidgets;
                }, Throwable::printStackTrace));
            }
            else {
                for (PlaceModel place : result.getPlaces()) {
                    destinationsForWidgets.add(place.getName());
                }
            }

            remoteViewPlacesList = destinationsForWidgets;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {

            return remoteViewPlacesList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_view_item);

            views.setTextViewText(R.id.widget_grid_view_item, "- " + remoteViewPlacesList.get(position));

            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_grid_view_item, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


    }


}