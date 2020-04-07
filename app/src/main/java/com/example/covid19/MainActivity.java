package com.example.covid19;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    public static TextView speechText;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<D> list;
    private static final String URL_DATA = "https://coronavirus-tracker-api.herokuapp.com/v2/locations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton micButton = findViewById(R.id.micButton);
        speechText = findViewById(R.id.speechText);

        recyclerView = findViewById(R.id.countryList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        if(list.size()==0)loadRecyclerViewData();


        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                } else
                    Toast.makeText(getApplicationContext(), "Your Device doesn't support this feature", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("locations");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject cdata = array.getJSONObject(i);
                                if (cdata.getString("province").equals("")) {
                                    JSONObject cases = cdata.getJSONObject("latest");
                                    JSONObject coord = cdata.getJSONObject("coordinates");
                                    D item = new D(
                                            cdata.getString("country"),
                                            (int) cases.get("confirmed"),
                                            (int) cases.get("recovered"),
                                            (int) cases.get("deaths"),
                                            cdata.getString("country_code"),
                                            coord.getString("latitude"),
                                            coord.getString("longitude")
                                    );

                                    list.add(item);
                                }

                            }

                            JSONObject worldData = jsonObject.getJSONObject("latest");

                            D item = new D("WorldWide",
                                    (int) worldData.get("confirmed"),
                                    (int) worldData.get("recovered"),
                                    (int) worldData.get("deaths"),
                                    "ww",
                                    38.360588+"",
                                    -101.367555+""
                            );
                            list.add(item);
                            Collections.sort(list, new Comparator<D>() {

                                        @Override
                                        public int compare(D o1, D o2) {
                                            return o2.getConfirmed() - (o1.getConfirmed());
                                        }
                                    }
                            );
                            adapter = new MyAdapter(list, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechText.setText(res.get(0));
                }

                break;
        }
    }

}
