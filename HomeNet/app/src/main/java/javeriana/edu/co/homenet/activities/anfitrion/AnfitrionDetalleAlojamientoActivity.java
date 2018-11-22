package javeriana.edu.co.homenet.activities.anfitrion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.models.Alojamiento;

public class AnfitrionDetalleAlojamientoActivity extends AppCompatActivity {

    Alojamiento alojamiento;

    Button historial;
    Button borrar;
    Button editar;
    Button comentarios;

    TextView tipo;
    TextView ubicacion;
    TextView valor;
    TextView descriopcion;
    TextView banios;
    TextView camas;
    TextView dormitorios;
    TextView huespedes;
    TextView parqueaderos;
    TextView calefaccion;
    TextView internet;
    TextView television;
    TextView mascotas;



    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anf_detalle_alojamiento);
        alojamiento = (Alojamiento) getIntent().getSerializableExtra("Data");

        mAuth = FirebaseAuth.getInstance();

        historial = findViewById(R.id.historial_alo);

        tipo = findViewById(R.id.tvTipoQ);
        ubicacion = findViewById(R.id.tvUbicacionQ);
        valor = findViewById(R.id.tvValorQ);
        descriopcion = findViewById(R.id.tvDescripcionQ);
        banios = findViewById(R.id.tvBaniosQ);
        camas = findViewById(R.id.tvCamasQ);
        dormitorios = findViewById(R.id.tvDormitoriosQ);
        huespedes = findViewById(R.id.tvHuespedesQ);
        parqueaderos = findViewById(R.id.tvParqueaderosQ);
        calefaccion = findViewById(R.id.tvCalefaccionQ);
        internet = findViewById(R.id.tvInternetQ);
        television = findViewById(R.id.tvTelevisionQ);
        mascotas = findViewById(R.id.tvMascotasQ);




        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionHistorialReservasActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Data", alojamiento);
                bundle.putInt("modo",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        editar = findViewById(R.id.editar_alo);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfMenuEditarActivity.class);
                intent.putExtra("Data", alojamiento);
                startActivity(intent);
            }
        });

        borrar = findViewById(R.id.borrar_alo);

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfitrionMenuActivity.class);
                startActivity(intent);
            }
        });

        comentarios = findViewById(R.id.btComentarioADA);
        comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AnfComentariosActivity.class);
                intent.putExtra("Data", alojamiento);
                startActivity(intent);
            }
        });
        llenarDatos();
    }

    public void llenarDatos(){
        tipo.setText(alojamiento.getTipo());
        ubicacion.setText(alojamiento.getUbicacion().getDireccion());
        descriopcion.setText(alojamiento.getDescripcion());
        valor.setText(String.valueOf(alojamiento.getPrecio()));
        System.out.println("ZZZZZZ"+alojamiento.getBanios());
        System.out.println(banios.getText().toString());
        banios.setText(String.valueOf(alojamiento.getBanios()));
        camas.setText(String.valueOf(alojamiento.getCamas()));
        dormitorios.setText(String.valueOf(alojamiento.getDormitorios()));
        huespedes.setText(String.valueOf(alojamiento.getHuespedes()));
        parqueaderos.setText(String.valueOf(alojamiento.getParqueaderos()));
        calefaccion.setText(String.valueOf(alojamiento.isCalefaccion()));
        internet.setText(String.valueOf(alojamiento.isInternet()));
        television.setText(String.valueOf(alojamiento.isTelevision()));
        mascotas.setText(String.valueOf(alojamiento.isMascotas()));
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
            Intent intent = new Intent(AnfitrionDetalleAlojamientoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
