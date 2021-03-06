package javeriana.edu.co.homenet.activities.anfitrion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;

import javeriana.edu.co.homenet.adapters.AnfPubAlojamientoDispAdapter;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Disponibilidad;
import javeriana.edu.co.homenet.models.Ubicacion;
import javeriana.edu.co.homenet.services.CalificacionAlojamientoService;
import javeriana.edu.co.homenet.services.ReservasService;


public class AnfitrionPublicarAlojamientoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static int PLACE_PICKER_REQUEST = 999;
    ArrayList<Disponibilidad> disponibilidads;
    AnfPubAlojamientoDispAdapter adapterDispo;

    Alojamiento alojamiento;
    Spinner spinner;

    public static final int FLAG_START_DATE = 0;
    public static final int FLAG_END_DATE = 1;
    int flag;
    int modo;
    String tipoAlojamiento;
    private ProgressDialog nProgressDialog;

    Button ubicacion;
    Button siguiente;
    Button fechaIni;
    Button fechaFin;
    Button agregarDisp;

    EditText valor;
    EditText descripcion;
    EditText fInicio;
    EditText fFin;
    EditText nombre;

    TextView direcciontv;

    RecyclerView rv;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_pub_general);




        // inflar variables
        spinner = findViewById(R.id.spTipoAPA);

        nProgressDialog = new ProgressDialog(AnfitrionPublicarAlojamientoActivity.this);

        ubicacion = findViewById(R.id.btUbicacionAPA);
        siguiente = findViewById(R.id.btSiguienteAPA);
        valor = findViewById(R.id.etValorAPA);
        descripcion = findViewById(R.id.etDescripcionAPA);
        fechaIni = findViewById(R.id.btFechaIniAPA);
        fechaFin = findViewById(R.id.btFechaFinAPA);
        agregarDisp = findViewById(R.id.btAgregarFechaAPA);
        fInicio = findViewById(R.id.etFechaIniAPA);
        fFin = findViewById(R.id.etFechaFinAPA);
        nombre = findViewById(R.id.etNombreAPA);
        rv = findViewById(R.id.rvDispFechasAPA);
        direcciontv = findViewById(R.id.tvDIreccionAPA);

        mAuth = FirebaseAuth.getInstance();

        // spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipoAlojamiento, R.layout.item_anf_spinner);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //imgAnfAdapter = new ImagenAnfitrionAdapter(AnfitrionPublicarAlojamientoActivity.this,listaImagenes);

        accionBotones();
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        modo = bundle.getInt("modo");
        if(modo == 2)
        {
            alojamiento = (Alojamiento) bundle.getSerializable("Data");
            llenarDatos();
        }else
        {
            alojamiento = new Alojamiento();
        }
    }

     public void llenarDatos(){
        siguiente.setText("guardar");
        nombre.setText(alojamiento.getNombre());
        descripcion.setText(alojamiento.getDescripcion());
        valor.setText( String.valueOf(alojamiento.getPrecio()));
        direcciontv.setText(alojamiento.getUbicacion().getDireccion());
        // TODO falta valor spinner
    }


    // Seccion spinner -----------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       tipoAlojamiento = adapterView.getItemAtPosition(i).toString();
       System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
        alojamiento.setTipo(tipoAlojamiento);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        alojamiento.setTipo("indeterminado");
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "NULL", Toast.LENGTH_SHORT);

        toast1.show();
    }
    // FIN Seccion spinner -----------------------------------------------


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
                direcciontv.setText(direccion);
                alojamiento.setUbicacion(u);
                //listDatos.add(u);
                System.out.println("---------------------AAAAAA:"+ latitud);
                //adapter.addItem(u);

            }
        }
            }

    // FIN seccion place picker ----------------------------------------------------------



    public boolean validarCampos(){

        boolean bandera = true;
        if(nombre.getText().toString().isEmpty())
        {
            nombre.setError("Campo requerido");
            bandera = false;
        }

        if(descripcion.getText().toString().isEmpty()){
            descripcion.setError("Campo requerido");
            bandera = false;
        }

        if(valor.getText().toString().isEmpty()){
            valor.setError("Campo requerido");
            bandera = false;
        }

        if(alojamiento.getUbicacion() == null)
        {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Seleccione una ubicacion", Toast.LENGTH_SHORT);

            toast1.show();
            bandera = false;
        }

        if(alojamiento.getTipo() == null)
        {
            alojamiento.setTipo(spinner.getSelectedItem().toString());
        }

        return bandera;
    }

    public void subirAlojamiento(){
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        alojamiento.setIdUsuario(uid);
        //FirebaseUser user = mAuth.getCurrentUser();
        Random random = new Random();
        int numRandom = random.nextInt(10000)+1;
        //final String idAlojamiento = "Alojamiento "+String.valueOf(numRandom)+String.valueOf(System.currentTimeMillis());
        //alojamiento.setId(idAlojamiento);
        FirebaseDatabase.getInstance().getReference("Alojamientos").child(alojamiento.getId()).setValue(alojamiento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    nProgressDialog.dismiss();
                    Toast.makeText(AnfitrionPublicarAlojamientoActivity.this, "Se ha publicado el alojamiento", Toast.LENGTH_SHORT).show();
                    //infoActualUsuario(idServicio,ususerv);
                    //agregarDatosUsuarioServicio(alojamiento.getId());

                }
                else{
                    nProgressDialog.dismiss();
                    Toast.makeText( AnfitrionPublicarAlojamientoActivity.this,"Hubo un error al crear un servicio", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    // seccion botones ---------------------------------------------------------
    public void accionBotones() {

        siguiente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(modo == 1) {
                    if (validarCampos()) {
                        alojamiento.setDescripcion(descripcion.getText().toString());
                        alojamiento.setPrecio(Long.parseLong(valor.getText().toString()));
                        alojamiento.setNombre(nombre.getText().toString());


                        Intent intent = new Intent(view.getContext(), AnfitrionPublicarDetalleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Data", alojamiento);
                        bundle.putInt("modo",1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        //TODO hacer toast
                    }
                }else{
                    if (validarCampos()) {
                    alojamiento.setDescripcion(descripcion.getText().toString());
                    alojamiento.setPrecio(Long.parseLong(valor.getText().toString()));
                    alojamiento.setNombre(nombre.getText().toString());

                    // TODO guardar datos firebase
                        nProgressDialog.setMessage("Publicando el alojamiento...");
                        nProgressDialog.show();
                        subirAlojamiento();

                    Intent intent = new Intent(view.getContext(), AnfMenuEditarActivity.class);
                    startActivity(intent);
                    }
                }


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
            Intent intentStop = new Intent(AnfitrionPublicarAlojamientoActivity.this,ReservasService.class);
            stopService(intentStop);
            Intent intentOpinionStop =  new Intent (AnfitrionPublicarAlojamientoActivity.this,CalificacionAlojamientoService.class);
            stopService(intentOpinionStop);
            Intent intent = new Intent(AnfitrionPublicarAlojamientoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // FIN seccion botones ---------------------------------------------------------
}