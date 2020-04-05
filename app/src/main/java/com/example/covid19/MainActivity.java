package com.example.covid19;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.RecognizerResultsIntent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    public static TextView speechText;

    public static ArrayList<D> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        list = new ArrayList<>();

        list.add(new D("India","40000","5000","0"));
        ImageButton micButton =findViewById(R.id.micButton);
        speechText = findViewById(R.id.speechText);
        Button refreshButton = findViewById(R.id.refreshButton);
        fetchData process = new fetchData();

        //To fetch the data from the internet.

        process.execute();

        ListView countryList = findViewById(R.id.countryList);
        customAdapter cA=new customAdapter(this,R.layout.covidcases,list);
        countryList.setAdapter(cA);
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i).getCountry());
        }
        //To put the list data into ListView

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData process = new fetchData();
                process.execute();
                for(int i=0;i<list.size();i++){
                    System.out.println(list.get(i).getCountry());
                }
            }
        });


        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if(intent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(intent,10);
                }
                else
                    Toast.makeText(getApplicationContext(), "Your Device doesnt support this feature", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng USA = new LatLng(38.360588, -101.367555);
        map.addMarker(new MarkerOptions().position(USA));
        map.moveCamera(CameraUpdateFactory.newLatLng(USA));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if(resultCode == RESULT_OK && data !=null){
                    ArrayList<String> res =  data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechText.setText(res.get(0));
                }

                break;
        }
    }

}
