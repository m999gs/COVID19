package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.Locale;

public class PopActivity extends Activity {
    private TextToSpeech textToSpeech;
    private TextView speechText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_popup);

        speechText2 = findViewById(R.id.speechText2);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        Intent intent = getIntent();

        final String message = (String)intent.getStringExtra("message");

        getWindow().setLayout((int)(width*.8),(int)(width*.7));

        speechText2.setText(message);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null);
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
}
