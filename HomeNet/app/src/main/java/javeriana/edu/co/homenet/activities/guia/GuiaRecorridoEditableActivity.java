package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Ubicacion;
import javeriana.edu.co.homenet.utils.Punto;
import javeriana.edu.co.homenet.utils.UtilidadesMapa;

public class GuiaRecorridoEditableActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final double lowerLeftLatitude = 4.497712;
    public static final double lowerLeftLongitude = -74.242971;
    public static final double upperRightLatitude = 4.763589;
    public static final double upperRigthLongitude = -74.003313;

    private GoogleMap mMap;

    private List<Ubicacion> ubicaciones = new ArrayList<>();
    private ArrayList<String> titulos = new ArrayList<>();
    private ArrayList<String> lats = new ArrayList<>();
    private ArrayList<String> longs = new ArrayList<>();
    private LatLng center;
    private ArrayList<LatLng> points;
    private PolylineOptions lineOptions;
    private Geocoder mGeocoder;

    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    private Button addStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_recorrido_editable);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addStop = findViewById(R.id.btAddGRE);

        mGeocoder = new Geocoder(getBaseContext());
        UtilidadesMapa.routes.clear();

        addStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putStringArrayList("lats",lats);
                b.putStringArrayList("longs",longs);
                b.putStringArrayList("titulos",titulos);
                startActivity(new Intent(GuiaRecorridoEditableActivity.this, GuiaCrearTourActivity.class).putExtras(b));
            }
        });
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
        // Add a marker in Sydney and move the camera
        request= Volley.newRequestQueue(getApplicationContext());
        LatLng bogota = new LatLng(4.6097102, -74.081749);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
        getPointsFromMap();
    }

    private void marcarParadas () {
        lineOptions = new PolylineOptions();

        for(int i = 0; i<UtilidadesMapa.routes.size(); i++){

            points = new ArrayList<LatLng>();

            // Obteniendo el detalle de la ruta
            List<HashMap<String, String>> path = UtilidadesMapa.routes.get(i);

            // Obteniendo todos los puntos y/o coordenadas de la ruta
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                if (center == null) {
                    //Obtengo la 1ra coordenada para centrar el mapa en la misma.
                    center = new LatLng(lat, lng);
                }
                points.add(position);
            }

            // Agregamos todos los puntos en la ruta al objeto LineOptions
            lineOptions.addAll(points);
            //Definimos el grosor de las Polilíneas
            lineOptions.width(10);
            //Definimos el color de la Polilíneas
            lineOptions.color(Color.BLUE);
        }
        // Dibujamos las Polilineas en el Google Map para cada ruta
        mMap.addPolyline(lineOptions);
    }

    private String busquedaParada (LatLng ll) {
        try {
            List<Address> addresses = mGeocoder.getFromLocation(ll.latitude, ll.longitude, 2);
            if (addresses != null && !addresses.isEmpty()) {
                Address addressResult = addresses.get(0);
                return addressResult.toString();
            } else {Toast.makeText(GuiaRecorridoEditableActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void buscarRutas (LatLng l1, LatLng l2){
        Punto p1 = new Punto();
        Punto p2 = new Punto();
        p1.setLatitudInicial(l1.latitude);
        p1.setLongitudInicial(l1.longitude);
        UtilidadesMapa.coordenadas.add(p1);
        p2.setLatitudFinal(l2.latitude);
        p2.setLongitudFinal(l2.longitude);
        UtilidadesMapa.coordenadas.add(p2);
        webServiceObtenerRuta(String.valueOf(l1.latitude), String.valueOf(l1.longitude),
                String.valueOf(l2.latitude), String.valueOf(l2.longitude));
    }

    private void webServiceObtenerRuta(String latitudInicial, String longitudInicial, String latitudFinal, String longitudFinal) {

        final String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudInicial + "," + longitudInicial
                + "&destination=" + latitudFinal + "," + longitudFinal + "&key=AIzaSyAchBP3BNVvqSHTbu02DA0PQur2d_RKE_M";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Este método PARSEA el JSONObject que retorna del API de Rutas de Google devolviendo
                //una lista del lista de HashMap Strings con el listado de Coordenadas de Lat y Long,
                //con la cual se podrá dibujar pollinas que describan la ruta entre 2 puntos.
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;
                try {

                    jRoutes = response.getJSONArray("routes");


                    /** Traversing all routes */
                    for (int i = 0; i < jRoutes.length(); i++) {
                        jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                        List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                        /** Traversing all legs */
                        for (int j = 0; j < jLegs.length(); j++) {
                            jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                            /** Traversing all steps */
                            for (int k = 0; k < jSteps.length(); k++) {
                                String polyline = "";
                                polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = decodePoly(polyline);

                                /** Traversing all points */
                                for (int l = 0; l < list.size(); l++) {
                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                                    hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                                    path.add(hm);
                                }
                            }
                            UtilidadesMapa.routes.add(path);
                            marcarParadas();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.d("ERROR: ", error.toString());
            }
        }
        );

        request.add(jsonObjectRequest);
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void getPointsFromMap(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // Already two locations
                // Adding new item to the ArrayList
                Ubicacion u = new Ubicacion();
                u.setLatitude(point.latitude);
                u.setLongitude(point.longitude);
                u.setTitulo(busquedaParada(point));
                u.setDescripcion(" ");
                ubicaciones.add(u);
                lats.add(String.valueOf(u.getLatitude()));
                longs.add(String.valueOf(u.getLongitude()));
                titulos.add(u.getTitulo());
                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
                // Setting the position of the marker
                options.position(point);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);
                // Checks, whether start and end locations are captured
                if (ubicaciones.size() >= 2){
                    LatLng inicial = new LatLng(ubicaciones.get(ubicaciones.size()-2).getLatitude(),
                            ubicaciones.get(ubicaciones.size()-2).getLongitude());
                    LatLng finalT =  new LatLng(ubicaciones.get(ubicaciones.size()-1).getLatitude(),
                            ubicaciones.get(ubicaciones.size()-1).getLongitude());
                    buscarRutas(inicial, finalT);
                }
            }
        });
    }


}
