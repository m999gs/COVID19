package com.example.covid19;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.RecognizerResultsIntent;
import android.speech.tts.TextToSpeech;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.android.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.ResponseMessage;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, AIListener {

    GoogleMap map;
    private TextView speechText;
    private TextToSpeech textToSpeech;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ai.api.android.AIService aiService;
    private ArrayList<D> list;
    private static final int RECORD_AUDIO = 200;
    private static final String URL_DATA = "https://coronavirus-tracker-api.herokuapp.com/v2/locations";
    private Gson gson = GsonFactory.getGson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton micButton = findViewById(R.id.micButton);
        speechText = findViewById(R.id.speechText);
        validateOS();

        recyclerView = findViewById(R.id.countryList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        if(list.size()==0)loadRecyclerViewData();
        final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration("3e0681d619ac43c9a55f1032267c1677",
                ai.api.android.AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System) ;
        aiService = ai.api.android.AIService.getService(this,config);
        aiService.setListener(this);

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.micButton) {
                    aiService.startListening();
                }
            }
        });

//        micButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(intent, 10);
//                } else
//                    Toast.makeText(getApplicationContext(), "Your Device doesn't support this feature", Toast.LENGTH_SHORT).show();
//            }
//        });
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
    private void validateOS() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO);
        }
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

    @Override
    public void onResult(AIResponse result) {
        
        Result result1 = result.getResult();

        final StringBuilder str = new StringBuilder();
        for (int i=0;i<result1.getFulfillment().getMessages().size();i++){
            ResponseMessage.ResponseSpeech responseMessage = (ResponseMessage.ResponseSpeech) result1.getFulfillment().getMessages().get(i);
            str.append(responseMessage.getSpeech().get(0));
        }
        // show result in textview
        speechText.setText(str.toString());
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.speak(str.toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onError(AIError error) {
        speechText.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
