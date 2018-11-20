package javeriana.edu.co.homenet.activities.anfitrion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.adapters.AnfPubAlojamientoDispAdapter;
import javeriana.edu.co.homenet.fragment.AnfitrionDatePickerFragment;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Disponibilidad;

public class AnfitrionPublicarDisponibilidadActivity extends AppCompatActivity
    implements DatePickerDialog.OnDateSetListener{

    public static final int FLAG_START_DATE = 0;
    public static final int FLAG_END_DATE = 1;
    int flag;

    private FirebaseAuth mAuth;

    ArrayList<Disponibilidad> disponibilidads;
    AnfPubAlojamientoDispAdapter adapterDispo;

    Alojamiento alojamiento;

    Button fechaIni;
    Button fechaFin;
    Button agregarDisp;
    Button siguiente;

    EditText fInicio;
    EditText fFin;

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_pub_disponibilidad);
        alojamiento = (Alojamiento) getIntent().getSerializableExtra("Data");

        mAuth = FirebaseAuth.getInstance();

        System.out.println("+++++++++"+alojamiento.getNombre());
        System.out.println("+++++++++"+alojamiento.getTipo());
        System.out.println("+++++++++"+alojamiento.getPrecio());
        System.out.println("+++++++++"+alojamiento.getDescripcion());



        fechaIni = findViewById(R.id.btFechaIniAPA);
        fechaFin = findViewById(R.id.btFechaFinAPA);
        siguiente = findViewById(R.id.btSiguienteAPA);
        agregarDisp = findViewById(R.id.btAgregarFechaAPA);
        fInicio = findViewById(R.id.etFechaIniAPA);
        fFin = findViewById(R.id.etFechaFinAPA);
        rv = findViewById(R.id.rvDispFechasAPA);

        disponibilidads = new ArrayList<Disponibilidad>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapterDispo = new AnfPubAlojamientoDispAdapter(disponibilidads);
        rv.setAdapter(adapterDispo);

        accionBotones();
    }


    public  boolean validarCampos(){
        boolean bandera = true;

        if(fInicio.getText().toString().isEmpty())
        {
            fInicio.setError("Campo requerido");
            bandera = false;
        }

        if(fFin.getText().toString().isEmpty())
        {
            fFin.setError("Campo requerido");
            bandera = false;
        }

        return bandera;
    }

    public boolean validarDisp(){
        if(disponibilidads.size() < 1)
        {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Ingrese al menos una disponibilidad", Toast.LENGTH_SHORT);

            toast1.show();
            return  false;
        }
        else
        {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Correcto", Toast.LENGTH_SHORT);

            toast1.show();
            return true;
        }
    }

    public void borrarCampos(){
        fFin.setText("");
        fInicio.setText("");
    }

    public void accionBotones() {


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

        agregarDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validarCampos())
                {
                    Disponibilidad d = new Disponibilidad();
                    d.setFechaInicio(fInicio.getText().toString());
                    d.setFechaFin(fFin.getText().toString());
                    adapterDispo.addItem(d);

                    borrarCampos();
                }

            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarDisp())
                {
                    alojamiento.setDisponibilidades(adapterDispo.getDisponibilidads());
                   // Alojamiento a = new Alojamiento();
                    //a.setDisponibilidades(adapterDispo.getDisponibilidads());
                    System.out.println("CONTINUA");
                    Intent intent = new Intent(view.getContext(),AnfitrionPublicarAlojamientoImgActivity.class);
                    intent.putExtra("Data", alojamiento);
                    startActivity(intent);
                }


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
            fFin.setError(null);
            String currentDate = firstDateFormat.format(c.getTime());
            //String currentDate = DateFormat.getDateInstance().format(c.getTime());
            fFin.setText(currentDate);
        }
        else {
            fInicio.setError(null);
            String currentDate = firstDateFormat.format(c.getTime());
            //String currentDate = DateFormat.getDateInstance().format(c.getTime());
            fInicio.setText(currentDate);
        }


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
            Intent intent = new Intent(AnfitrionPublicarDisponibilidadActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
