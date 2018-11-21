package javeriana.edu.co.homenet.activities.guia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javeriana.edu.co.homenet.R;

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
                    return true;
                case R.id.navigation_config:
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

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_detalle_tour);

        database = FirebaseDatabase.getInstance();

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

    }
}
