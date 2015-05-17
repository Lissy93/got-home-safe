package net.as93.homesafe;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.as93.homesafe.autocomplete.PlaceJsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;


public class CreateSchedule extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener {

    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = R.string.str_atv_places;

    public String TAG = "debug duck says: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CreateSchedule act = this;

        mGoogleApiClient = new GoogleApiClient.Builder(CreateSchedule.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();

        atvPlaces = (AutoCompleteTextView) findViewById(R.id.placesAutocomplete);
        atvPlaces.setThreshold(1);
        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask(act);
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            Log.d(TAG, "Exception while downloading url"+e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public void updateMap(final String placeId){
        Log.i(TAG, " starting place lookup");
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {

                            // Get place, name and lat+long
                            final Place myPlace = places.get(0);
                            Log.i(TAG, "Place found: " + myPlace.getName());
                            String placeName = myPlace.getName().toString();
                            LatLng latlng = myPlace.getLatLng();

                            // Select the map then clear it of previous markers
                            GoogleMap map = ((SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.mapFragmentInner)).getMap();
                            map.clear();

                            // Add new marker for the selected place
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(placeName));

                            // Zoom into the new marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(15.0f).build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            map.moveCamera(cameraUpdate);

                            // Hide the soft keyboard
                            EditText myEditText = (EditText) findViewById(R.id.placesAutocomplete);
                            InputMethodManager imm = (InputMethodManager)getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
                        }
                        places.release();
                    }
                });

    }



    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{
        CreateSchedule mActivity;

        public PlacesTask(CreateSchedule activity) {
            mActivity = activity;
        }

        @Override
        protected String doInBackground(String... place) {
            String data = "";
            String key = "key=AIzaSyC6NOgE2-jDwRWeEf0Wy_3rKLtyhQcrbVI";
            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String types = "types=geocode";
            String sensor = "sensor=false";
            String parameters = input+"&"+types+"&"+sensor+"&"+key;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{ //Fetch data from google web services
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            parserTask = new ParserTask(mActivity);
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
        JSONObject jObject;
        CreateSchedule mActivity;

        public ParserTask(CreateSchedule activity) {
            mActivity = activity;
        }


        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            PlaceJsonParser placeJsonParser = new PlaceJsonParser();
            try{
                jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
            atvPlaces.setAdapter(adapter);

            atvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    // Find place id
                    String placeId = "";
                    HashMap mMap = (HashMap)arg0.getAdapter().getItem(arg2);
                    for (Object o : mMap.keySet()) {
                        String key = (String) o;
                        String value = (String) mMap.get(key);
                        if(key.equals("_id")){
                            placeId = value;
                        }
                    }
                    mActivity.updateMap(placeId); // Call update map, and pass place id
                }
            });
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }


}

