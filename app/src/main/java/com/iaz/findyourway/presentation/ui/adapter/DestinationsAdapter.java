package com.iaz.findyourway.presentation.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iaz.findyourway.R;
import com.iaz.findyourway.databinding.ItemDestinationBinding;
import com.iaz.findyourway.domain.entity.PlaceModel;
import com.iaz.findyourway.domain.manager.AppDatabase;
import com.iaz.findyourway.utility.Util;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.ViewHolder> {

    private List<PlaceModel> places;
    final private AppDatabase mDb;
    final private Context context;
    final private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DestinationsAdapter(Context context, List<PlaceModel> places) {

        this.context = context;
        this.places = places;
        mDb = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public DestinationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDestinationBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_destination, parent, false);
        return new DestinationsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationsAdapter.ViewHolder holder, int position) {
        PlaceModel place = places.get(position);

        holder.binding.tvDestination.setText(place.getName());
        holder.binding.btDestinationRemove.setOnClickListener(v ->
                compositeDisposable.add(Completable.fromAction(() -> mDb.placeDao().deletePlace(place))
                        .subscribeOn(Schedulers.computation()).subscribe(() ->
                                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, "Destination removed successfully", Toast.LENGTH_SHORT).show()),
                        throwable -> Util.throwErrorMessage(context))));
    }

    @Override
    public int getItemCount() {
        if (places == null)
            return 0;
        else
            return places.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ItemDestinationBinding binding;

        ViewHolder(ItemDestinationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(List<PlaceModel> places) {
        this.places = places;
        notifyDataSetChanged();
    }
}
