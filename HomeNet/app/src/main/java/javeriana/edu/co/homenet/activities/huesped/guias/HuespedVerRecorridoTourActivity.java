package javeriana.edu.co.homenet.activities.huesped.guias;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.utils.UtilidadesMapa;

public class HuespedVerRecorridoTourActivity extends FragmentActivity implements OnMapReadyCallback {

    final static int RADIUS_OF_EARTH_KM = 6371;

    private GoogleMap mMap;

    private Bundle b;
    private ArrayList<String> lats;
    private ArrayList<String> longs;
    private ArrayList<String> titulos;
    private ArrayList<String> descripciones;

    LatLng center;
    ArrayList<LatLng> points;
    PolylineOptions lineOptions;

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

        center = null;
        points = null;
        lineOptions = null;

        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (titulos.size() > 1) {
            marcarParadas();
        }else{
            LatLng p = new LatLng(Double.valueOf(lats.get(0)), Double.valueOf(longs.get(0)));
            mMap.addMarker(new MarkerOptions().position(p).title(titulos.get(0)))
            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
        }
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

        for (int i=0; i< titulos.size()-1; i++){
            mMap.addPolyline(lineOptions);

            LatLng origen = new LatLng(Double.valueOf(lats.get(i)), Double.valueOf(longs.get(i)));
            mMap.addMarker(new MarkerOptions().position(origen)
                    .title(titulos.get(i)))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag));

            LatLng destino = new LatLng(Double.valueOf(lats.get(i+1)), Double.valueOf(longs.get(i+1)));
            mMap.addMarker(new MarkerOptions().position(destino)
                    .title(titulos.get(i+1)))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 15));
        }
        /////////////

    }
}
