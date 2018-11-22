package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.models.Alojamiento;
import javeriana.edu.co.homenet.models.Tour;

import static javeriana.edu.co.homenet.activities.guia.GuiaRenovarTourActivity.newInstance;

public class GuiaDetalleTourActivity extends AppCompatActivity {

    public static final String PATH_TOURS="Tours/";

    BottomNavigationView navigation;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tours:
                    item.setIntent(new Intent(GuiaDetalleTourActivity.this, GuiaPrincipalActivity.class));
                    return true;
                case R.id.navigation_new_tour:
                    item.setIntent(new Intent(GuiaDetalleTourActivity.this, GuiaCrearTourActivity.class));
                    return true;
                case R.id.navigation_history_tours:
                    item.setIntent(new Intent(GuiaDetalleTourActivity.this, GuiaHistorialToures.class));
                    return true;
                case R.id.navigation_config:
                    item.setIntent(new Intent(GuiaDetalleTourActivity.this, GuiaCalificacionesActivity.class));
                    return true;
            }
            return false;
        }
    };

    TextView precio;
    TextView descripcion;
    TextView nombre;
    TextView fecha;
    TextView hora;
    TextView duracion;
    TextView capacidad;
    ImageView poster;
    Button recorrido;
    Button renovar;
    Button editar;
    Button eliminar;
    Button usuarios;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    private String idTour;
    private Tour tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_detalle_tour);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        precio = findViewById(R.id.tvPrecioGDT);
        descripcion = findViewById(R.id.tvDescripcionITGP2);
        nombre = findViewById(R.id.tvNombreGDT);
        fecha = findViewById(R.id.tvDateGDT);
        hora = findViewById(R.id.tvHourGDT);
        duracion = findViewById(R.id.tvDurationGDT);
        capacidad = findViewById(R.id.tvCapacidadGDT);
        poster = findViewById(R.id.imgTourGDT);
        recorrido = findViewById(R.id.btRecorridoGDT);
        renovar = findViewById(R.id.btRenovarGDT);
        editar = findViewById(R.id.btEditarGDT);
        eliminar = findViewById(R.id.btEliminarGDT);
        usuarios = findViewById(R.id.btVerGDT);

        Intent bA = getIntent();
        Bundle b = bA.getExtras();
        idTour = b.getString("idTour");
        findTour();

        renovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = GuiaRenovarTourActivity.newInstance(idTour);
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = GuiaEliminarTourActivity.newInstance(idTour);
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });
        usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("idTour", idTour);
                startActivity(new Intent(v.getContext(),GuiaUsuariosTourActivity.class).putExtras(b));
            }
        });

    }

    private void findTour() {
        myRef = database.getReference(PATH_TOURS/*+"/"+idTour*/);
        myRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Tour itour = singleSnapshot.getValue(Tour.class);
                    if (singleSnapshot.getKey().equals(idTour)) {
                        tour = itour;
                    }
                }
                llenarDatosVista();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase database", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void llenarDatosVista(){
        precio.setText(String.valueOf(tour.getPrecio()).concat(" "+tour.getMoneda()));
        descripcion.setText(tour.getDescripcion());
        nombre.setText(tour.getTitulo());
        fecha.setText(tour.getFecha());
        hora.setText(tour.getHora());
        duracion.setText(String.valueOf(tour.getDuracion()).concat(" horas"));
        capacidad.setText(String.valueOf(tour.getInscritos()).concat(" de "+tour.getCapacidad()));
        try{
            Picasso.get()
                    .load(tour.getUrlImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(poster);
        }catch(Exception ex){
            ex.printStackTrace();
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
            Intent intent = new Intent(GuiaDetalleTourActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
