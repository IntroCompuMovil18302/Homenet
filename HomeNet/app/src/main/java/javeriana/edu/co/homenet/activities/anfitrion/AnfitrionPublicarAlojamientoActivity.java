package javeriana.edu.co.homenet.activities.anfitrion;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.BufferedReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.fragment.AnfitrionDatePickerFragment;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Ubicacion;

public class AnfitrionPublicarAlojamientoActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private final static int PLACE_PICKER_REQUEST = 999;
    Alojamiento alojamiento;
    Spinner spinner;

    public static final int FLAG_START_DATE = 0;
    public static final int FLAG_END_DATE = 1;
    int flag;

    String tipoAlojamiento;

    Button ubicacion;
    Button publicar;
    Button fechaIni;
    Button fechaFin;

    EditText valor;
    EditText descripcion;
    EditText fInicio;
    EditText fFin;

    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_publicar_alojamiento);



        alojamiento = new Alojamiento();

        // inflar variables
        spinner = findViewById(R.id.spTipoAPA);

        ubicacion = findViewById(R.id.btUbicacionAPA);
        publicar = findViewById(R.id.btPublicarAPA);
        valor = findViewById(R.id.etValorAPA);
        descripcion = findViewById(R.id.etDescripcionAPA);
        fechaIni = findViewById(R.id.btFechaIniAPA);
        fechaFin = findViewById(R.id.btFechaFinAPA);
        fInicio = findViewById(R.id.etFechaIniAPA);
        fFin = findViewById(R.id.etFechaFinAPA);
        rv = findViewById(R.id.rvDispFechasAPA);


        // spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipoAlojamiento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        // recyclerView

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

        fechaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = FLAG_START_DATE;
                DialogFragment datePickerIni = new AnfitrionDatePickerFragment();
                datePickerIni.show(getSupportFragmentManager(), "date picker");
            }
        });

        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = FLAG_END_DATE;
                DialogFragment datePickerIni = new AnfitrionDatePickerFragment();
                datePickerIni.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar  c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat firstDateFormat = new SimpleDateFormat("dd/MM/yyyy");


        if(flag == FLAG_END_DATE)
        {
            String currentDate = firstDateFormat.format(c.getTime());
            //String currentDate = DateFormat.getDateInstance().format(c.getTime());
            fFin.setText(currentDate);
        }
        else {
            String currentDate = firstDateFormat.format(c.getTime());
            //String currentDate = DateFormat.getDateInstance().format(c.getTime());
            fInicio.setText(currentDate);
        }


    }
    // FIN seccion botones ---------------------------------------------------------
}