package com.iaz.findyourway.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.iaz.findyourway.R;
import com.iaz.findyourway.databinding.FragmentResultsBinding;
import com.iaz.findyourway.domain.entity.PlaceModel;
import com.iaz.findyourway.domain.entity.ResultModel;
import com.iaz.findyourway.presentation.ui.activity.HomeActivity;
import com.iaz.findyourway.presentation.ui.viewModel.HomeViewModel;
import com.iaz.findyourway.presentation.ui.adapter.ResultsAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ResultsFragment extends Fragment {

    private HomeViewModel mViewModel;
    private FragmentResultsBinding binding;
    private ResultsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_results, container, false);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        mViewModel.loadResults().observe(this, results -> {
            if (results != null && !results.isEmpty())
                configView(results.get(results.size() - 1));
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        return binding.getRoot();
    }

    private void configView(ResultModel result) {
        List<PlaceModel> places = result.getPlaces();
        String totalDistance = result.getTotalDistance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        if (places != null && !places.isEmpty()) {
            binding.tvResultPlaceholder.setVisibility(View.GONE);
            binding.rvResult.setVisibility(View.VISIBLE);
            binding.rvResult.setLayoutManager(layoutManager);
            setupAdapter(result);
            binding.tvDestinationsFooter.setVisibility(View.VISIBLE);
            binding.tvDestinationsDistanceValue.setVisibility(View.VISIBLE);
            binding.tvDestinationsDistanceValue.setText(String.format("%s km", totalDistance));

            if (getActivity() != null && getActivity() instanceof HomeActivity)
                ((HomeActivity) getActivity()).setViewPagerPage(1);

        } else {
            binding.tvResultPlaceholder.setVisibility(View.VISIBLE);
            binding.rvResult.setVisibility(View.GONE);
            binding.tvDestinationsFooter.setVisibility(View.GONE);
            binding.tvDestinationsDistanceValue.setVisibility(View.GONE);
        }
    }

    private void setupAdapter(ResultModel result) {
        if (adapter == null) {
            adapter = new ResultsAdapter(getContext(), result);
            binding.rvResult.setAdapter(adapter);
        } else {
            adapter.setData(result);
        }
    }
}