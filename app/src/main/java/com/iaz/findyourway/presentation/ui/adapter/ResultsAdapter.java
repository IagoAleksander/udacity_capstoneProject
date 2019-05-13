package com.iaz.findyourway.presentation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iaz.findyourway.R;
import com.iaz.findyourway.databinding.ItemResultBinding;
import com.iaz.findyourway.domain.entity.PlaceModel;
import com.iaz.findyourway.domain.entity.ResultModel;
import com.iaz.findyourway.domain.manager.AppDatabase;
import com.iaz.findyourway.utility.Util;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private ResultModel result;
    private List<PlaceModel> places;
    final private AppDatabase mDb;
    final private Context context;
    final private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ResultsAdapter(Context context, ResultModel result) {
        this.result = result;
        this.places = result.getPlaces();
        this.context = context;
        mDb = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemResultBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_result, parent, false);
        return new ResultsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ViewHolder holder, int position) {
        PlaceModel place = places.get(position);

        holder.binding.tvDestination.setText(place.getName());

        if (place.isChecked()) {
            holder.binding.cbDestination.setChecked(true);
        } else {
            holder.binding.cbDestination.setChecked(false);
        }

        holder.binding.cbDestination.setOnCheckedChangeListener((buttonView, isChecked) -> {
            result.getPlaces().get(position).setChecked(isChecked);
            compositeDisposable.add(Completable.fromAction(() -> mDb.resultDao().updateResult(result))
                    .subscribeOn(Schedulers.computation()).subscribe(() -> {
                            },
                            throwable -> {
                                holder.binding.cbDestination.setChecked(!isChecked);
                                result.getPlaces().get(position).setChecked(!isChecked);
                                Util.throwErrorMessage(context);
                            }));
        });


    }

    @Override
    public int getItemCount() {
        if (places == null)
            return 0;
        else
            return places.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ItemResultBinding binding;

        ViewHolder(ItemResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(ResultModel result) {
        this.result = result;
        this.places = result.getPlaces();
        notifyDataSetChanged();
    }
}
