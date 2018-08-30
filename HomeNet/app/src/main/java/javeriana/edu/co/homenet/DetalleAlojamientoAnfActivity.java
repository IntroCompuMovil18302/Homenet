package javeriana.edu.co.homenet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetalleAlojamientoAnfActivity extends AppCompatActivity {

    Button historial;
    Button borrar;
    Button editar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alojamiento_anf);

        historial = findViewById(R.id.historial_alo);

        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),HistorialReservasAnfActivity.class);
                startActivity(intent);
            }
        });

        editar = findViewById(R.id.editar_alo);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),PublicarAlojamientoAnfActivity.class);
                startActivity(intent);
            }
        });

        borrar = findViewById(R.id.borrar_alo);

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MenuAnfitrionActivity.class);
                startActivity(intent);
            }
        });
    }
}
