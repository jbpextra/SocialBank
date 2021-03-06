package me.integrate.socialbank;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ConvertAddressToCoordinatesTask extends AsyncTask<URL,Integer,EventLocation > {
    @Override
    protected EventLocation doInBackground(URL... urls) {
        EventLocation eventLocation;
        try {

            HttpURLConnection conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 500) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;StringBuilder full = new StringBuilder();
            while ((output = br.readLine()) != null) {
                full.append(output);
            }


            JSONObject json = new JSONObject(full.toString());


            JSONObject results = json.getJSONArray("results").getJSONObject(0);
            String address = results.getString("formatted_address");
            JSONObject coordinates = results.getJSONObject("geometry").getJSONObject("location");
            String longitude = coordinates.getString("lng");
            String latitude = coordinates.getString("lat");


            eventLocation = new EventLocation(address, Double.parseDouble(latitude), Double.parseDouble(longitude));
            return eventLocation;


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
