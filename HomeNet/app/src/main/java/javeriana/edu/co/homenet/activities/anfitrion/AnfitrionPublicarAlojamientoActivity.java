package javeriana.edu.co.homenet.activities.anfitrion;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.BufferedReader;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Ubicacion;

public class AnfitrionPublicarAlojamientoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private final static int PLACE_PICKER_REQUEST = 999;
    Alojamiento alojamiento;
    Spinner spinner;

    String tipoAlojamiento;

    Button agregarImg;
    Button ubicacion;
    Button publicar;

    EditText valor;
    EditText descripcion;

    RecyclerView rvImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_publicar_alojamiento);

        alojamiento = new Alojamiento();

        // inflar variables
        spinner = findViewById(R.id.spTipoAPA);
        agregarImg = findViewById(R.id.btSubirImgAPA);
        ubicacion = findViewById(R.id.btUbicacionAPA);
        publicar = findViewById(R.id.btPublicarAPA);
        valor = findViewById(R.id.etValorAPA);
        descripcion = findViewById(R.id.etDescripcionAPA);
        rvImagenes = findViewById(R.id.rvImagenesAlojAPA);

        // spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipoAlojamiento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        // recyclerView
        rvImagenes.setHasFixedSize(true);
        rvImagenes.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        accionBotones();
    }


    // Seccion spinner -----------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       alojamiento.setTipo( tipoAlojamiento = adapterView.getItemAtPosition(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        alojamiento.setTipo("indeterminado");
    }
    // FIN Seccion spinner -----------------------------------------------


    // seccion recycler view imagenes--------------------------------------------


    // FIN seccion recycler view imagenes--------------------------------------------

    // seccion recycler view fechas--------------------------------------------


    // FIN seccion recycler view fechas--------------------------------------------


    // seccion place picker ----------------------------------------------------------
    public void onActivityResult(int requestCode, int ResultCode, Intent data){

        if (requestCode == PLACE_PICKER_REQUEST)
        {
            if(ResultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data,this);
                String direccion = String.format("%s",place.getAddress());
                String nombre = String.format("%s",place.getName());
                System.out.println("---------------------Name:"+ nombre);
                System.out.println("---------------------Ess:"+ direccion);
                double latitud = place.getLatLng().latitude;
                double longitud = place.getLatLng().longitude;
                Ubicacion u = new Ubicacion();
                u.setLatitude(latitud);
                u.setLongitude(longitud);
                alojamiento.setUbicacion(u);
                //listDatos.add(u);
                System.out.println("---------------------AAAAAA:"+ latitud);
                //adapter.addItem(u);

            }
        }
    }

    // FIN seccion place picker ----------------------------------------------------------


    // seccion botones ---------------------------------------------------------
    public void accionBotones() {
        agregarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ENTRO acccion boton");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar imagenes"), RESULT_LOAD_IMAGE);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alojamiento.setDescripcion(descripcion.getText().toString());
                alojamiento.setPrecio(Long.parseLong(valor.getText().toString()));

            }
        });

        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(AnfitrionPublicarAlojamientoActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // FIN seccion botones ---------------------------------------------------------
}