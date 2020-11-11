package com.example.mobilibrary;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;

import java.io.IOException;
import java.util.List;

public class requestMap extends FragmentActivity implements OnMapReadyCallback{
    private LatLng newLatLng;
    private GoogleMap map;
    private Button confirmButton;
    private SearchView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "YOUR_API_KEY");
        }

        searchButton = findViewById(R.id.search_bar);
        confirmButton = findViewById(R.id.confirm_button);

        mapFragment.getMapAsync(this);

        searchButton.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchButton.getQuery().toString();
                List<Address> addressList = null;
                if(location !=null || location != ""){
                    Geocoder geocoder = new Geocoder(requestMap.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    Float zoom = 16.0f;
                    newLatLng = new LatLng(address.getLatitude(),address.getLongitude());
                    map.addMarker(new MarkerOptions().position(newLatLng).title("Location"));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng,zoom));

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent();
                mapIntent.putExtra("LatLang", newLatLng);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}


