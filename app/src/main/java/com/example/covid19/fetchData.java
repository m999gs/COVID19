package com.example.covid19;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class fetchData extends AsyncTask<Void,Void,Void> {
    private StringBuilder data= new StringBuilder();
    private static ArrayList<D> webList;
    @Override
    protected Void doInBackground(Void... voids) {
        try{

            //Fetching the data from the given URL in a String called data.
            URL url = new URL("https://coronavirus-tracker-api.herokuapp.com/v2/locations");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            webList=new ArrayList<>();

            while (line!=null){
                line = bufferedReader.readLine();
                data.append(line);
            }


            JSONObject jsonObject= new JSONObject(data.toString());
            JSONObject temp = (JSONObject) jsonObject.get("latest");
            webList.add(new D(
                    "WorldWide",
                    temp.get("confirmed")+"",
                    temp.get("deaths")+"",
                    temp.get("recovered")+""
            ));


        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MainActivity.list = webList;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}
