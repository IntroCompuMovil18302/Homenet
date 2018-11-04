package javeriana.edu.co.homenet.activities.huesped;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.LoginActivity;
import javeriana.edu.co.homenet.activities.guia.GuiasDisponiblesActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedConsultarAlojamientoActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedDetallesHistorialReservaActivity;
import javeriana.edu.co.homenet.activities.huesped.guias.HuespedHistorialRecorridosActivity;

public class MenuHuespedActivity extends AppCompatActivity {

    Button consultarAlojamientos;
    Button verHistorialReservas;
    Button verGuiasCercanos;
    Button verHistorialRecorridos;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_menu);

        mAuth = FirebaseAuth.getInstance();

        consultarAlojamientos = findViewById(R.id.consultarAlojamientos);
        verHistorialReservas = findViewById(R.id.verHistorialReservas);
        verGuiasCercanos = findViewById(R.id.verGuiasCercanos);
        verHistorialRecorridos = findViewById(R.id.verHistorialRecorridos);

        consultarAlojamientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedConsultarAlojamientoActivity.class);
                startActivity(intent);
            }
        });

        verHistorialReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedDetallesHistorialReservaActivity.class);
                startActivity(intent);
            }
        });

        verGuiasCercanos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),GuiasDisponiblesActivity.class);
                startActivity(intent);
            }
        });

        verHistorialRecorridos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HuespedHistorialRecorridosActivity.class);
                startActivity(intent);
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
            Intent intent = new Intent(MenuHuespedActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (itemClicked == R.id.menuSettings){
            //Abrir actividad para configuración etc
        }
        return super.onOptionsItemSelected(item);
    }
}
