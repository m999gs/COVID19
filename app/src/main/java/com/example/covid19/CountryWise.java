package com.example.covid19;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CountryWise extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;

    double lati;
    double longi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countrywise);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        setTitle(i.getStringExtra("countryname"));
        lati = i.getDoubleExtra("latitude",0);
        longi = i.getDoubleExtra("longitude",0);
        ImageButton countryname = findViewById(R.id.countryName2);
        Button confirmed = findViewById(R.id.confirmedCases2);
        Button recovered = findViewById(R.id.recoveredCases2);
        Button death = findViewById(R.id.deathCases2);

        countryname.setImageResource(i.getIntExtra("flag",0));
        confirmed.setText("Confirmed  "+ i.getIntExtra("confirmed",0));
        recovered.setText("Recovered  " + i.getIntExtra("recovered",0));
        death.setText("Deceased  " + i.getIntExtra("Death",0));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng USA = new LatLng(lati, longi);
        map.addMarker(new MarkerOptions().position(USA));
        map.moveCamera(CameraUpdateFactory.newLatLng(USA));
    }
}
