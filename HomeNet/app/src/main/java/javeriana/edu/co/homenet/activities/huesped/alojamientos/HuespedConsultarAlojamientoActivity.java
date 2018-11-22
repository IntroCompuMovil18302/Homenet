package javeriana.edu.co.homenet.activities.huesped.alojamientos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.ArrayList;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.AlojamientoAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.utils.DateFormater;

public class HuespedConsultarAlojamientoActivity extends AppCompatActivity {

    EditText palabra;
    EditText fechaInicio;
    EditText fechaFin;
    EditText distancia;
    EditText lugar;
    EditText huespedes;
    EditText dormitorios;
    Button buttonBuscar;
    Button buttonMapa;
    CheckBox calefaccion;
    CheckBox internet;
    CheckBox television;
    CheckBox mascotas;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_consultar_alojamiento);

        mAuth = FirebaseAuth.getInstance();

        palabra = findViewById(R.id.etPalabraHCA);
        fechaInicio = findViewById(R.id.etFechaInicioHCA);
        fechaFin = findViewById(R.id.etFechaFinHCA);
        distancia = findViewById(R.id.etDistanciaHCA);
        lugar = findViewById(R.id.etLugarHCA);
        huespedes = findViewById(R.id.etHuespedesHCA);
        dormitorios = findViewById(R.id.etDormitoriosHCA);
        buttonBuscar = findViewById(R.id.btBuscarHCA);
        buttonMapa = findViewById(R.id.btMapaHCA);
        calefaccion = findViewById(R.id.cbCalefaccionHCA);
        internet = findViewById(R.id.cbInternetHCA);
        television = findViewById(R.id.cbTelevisionHCA);
        mascotas = findViewById(R.id.cbMascotasHCA);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fechaInicio.getText().toString().equals("") || !fechaFin.getText().toString().equals("")){
                    if (isValidDate(fechaInicio.getText().toString())){
                        if (isValidDate(fechaFin.getText().toString())){
                            if(isValidStartEndDate(fechaInicio.getText().toString(),
                                    fechaFin.getText().toString())){
                                Intent intent = new Intent(view.getContext(), HuespedResultadosListActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("keyword",palabra.getText().toString());
                                bundle.putString("fechaInicio",fechaInicio.getText().toString());
                                bundle.putString("fechaFin",fechaFin.getText().toString());
                                bundle.putString("distancia",distancia.getText().toString());
                                bundle.putString("lugar",lugar.getText().toString());
                                bundle.putString("huespedes",huespedes.getText().toString());
                                bundle.putString("dormitorios",dormitorios.getText().toString());
                                bundle.putBoolean("calefaccion",calefaccion.isChecked());
                                bundle.putBoolean("internet",internet.isChecked());
                                bundle.putBoolean("television",television.isChecked());
                                bundle.putBoolean("mascotas",mascotas.isChecked());
                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                            }
                            else{
                                fechaFin.setError("Fecha inválida");
                                fechaInicio.setError("Fecha inválida");
                            }

                        }else{
                            fechaFin.setError("Fecha inválida");
                        }
                    }else{
                        fechaInicio.setError("Fecha inválida");
                    }
                }
                else{
                    Intent intent = new Intent(view.getContext(), HuespedResultadosListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("keyword",palabra.getText().toString());
                    bundle.putString("fechaInicio",fechaInicio.getText().toString());
                    bundle.putString("fechaFin",fechaFin.getText().toString());
                    bundle.putString("distancia",distancia.getText().toString());
                    bundle.putString("lugar",lugar.getText().toString());
                    bundle.putString("huespedes",huespedes.getText().toString());
                    bundle.putString("dormitorios",dormitorios.getText().toString());
                    bundle.putBoolean("calefaccion",calefaccion.isChecked());
                    bundle.putBoolean("internet",internet.isChecked());
                    bundle.putBoolean("television",television.isChecked());
                    bundle.putBoolean("mascotas",mascotas.isChecked());
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            }
        });

        buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(!fechaInicio.getText().toString().equals("") || !fechaFin.getText().toString().equals("")){
                    if (isValidDate(fechaInicio.getText().toString())){
                        if (isValidDate(fechaFin.getText().toString())){
                            if(isValidStartEndDate(fechaInicio.getText().toString(),
                                    fechaFin.getText().toString())){
                                Intent intent = new Intent(view.getContext(), HuespedResultadosListActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("keyword",palabra.getText().toString());
                                bundle.putString("fechaInicio",fechaInicio.getText().toString());
                                bundle.putString("fechaFin",fechaFin.getText().toString());
                                bundle.putString("distancia",distancia.getText().toString());
                                bundle.putString("lugar",lugar.getText().toString());
                                intent.putExtra("bundle", bundle);
                                startActivity(intent);
                            }
                            else{
                                fechaFin.setError("Fecha inválida");
                                fechaInicio.setError("Fecha inválida");
                            }
                        }else{
                            fechaFin.setError("Fecha inválida");
                        }
                    }else{
                        fechaInicio.setError("Fecha inválida");
                    }
                }
                else{
                    Intent intent = new Intent(view.getContext(), HuespedResultadosMapActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("keyword",palabra.getText().toString());
                    bundle.putString("fechaInicio",fechaInicio.getText().toString());
                    bundle.putString("fechaFin",fechaFin.getText().toString());
                    bundle.putString("distancia",distancia.getText().toString());
                    bundle.putString("lugar",lugar.getText().toString());
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public static boolean isValidDate(String inDate) {
        if(!inDate.equals("")){
            Log.d("DATE", inDate.substring(inDate.length()-5,inDate.length()));
            if(!inDate.substring(inDate.length()-5,inDate.length()-4).equals("/")){
                return false;
            }
            if(DateFormater.stringToDate(inDate)!=null){
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidStartEndDate(String startDate, String endDate) {
        if(!startDate.equals("")){
            if(DateFormater.stringToDate(startDate).before(DateFormater.today())){
                return false;
            }
        }
        if(!endDate.equals("")){
            if(DateFormater.stringToDate(endDate).before(DateFormater.today())){
                return false;
            }
        }
        if(!startDate.equals("") && !endDate.equals("")){
            if(DateFormater.stringToDate(startDate).after(DateFormater.stringToDate(endDate))){
                return false;
            }
        }
        return true;
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
        }
        return super.onOptionsItemSelected(item);
    }

}
