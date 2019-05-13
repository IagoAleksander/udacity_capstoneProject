package com.iaz.findyourway.presentation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.maps.model.TravelMode;
import com.iaz.findyourway.R;
import com.iaz.findyourway.databinding.FragmentMainBinding;
import com.iaz.findyourway.domain.entity.PlaceModel;
import com.iaz.findyourway.presentation.ui.activity.HomeActivity;
import com.iaz.findyourway.presentation.ui.adapter.DestinationsAdapter;
import com.iaz.findyourway.presentation.ui.viewModel.HomeViewModel;
import com.iaz.findyourway.presentation.ui.activity.MapsActivity;
import com.iaz.findyourway.utility.Constants;
import com.iaz.findyourway.utility.Util;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

public class MainPageFragment extends Fragment {

    private HomeViewModel mViewModel;
    private FragmentMainBinding binding;
    private DestinationsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        binding.btMaps.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivityForResult(intent, 1);
        });
        binding.btCalculate.setOnClickListener(v -> {
            TravelMode mode = TravelMode.DRIVING;
            if (binding.rbFoot.isChecked()) {
                mode = TravelMode.WALKING;
            }
            if (binding.rbBicycle.isChecked()) {
                mode = TravelMode.BICYCLING;
            }

            if (getActivity() != null && getActivity() instanceof HomeActivity)
                ((HomeActivity) getActivity()).resultsWereUpdated = true;

            mViewModel.calculate(mode);

        });

        mViewModel.loadAllPlaces().observe(this, placesList -> {
            if (placesList != null && !placesList.isEmpty()) {
                binding.rvDestinations.setVisibility(View.VISIBLE);
                binding.tvDestinationsPlaceholder.setVisibility(View.GONE);
                binding.btClear.setVisibility(View.VISIBLE);
                binding.btClear.setOnClickListener(v -> mViewModel.clearAllPlaces());

                mViewModel.setPlaces(new ArrayList<>(placesList));
                configRecycler(mViewModel.getPlaces());

                if (placesList.size() == 10) {
                    binding.btMaps.setVisibility(View.GONE);
                }
                else {
                    binding.btMaps.setVisibility(View.VISIBLE);
                }
            } else {
                mViewModel.setPlaces(new ArrayList<>());
                binding.rvDestinations.setVisibility(View.GONE);
                binding.tvDestinationsPlaceholder.setVisibility(View.VISIBLE);
                binding.btClear.setVisibility(View.GONE);
                binding.btMaps.setVisibility(View.VISIBLE);
            }
        });

        return binding.getRoot();
    }

    private void configRecycler(ArrayList<PlaceModel> places) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        if (places != null && !places.isEmpty()) {
            if (adapter == null) {
                binding.rvDestinations.setVisibility(View.VISIBLE);
                binding.rvDestinations.setLayoutManager(layoutManager);
                adapter = new DestinationsAdapter(getActivity(), places);
                binding.rvDestinations.setAdapter(adapter);
            } else {
                adapter.setData(places);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            if (mViewModel.getPlaces().size() < 10) {
                PlaceModel place = data.getParcelableExtra(Constants.BUNDLE_PLACE);
                mViewModel.insertPlace(place);
            }
            else {
                Toast.makeText(getContext(), getString(R.string.error_location_at_max), Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == RESULT_FIRST_USER && requestCode == 1){
            Util.throwErrorMessage(getContext());
        }
    }
}