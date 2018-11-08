package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.List;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.AlojamientoAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;

public class HuespedResultadosListActivity extends AppCompatActivity {

    public static final String PATH_ALO="Alojamientos/";
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    final static int REQUEST_CHECK_SETTINGS = 2;
    public static final double lowerLeftLatitude = 4.497712;
    public static final double lowerLeftLongitude = -74.242971;
    public static final double upperRightLatitude = 4.763589;
    public static final double upperRigthLongitude = -74.003313;

    ListView resultadosBusqueda;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location location;
    private LatLng qLocation;
    private Geocoder mGeocoder;

    private Bundle bundle;
    private ArrayList<Alojamiento> listAlojamiento;
    private Double radius;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_resultados_list);

        resultadosBusqueda = findViewById(R.id.lvResultadosHRL);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mGeocoder = new Geocoder(getBaseContext());

        myRef = database.getReference(PATH_ALO);

        bundle = getIntent().getBundleExtra("bundle");
        if(bundle.get("distancia").toString().equals("")){
            radius = 2000.0;
        }
        else{
            radius = Double.parseDouble(bundle.get("distancia").toString())*1000;
        }

        listAlojamiento = new ArrayList<Alojamiento>();

        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION,
                "Se necesita acceder a los ubicacion", MY_PERMISSIONS_REQUEST_LOCATION);

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
                            listAlojamiento.clear();
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                                if (matchAlojamiento(ialojamiento)){
                                    listAlojamiento.add(ialojamiento);
                                }
                            }
                            AlojamientoAdapter adapter = new AlojamientoAdapter(HuespedResultadosListActivity.this, listAlojamiento);
                            resultadosBusqueda.setAdapter(adapter);
                            resultadosBusqueda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Bundle b = new Bundle();
                                    b.putString("idAloj", listAlojamiento
                                            .get(position).getId());
                                    startActivity(new Intent(view.getContext(),HuespedInformacionAlojamientoActivity.class).putExtras(b));
                                }
                            });
                            Toast.makeText(HuespedResultadosListActivity.this, "Dirección encontrada", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("Firebase database", "error en la consulta", databaseError.toException());
                        }
                    });
                } else {Toast.makeText(HuespedResultadosListActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            qLocation = null;
        }

        mLocationRequest = createLocationRequest();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLastLocation();
                if(location != null && qLocation == null){
                    if(firstTime == true){
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                listAlojamiento.clear();
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                                    if (matchAlojamiento(ialojamiento)){
                                        listAlojamiento.add(ialojamiento);
                                    }
                                }
                                AlojamientoAdapter adapter = new AlojamientoAdapter(HuespedResultadosListActivity.this, listAlojamiento);
                                resultadosBusqueda.setAdapter(adapter);
                                resultadosBusqueda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Bundle b = new Bundle();
                                        b.putString("idAloj", listAlojamiento
                                                .get(position).getId());
                                        startActivity(new Intent(view.getContext(),HuespedInformacionAlojamientoActivity.class).putExtras(b));
                                    }
                                });
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
        };

        turnLocation();

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
            Intent intent = new Intent(HuespedResultadosListActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
                            resolvable.startResolutionForResult(HuespedResultadosListActivity.this, REQUEST_CHECK_SETTINGS);
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
