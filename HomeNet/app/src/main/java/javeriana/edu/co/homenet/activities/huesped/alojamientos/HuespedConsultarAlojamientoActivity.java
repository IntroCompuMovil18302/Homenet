package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.AlojamientoAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Reserva;

public class HuespedConsultarAlojamientoActivity extends AppCompatActivity {

    public static final String PATH_ALO="Alojamientos/";
    public static final String PATH_RESV="Reservas/";
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    final static int REQUEST_CHECK_SETTINGS = 2;

    EditText palabra;
    EditText fechaInicio;
    EditText fechaFin;
    EditText distancia;
    Button buttonBuscar;
    ListView resultadosBusqueda;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_consultar_alojamiento);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION,
                "Se necesita acceder a los ubicacion", MY_PERMISSIONS_REQUEST_LOCATION);

        mLocationRequest = createLocationRequest();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLastLocation();
                //Log.i(“LOCATION", "Location update in the callback: " + location);
            }
        };

        turnLocation();

        palabra = findViewById(R.id.etPalabraHCA);
        fechaInicio = findViewById(R.id.etFechaInicioHCA);
        fechaFin = findViewById(R.id.etFechaFinHCA);
        distancia = findViewById(R.id.etDistanciaHCA);
        buttonBuscar = findViewById(R.id.btBuscarHCA);
        resultadosBusqueda = findViewById(R.id.lvResultadosHCA);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidDate(fechaInicio.getText().toString())){
                    if (isValidDate(fechaFin.getText().toString())) {

                        myRef = database.getReference(PATH_ALO);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<Alojamiento> listAlojamiento = new ArrayList<Alojamiento>();
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Alojamiento ialojamiento = singleSnapshot.getValue(Alojamiento.class);
                                    //List<Reserva> reservas = new ArrayList<Reserva>();
                                    Log.d("RESERVAS LIST", ialojamiento.getReservas().toString());
                                    if (matchAlojamiento(ialojamiento)) {
                                        listAlojamiento.add(ialojamiento);
                                    }
                                }
                                AlojamientoAdapter adapter = new AlojamientoAdapter(HuespedConsultarAlojamientoActivity.this, listAlojamiento);
                                resultadosBusqueda.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.w("Firebase database", "error en la consulta", databaseError.toException());
                            }
                        });
                    }else{
                        fechaFin.setError("Fecha inválida");
                    }
                }else{
                    fechaInicio.setError("Fecha inválida");
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
        if(location!=null){
            alojamiento.initDist(location.getLatitude(),location.getLongitude());
        }
        else{
            alojamiento.initDist(0,0);
        }
        boolean match = true;
        if(!palabra.getText().toString().equals("")){
            match = alojamiento.tienePalabra(palabra.getText().toString());
        }
        if(!distancia.getText().toString().equals("") && location!=null){
            match = alojamiento.estaCerca(location.getLatitude(), location.getLongitude(),
                    Double.parseDouble(distancia.getText().toString()));
        }
        if(!fechaInicio.getText().toString().equals("") && !fechaFin.getText().toString().equals("")){
            match = alojamiento.estaDisponible(fechaInicio.getText().toString(),
                    fechaFin.getText().toString());
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
            Intent intent = new Intent(HuespedConsultarAlojamientoActivity.this, LoginActivity.class);
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
                            resolvable.startResolutionForResult(HuespedConsultarAlojamientoActivity.this, REQUEST_CHECK_SETTINGS);
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

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

}
