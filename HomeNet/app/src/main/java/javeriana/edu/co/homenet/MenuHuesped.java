package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuHuesped extends AppCompatActivity {

    Button consultarAlojamientos;
    Button verHistorialReservas;
    Button verGuiasCercanos;
    Button verHistorialRecorridos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_huesped);

        consultarAlojamientos = findViewById(R.id.consultarAlojamientos);
        verHistorialReservas = findViewById(R.id.verHistorialReservas);
        verGuiasCercanos = findViewById(R.id.verGuiasCercanos);
        verHistorialRecorridos = findViewById(R.id.verHistorialRecorridos);

        consultarAlojamientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ConsultarAlojamientoActivity.class);
                startActivity(intent);
            }
        });

        verHistorialReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DetallesHistorialReservaActivity.class);
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
                Intent intent = new Intent(v.getContext(),SolicitudesAceptadasDelGuiaActivity.class);
                startActivity(intent);
            }
        });

    }
}
