package com.example.a3;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.assignment_1_michael_cetrola.provider.EMAViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.assignment_1_michael_cetrola.databinding.ActivityGoogleMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;
    private String countryToFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        countryToFocus = getIntent().getExtras().getString("clickedCategoryCountry", "");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        String categoryName = getIntent().getExtras().getString("categoryName", "");

        findCountryMoveCamera(categoryName);

    }

    private void findCountryMoveCamera(String categoryName){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(countryToFocus, 1, addresses -> {
                if (!addresses.isEmpty()){
                    runOnUiThread(() -> {
                        LatLng countryLatLng = new LatLng(addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude());

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(countryLatLng));
                        mMap.addMarker(
                                new MarkerOptions().position(countryLatLng).title(categoryName)
                        );
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
                    });

                }
                else{
                    runOnUiThread(() -> {
                        String message = "Category address not found";
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }

    }

}