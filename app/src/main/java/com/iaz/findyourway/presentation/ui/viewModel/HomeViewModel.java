package com.iaz.findyourway.presentation.ui.viewModel;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.iaz.findyourway.R;
import com.iaz.findyourway.widget.FindYourWayWidget;
import com.iaz.findyourway.utility.Util;
import com.iaz.findyourway.domain.entity.PlaceModel;
import com.iaz.findyourway.domain.entity.ResultModel;
import com.iaz.findyourway.domain.manager.AppDatabase;
import com.iaz.travellingsalesman.travellingSalesman;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel implements Observable {

    final private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();
    final private AppDatabase mDb;
    private ArrayList<PlaceModel> places = new ArrayList<>();
    final private Context context;
    final private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        mDb = AppDatabase.getInstance(context);
    }

    public LiveData<List<PlaceModel>> loadAllPlaces() {
        return mDb.placeDao().loadAllPlaces();
    }

    public LiveData<List<ResultModel>> loadResults() {
        return mDb.resultDao().loadAllResults();
    }

    public void insertPlace(PlaceModel place) {
        compositeDisposable.add(Completable.fromAction(() -> mDb.placeDao().insertPlace(place)).subscribeOn(Schedulers.computation()).subscribe(() -> {
        }, throwable -> Util.throwErrorMessage(context)));
    }

    public void clearAllPlaces() {
        compositeDisposable.add(Completable.fromAction(() -> mDb.placeDao().nukeTable()).subscribeOn(Schedulers.computation()).subscribe(() -> {
        }, throwable -> Util.throwErrorMessage(context)));
    }

    private void insertResult(ResultModel result) {
        compositeDisposable.add(Completable.fromAction(() -> mDb.resultDao().insertResult(result)).subscribeOn(Schedulers.computation()).subscribe(() -> {
            //update widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, FindYourWayWidget.class));
            FindYourWayWidget.updateResultWidgets(context, appWidgetManager, appWidgetIds, result);
        }, throwable -> Util.throwErrorMessage(context)));
    }

    public ArrayList<PlaceModel> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<PlaceModel> places) {
        this.places = places;
    }

    public void calculate(TravelMode mode) {
        ArrayList<LatLng> locations = new ArrayList<>();
        for (PlaceModel place : places) {

            LatLng location = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            locations.add(location);
        }

        if (locations.size() >= 3) {
            compositeDisposable.add(travellingSalesman.main(locations, mode).subscribeOn(Schedulers.computation()).subscribe(result -> {
                if (result != null && !result.isEmpty()) {
                    ArrayList<PlaceModel> sortedPlaces = Util.convertArray(result, getPlaces());
                    insertResult(new ResultModel(sortedPlaces, result.get(result.size() - 1)));
                } else {
                    Util.throwErrorMessage(context);
                }
            }, throwable -> Util.throwErrorMessage(context)));

        } else {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, context.getString(R.string.error_location_not_min), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }
}
