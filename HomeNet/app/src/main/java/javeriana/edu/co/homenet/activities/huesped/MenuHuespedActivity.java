package javeriana.edu.co.homenet.activities.huesped;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javeriana.edu.co.homenet.R;
import javeriana.edu.co.homenet.activities.guia.GuiasDisponiblesActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedConsultarAlojamientoActivity;
import javeriana.edu.co.homenet.activities.huesped.alojamientos.HuespedDetallesHistorialReservaActivity;
import javeriana.edu.co.homenet.activities.huesped.guias.HuespedHistorialRecorridosActivity;

public class MenuHuespedActivity extends AppCompatActivity {

    Button consultarAlojamientos;
    Button verHistorialReservas;
    Button verGuiasCercanos;
    Button verHistorialRecorridos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huesped_menu);

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
}
