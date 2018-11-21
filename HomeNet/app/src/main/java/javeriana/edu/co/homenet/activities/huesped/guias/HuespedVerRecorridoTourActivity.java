package javeriana.edu.co.homenet.activities.huesped.guias;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.utils.DataParser;

public class HuespedVerRecorridoTourActivity extends FragmentActivity implements OnMapReadyCallback {

    final static int RADIUS_OF_EARTH_KM = 6371;

    private GoogleMap mMap;

    private MarkerOptions actualMarkerOptions;
    private Marker actualMarker;
    private Marker myMarker;
    private MarkerOptions myMarkerOptions;
    private Polyline path;

    private Bundle b;
    private ArrayList<String> lats;
    private ArrayList<String> longs;
    private ArrayList<String> titulos;
    private ArrayList<String> descripciones;
    private ArrayList<LatLng> points;
    private PolylineOptions lineOptions;
    private LatLng positionOrigin;
    private LatLng positionDest;
    private int i = 0;

    public HuespedVerRecorridoTourActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_ver_recorrido_tour);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        b = getIntent().getExtras();
        lats = b.getStringArrayList("latitudes");
        longs = b.getStringArrayList("longitudes");
        titulos = b.getStringArrayList("titulos");
        descripciones = b.getStringArrayList("descripciones");

        myMarkerOptions = new MarkerOptions();

        actualMarkerOptions = new MarkerOptions();
        actualMarkerOptions
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
                .title(titulos.get(0));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        marcarParadas();

    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(5000); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void marcarParadas () {
        MarkerOptions primerMarcador = new MarkerOptions();
        primerMarcador = actualMarkerOptions;
        positionOrigin = new LatLng(Double.valueOf(lats.get(0)), Double.valueOf(longs.get(0)));
        primerMarcador.position(positionOrigin);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(positionOrigin));
        for (; i < titulos.size()-1; i++){
            positionDest = new LatLng(Double.valueOf(lats.get(i)), Double.valueOf(longs.get(i)));
            if (mMap != null) {
                Log.i("COCU1", String.valueOf(titulos.size()));
                Log.i("COCU2", String.valueOf(i)+"==>"+positionOrigin.toString()
                +"/"+positionDest.toString());
                drawPath(positionOrigin, positionDest);
                positionOrigin = positionDest;
            }
        }
    }

    private void drawPath(LatLng origin, LatLng destination){
        if(path != null){
            path.remove();
        }
        // Getting URL to the Google Directions API
        String url = getUrl(origin, destination);
        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        //String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        //String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + R.string.google_directions_key;
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + "AIzaSyAchBP3BNVvqSHTbu02DA0PQur2d_RKE_M";

        return url;
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*100.0)/100.0;
    }

    private String distanceTo(double lat1, double lat2, double lat3, double lat4){
        double dist = distance(lat1,lat2,lat3,lat4);
        return "Distancia: " + String.valueOf(dist)+ " km.";
    }

    private String pathDistance(){
        if(points != null){
            return "Distancia: " +
                    String.valueOf(Math.round(SphericalUtil.computeLength(points))/1000.0)+ " km.";
        }
        return "Distancia: Error km.";
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            // Traversing through all the routes
            for (int i = 0; i < 1/*result.size()*/; i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(6);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null && myMarkerOptions!=null && positionDest!=null && mMap != null) {

                myMarkerOptions.position(positionDest);
                myMarkerOptions.title(titulos.get(i));
                myMarkerOptions.snippet(pathDistance());
                myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
                myMarker = mMap.addMarker(myMarkerOptions);
                path = mMap.addPolyline(lineOptions);
                myMarkerOptions.position(positionOrigin);
                myMarkerOptions.title(titulos.get(i-1));
                myMarkerOptions.snippet(pathDistance());
                myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
                myMarker = mMap.addMarker(myMarkerOptions);
                path = mMap.addPolyline(lineOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionDest, 15));
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
}
