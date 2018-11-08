package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.utils.DateFormater;

public class HuespedResultadosMapActivity extends AppCompatActivity implements OnMapReadyCallback  {

    public static final String PATH_ALO="Alojamientos/";
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    final static int REQUEST_CHECK_SETTINGS = 2;
    public static final double lowerLeftLatitude = 4.497712;
    public static final double lowerLeftLongitude = -74.242971;
    public static final double upperRightLatitude = 4.763589;
    public static final double upperRigthLongitude = -74.003313;

    Button buttonLocation;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location location;
    private LatLng qLocation;
    private GoogleMap mMap;
    private Geocoder mGeocoder;

    private MarkerOptions actualMarkerOptions;
    private Marker actualMarker;
    private MarkerOptions placeMarkerOptions;
    private Marker placeMarker;
    private MarkerOptions hotelMarkerOptions;
    private CircleOptions circleOptions;
    private Circle circle;
    private Double radius;

    private Bundle bundle;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_resultados_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGeocoder = new Geocoder(getBaseContext());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference(PATH_ALO);

        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION,
                "Se necesita acceder a los ubicacion", MY_PERMISSIONS_REQUEST_LOCATION);

        bundle = getIntent().getBundleExtra("bundle");
        if(bundle.get("distancia").toString().equals("")){
            radius = 2000.0;
        }
        else{
            radius = Double.parseDouble(bundle.get("distancia").toString())*1000;
        }

        if (!bundle.get("lugar").toString().equals("")) {
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(bundle.get("lugar").toString(), 2,
                        lowerLeftLatitude, lowerLeftLongitude,
                        upperRightLatitude, upperRigthLongitude);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    //LatLng position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                    qLocation = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                                if (matchAlojamiento(ialojamiento)){
                                    hotelMarkerOptions
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.apartment_marker));
                                    hotelMarkerOptions.title(
                                            ialojamiento.getNombre() + "\n"+
                                                    String.valueOf(ialojamiento.getDist()) + " km."
                                    );
                                    hotelMarkerOptions.snippet(
                                            ialojamiento.getId()
                                    );
                                    hotelMarkerOptions.position(
                                            new LatLng(ialojamiento.getUbicacion().getLatitude(),ialojamiento.getUbicacion().getLongitude())
                                    );
                                    mMap.addMarker(hotelMarkerOptions);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("Firebase database", "error en la consulta", databaseError.toException());
                        }
                    });
                } else {Toast.makeText(HuespedResultadosMapActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            qLocation = null;
        }
        hotelMarkerOptions = new MarkerOptions();

        actualMarkerOptions = new MarkerOptions();
        actualMarkerOptions
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.person_marker))
                .title("Me");

        placeMarkerOptions = new MarkerOptions();
        placeMarkerOptions
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.place_marker))
                .title(bundle.get("lugar").toString());

        circleOptions = new CircleOptions();
        circleOptions.radius(radius)
                .strokeWidth(10)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(128, 127, 0, 0))
                .clickable(true);

        mLocationRequest = createLocationRequest();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLastLocation();
                if (mMap != null) {
                    if(qLocation != null){
                        if (placeMarker != null){
                            placeMarker.setPosition(qLocation);
                        }
                        else{
                            placeMarkerOptions.position(qLocation);
                            placeMarker = mMap.addMarker(placeMarkerOptions);
                        }
                        if(circle != null){
                            circle.setCenter(qLocation);
                        }
                        else{
                            circleOptions.center(qLocation);
                            circle = mMap.addCircle(circleOptions);
                        }
                    }
                    if(location != null){
                        LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        if (actualMarker != null){
                            actualMarker.setPosition(actualPosition);
                        }
                        else{
                            actualMarkerOptions.position(actualPosition);
                            actualMarker = mMap.addMarker(actualMarkerOptions);
                        }
                        if(qLocation == null){
                            if(circle != null){
                                circle.setCenter(actualPosition);
                            }
                            else{
                                circleOptions.center(actualPosition);
                                circle = mMap.addCircle(circleOptions);
                            }
                            if(firstTime == true){
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(actualPosition));
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                            Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                                            if (matchAlojamiento(ialojamiento)){
                                                hotelMarkerOptions
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.apartment_marker));
                                                hotelMarkerOptions.title(
                                                        ialojamiento.getNombre() + "\n"+
                                                                String.valueOf(ialojamiento.getDist()) + " km."
                                                );
                                                hotelMarkerOptions.snippet(
                                                        ialojamiento.getId()
                                                );
                                                hotelMarkerOptions.position(
                                                        new LatLng(ialojamiento.getUbicacion().getLatitude(),ialojamiento.getUbicacion().getLongitude())
                                                );
                                                mMap.addMarker(hotelMarkerOptions);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.w("Firebase database", "error en la consulta", databaseError.toException());
                                    }
                                });
                                firstTime = false;
                            }

                        }
                    }
                }
            }
        };

        turnLocation();


        buttonLocation = findViewById(R.id.btLocationHRM);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location != null && mMap != null) {
                    LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(actualPosition));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private boolean matchAlojamiento(Alojamiento alojamiento){
        alojamiento.initDist(0,0);
        boolean match = true;
        if(qLocation != null){
            alojamiento.initDist(qLocation.latitude,qLocation.longitude);
            match = match && alojamiento.estaCerca(qLocation.latitude,qLocation.longitude,
                    radius/1000);
        }
        else if(location!=null){
            alojamiento.initDist(location.getLatitude(),location.getLongitude());
            match = match && alojamiento.estaCerca(location.getLatitude(), location.getLongitude(),
                    radius/1000);
        }

        if(!bundle.getString("keyword").equals("")){
            match =  match && alojamiento.tienePalabra(bundle.getString("keyword"));
        }
        if(!bundle.getString("fechaInicio").equals("") && !bundle.getString("fechaFin").equals("")){
            match = match && alojamiento.estaDisponible(bundle.getString("fechaInicio"),
                    bundle.getString("fechaFin"));
        }
        return match;
    }

    private void changeMap(){
        mMap.setMapStyle(MapStyleOptions
                .loadRawResourceStyle(this, R.raw.day_map));
        /*if(DateFormater.getHourOftheDay() <= 6 || DateFormater.getHourOftheDay() >= 18){
            mMap.setMapStyle(MapStyleOptions
                    .loadRawResourceStyle(this, R.raw.night_map));
        }
        else{

        }*/
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
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if(qLocation !=  null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(qLocation));
        }
        else if (location!=null){
            LatLng actual = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(actual));
        }
        if(radius>60000){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(7));
        }
        if(radius>=30000 && radius <= 60000){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(9));
        }
        if(radius>=10000 && radius <= 29999){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
        }
        if(radius>=2000 && radius <= 9999){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        }
        if(radius<=1999){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        }
        changeMap();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                String uid = m.getSnippet();
                if(uid!=null){
                    //m.setSnippet("");
                    Bundle b = new Bundle();
                    b.putString("idAloj", uid);
                    startActivity(new Intent(getApplicationContext(),HuespedInformacionAlojamientoActivity.class).putExtras(b));
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut){
            mAuth.signOut();
            Intent intent = new Intent(HuespedResultadosMapActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (itemClicked == R.id.menuSettings){
            //Abrir actividad para configuración etc
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPermission(Activity context, String permission, String explanation, int requestId ){
        if (ContextCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?   
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,permission)) {
                Toast.makeText(context, explanation, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION : {
                //loadLocation();
                break;
            }
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(5000); //máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void startLocationUpdates(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, null);
        }
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void turnLocation(){
        LocationSettingsRequest.Builder builder = new
                LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates(); //Todas las condiciones para recibir localizaciones
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {// Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(HuespedResultadosMapActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        } break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates(); //Se encendió la localización!!!
                } else {
                    Toast.makeText(this,
                            "Sin acceso a localización, hardware deshabilitado!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
