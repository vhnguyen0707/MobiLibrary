package com.example.mobilibrary;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class requestMap extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap map;
    SupportMapFragment mapFragment;
    LatLng newLatLng;
    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /*mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        AutocompleteSupportFragment searchBar = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.searchbar);

        searchBar.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        searchBar.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String location = searchBar.getQuery().toString();
                List<Address> addressList = null;
                if(location != null || location != ""){
                    Geocoder geocoder = new Geocoder(requestMap.this);
                    try {
                        addressList = geocoder.getFromLocationName(place, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);
                    newLatLng = new LatLng(address.getLatitude(),address.getLongitude());
                    float zoomLevel = 16.0f;
                    map.addMarker(new MarkerOptions().position(newLatLng).title("Marker"));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng,zoomLevel));
                }
            }
            @Override
            public void onError(@NonNull Status status) {
            }

            });

        mapFragment.getMapAsync(this);*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}


